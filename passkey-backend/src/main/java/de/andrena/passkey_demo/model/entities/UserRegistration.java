package de.andrena.passkey_demo.model.entities;

import java.time.Instant;
import java.util.Set;

import com.yubico.fido.metadata.MetadataBLOBPayloadEntry;
import lombok.Builder;
import lombok.Data;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.UserIdentity;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Data
@Builder
public class UserRegistration {
    UserIdentity userIdentity;
    RegisteredCredential registeredCredential;
    PublicKeyCredentialDescriptor credentialDescriptor;
    Instant registrationTime;
    AttestationObjectWrapper attestationObject;
    boolean isAttestationTrusted;
    Set<MetadataBLOBPayloadEntry> additionalAuthenticatorMetadata;
}
