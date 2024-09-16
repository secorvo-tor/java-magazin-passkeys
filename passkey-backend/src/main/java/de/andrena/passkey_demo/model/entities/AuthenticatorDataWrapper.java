package de.andrena.passkey_demo.model.entities;

import com.yubico.webauthn.data.ByteArray;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatorDataWrapper {
    public ByteArray bytes;
    public ByteArray rpIdHash;
    public long signatureCounter;
    public AuthenticatorDataFlagsWrapper flags;
    public AttestedCredentialDataWrapper attestedCredentialData;
}
