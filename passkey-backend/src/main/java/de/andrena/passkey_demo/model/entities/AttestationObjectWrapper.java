package de.andrena.passkey_demo.model.entities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yubico.webauthn.data.ByteArray;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttestationObjectWrapper {
    public ByteArray bytes;
    public AuthenticatorDataWrapper authenticatorData;
    public String format;
    public ObjectNode attestationStatement;
}
