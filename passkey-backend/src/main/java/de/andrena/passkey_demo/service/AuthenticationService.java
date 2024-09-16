package de.andrena.passkey_demo.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.exception.AssertionFailedException;

import de.andrena.passkey_demo.exception.AuthenticationException;
import de.andrena.passkey_demo.exception.UnknownAuthenticationRequestException;
import de.andrena.passkey_demo.model.ApplicationRelyingParty;
import de.andrena.passkey_demo.persistence.CustomCredentialRepository;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Service
public class AuthenticationService {

    private final CustomCredentialRepository customCredentialRepository;
    private final ApplicationRelyingParty applicationRelyingParty;

    private final ConcurrentHashMap<String, AssertionRequest> authenticationRequests = new ConcurrentHashMap<>();

    public AuthenticationService(CustomCredentialRepository customCredentialRepository, ApplicationRelyingParty applicationRelyingParty) {
        this.customCredentialRepository = customCredentialRepository;
        this.applicationRelyingParty = applicationRelyingParty;
    }

    public String authenticate(String username) throws JsonProcessingException {
        if ("".equals(username)) {
            username = null;
        }
        final AssertionRequest assertionRequest = applicationRelyingParty.getRelyingParty()
                                                                         .startAssertion(StartAssertionOptions.builder()
                                                                                                              .username(username)
                                                                                                              .build());
        authenticationRequests.put(assertionRequest.getPublicKeyCredentialRequestOptions()
                                                   .getChallenge()
                                                   .getBase64(), assertionRequest);
        return assertionRequest.toCredentialsGetJson();
    }

    public AssertionResult finishAuthentication(String authentication) throws IOException {
        final PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc = PublicKeyCredential.parseAssertionResponseJson(
            authentication);

        final AssertionRequest assertionRequest = authenticationRequests.get(pkc.getResponse()
                                                                                .getClientData()
                                                                                .getChallenge()
                                                                                .getBase64());
        if (assertionRequest == null) {
            throw new UnknownAuthenticationRequestException("No matching authentication in progress");
        }
        authenticationRequests.remove(pkc.getId()
                                         .getBase64());

        final FinishAssertionOptions finishAssertionOptions = FinishAssertionOptions.builder()
                                                                                    .request(assertionRequest)
                                                                                    .response(pkc)
                                                                                    .build();

        AssertionResult assertionResult;
        try {
            assertionResult = applicationRelyingParty.getRelyingParty()
                                                                           .finishAssertion(finishAssertionOptions);
            if (assertionResult.isSuccess()) {
                customCredentialRepository.updateCredential(assertionResult.getUsername(),
                                                            assertionResult.getCredential(),
                                                            assertionResult.getSignatureCount());
                return assertionResult;
            }
        } catch (AssertionFailedException e) {
            throw new AuthenticationException("Failed to authenticate", e);
        }

        return assertionResult;
    }
}
