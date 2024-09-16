package de.andrena.passkey_demo.model.entities.mapper;

import com.yubico.webauthn.data.AttestedCredentialData;
import com.yubico.webauthn.data.AuthenticatorData;
import de.andrena.passkey_demo.model.entities.AuthenticatorDataWrapper;

import java.util.Optional;

public class AuthenticatorDataMapper {
    private AuthenticatorDataMapper() {
    }

    public static AuthenticatorDataWrapper map(AuthenticatorData authenticatorData) {
        Optional<AttestedCredentialData> attestedCredentialData = authenticatorData.getAttestedCredentialData();
        return AuthenticatorDataWrapper.builder()
                .bytes(authenticatorData.getBytes())
                .rpIdHash(authenticatorData.getRpIdHash())
                .signatureCounter(authenticatorData.getSignatureCounter())
                .flags(AuthenticatorDataFlagMapper.map(authenticatorData.getFlags()))
                .attestedCredentialData(attestedCredentialData.map(AttestedCredentialDataMapper::map).orElse(null))
                .build();
    }
}
