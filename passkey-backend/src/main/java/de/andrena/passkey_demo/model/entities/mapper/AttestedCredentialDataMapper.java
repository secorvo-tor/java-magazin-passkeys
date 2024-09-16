package de.andrena.passkey_demo.model.entities.mapper;

import com.yubico.webauthn.data.AttestedCredentialData;
import de.andrena.passkey_demo.model.entities.AttestedCredentialDataWrapper;

public class AttestedCredentialDataMapper {
    private AttestedCredentialDataMapper() {
    }

    public static AttestedCredentialDataWrapper map(AttestedCredentialData attestedCredentialData) {
        return AttestedCredentialDataWrapper.builder()
                .aaguid(attestedCredentialData.getAaguid())
                .credentialId(attestedCredentialData.getCredentialId())
                .credentialPublicKey(attestedCredentialData.getCredentialPublicKey())
                .build();
    }
}
