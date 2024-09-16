package de.andrena.passkey_demo.model.entities.mapper;

import com.yubico.webauthn.data.AttestationObject;
import de.andrena.passkey_demo.model.entities.AttestationObjectWrapper;

public class AttestationObjectMapper {
    private AttestationObjectMapper() {
    }

    public static AttestationObjectWrapper map(AttestationObject attestationObject) {
        return AttestationObjectWrapper.builder()
                .bytes(attestationObject.getBytes())
                .authenticatorData(AuthenticatorDataMapper.map(attestationObject.getAuthenticatorData()))
                .format(attestationObject.getFormat())
                .attestationStatement(attestationObject.getAttestationStatement())
                .build();
    }
}
