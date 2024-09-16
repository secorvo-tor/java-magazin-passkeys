package de.andrena.passkey_demo.model.entities;

import com.yubico.webauthn.data.ByteArray;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttestedCredentialDataWrapper {
    public ByteArray aaguid;
    public ByteArray credentialId;
    public ByteArray credentialPublicKey;
}
