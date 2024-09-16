package de.andrena.passkey_demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import de.andrena.passkey_demo.exception.RegistrationException;
import de.andrena.passkey_demo.exception.UnknownRegistrationException;
import de.andrena.passkey_demo.model.ApplicationRelyingParty;
import de.andrena.passkey_demo.model.entities.UserRegistration;
import de.andrena.passkey_demo.model.entities.mapper.AttestationObjectMapper;
import de.andrena.passkey_demo.persistence.CustomCredentialRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Service
public class RegistrationService {

    private final ApplicationRelyingParty applicationRelyingParty;
    private final CustomCredentialRepository customCredentialRepository;

    private final ConcurrentHashMap<String, PublicKeyCredentialCreationOptions> registrationRequests = new ConcurrentHashMap<>();

    public RegistrationService(CustomCredentialRepository customCredentialRepository, ApplicationRelyingParty applicationRelyingParty) {
        this.customCredentialRepository = customCredentialRepository;
        this.applicationRelyingParty = applicationRelyingParty;
    }

    public String startRegistration(String userName) throws JsonProcessingException {
        final AuthenticatorSelectionCriteria authenticatorSelection = AuthenticatorSelectionCriteria.builder()
                .residentKey(ResidentKeyRequirement.REQUIRED)
                //.authenticatorAttachment(AuthenticatorAttachment.CROSS_PLATFORM)
                .userVerification(UserVerificationRequirement.DISCOURAGED)
                .build();
        StartRegistrationOptions options = StartRegistrationOptions.builder()
                .user(customCredentialRepository.findExistingUser(userName)
                        .orElseGet(() -> createNewUser(userName)))
                .authenticatorSelection(authenticatorSelection)
                .build();
        PublicKeyCredentialCreationOptions request = applicationRelyingParty.getRelyingParty()
                .startRegistration(options);

        registrationRequests.put(request.getChallenge()
                .getBase64(), request);
        return request.toCredentialsCreateJson();
    }

    private static UserIdentity createNewUser(String userName) {
        byte[] userHandle = new byte[64];
        new Random().nextBytes(userHandle);
        return UserIdentity.builder()
                .name(userName)
                .displayName(userName)
                .id(new ByteArray(userHandle))
                .build();
    }

    public UserRegistration finishRegistration(String credentials) throws IOException, RegistrationFailedException {
        final PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc = PublicKeyCredential.parseRegistrationResponseJson(
                credentials);

        final String challenge = pkc.getResponse()
                .getClientData()
                .getChallenge()
                .getBase64();
        final PublicKeyCredentialCreationOptions registrationRequest = registrationRequests.get(challenge);
        if (registrationRequest == null) {
            throw new UnknownRegistrationException("Registration request unknown");
        }
        registrationRequests.remove(challenge);

        final FinishRegistrationOptions options = FinishRegistrationOptions.builder()
                .request(registrationRequest)
                .response(pkc)
                .build();

        final RegistrationResult registrationResult;
        try {
            registrationResult = applicationRelyingParty.getRelyingParty()
                    .finishRegistration(options);
        } catch (RegistrationFailedException e) {
            throw new RegistrationException("Could not finish registration", e);
        }

        final RegisteredCredential registeredCredential = RegisteredCredential.builder()
                .credentialId(registrationResult.getKeyId()
                        .getId())
                .userHandle(registrationRequest.getUser()
                        .getId())
                .publicKeyCose(registrationResult.getPublicKeyCose())
                .build();

        AttestationObject attestationObject = new AttestationObject(pkc.getResponse().getAttestationObject());
        final UserRegistration userRegistration = UserRegistration.builder()
                .userIdentity(registrationRequest.getUser())
                .registeredCredential(registeredCredential)
                .credentialDescriptor(registrationResult.getKeyId())
                .registrationTime(Instant.now())
                .attestationObject(AttestationObjectMapper.map(attestationObject))
                .isAttestationTrusted(registrationResult.isAttestationTrusted())
                .additionalAuthenticatorMetadata(applicationRelyingParty.getMds().findEntries(registrationResult))
                .build();

        customCredentialRepository.addUserRegistration(userRegistration);
        return userRegistration;
    }
}

