package de.andrena.passkey_demo.model.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatorDataFlagsWrapper {
    public byte value;
    public boolean UP;
    public boolean UV;
    public boolean BE;
    public boolean BS;
    public boolean AT;
    public boolean ED;
}
