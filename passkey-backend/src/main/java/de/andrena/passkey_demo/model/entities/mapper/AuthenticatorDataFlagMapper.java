package de.andrena.passkey_demo.model.entities.mapper;

import com.yubico.webauthn.data.AuthenticatorDataFlags;
import de.andrena.passkey_demo.model.entities.AuthenticatorDataFlagsWrapper;

public class AuthenticatorDataFlagMapper {
    private AuthenticatorDataFlagMapper() {
    }

    public static AuthenticatorDataFlagsWrapper map(AuthenticatorDataFlags authenticatorDataFlags) {
        return AuthenticatorDataFlagsWrapper.builder()
                .value(authenticatorDataFlags.value)
                .UP(authenticatorDataFlags.UP)
                .UV(authenticatorDataFlags.UV)
                .BE(authenticatorDataFlags.BE)
                .BS(authenticatorDataFlags.BS)
                .AT(authenticatorDataFlags.AT)
                .ED(authenticatorDataFlags.ED)
                .build();
    }
}
