package de.andrena.passkey_demo.model;

import com.yubico.fido.metadata.FidoMetadataDownloader;
import com.yubico.fido.metadata.FidoMetadataDownloaderException;
import com.yubico.fido.metadata.FidoMetadataService;
import com.yubico.fido.metadata.UnexpectedLegalHeader;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.AttestationConveyancePreference;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.yubico.webauthn.data.exception.Base64UrlException;
import de.andrena.passkey_demo.persistence.CustomCredentialRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Component
@Getter
public class ApplicationRelyingParty {

    private final RelyingParty relyingParty;
    private final FidoMetadataService mds;

    public ApplicationRelyingParty(CustomCredentialRepository customCredentialRepository, RelyingPartyProperties properties) {
        RelyingPartyIdentity relyingPartyIdentity = RelyingPartyIdentity.builder()
                .id(properties.getId())
                .name(properties.getName())
                .build();

        FidoMetadataDownloader downloader = FidoMetadataDownloader.builder()
                .expectLegalHeader("Retrieval and use of this BLOB indicates acceptance of the appropriate agreement located at https://fidoalliance.org/metadata/metadata-legal-terms/")
                .useDefaultTrustRoot()
                .useTrustRootCacheFile(new File("/tmp/fido-mds-trust-root.bin"))
                .useDefaultBlob()
                .useBlobCacheFile(new File("/tmp/fido-mds-blob.bin"))
                .verifyDownloadsOnly(true)
                .build();

        try {

            mds = FidoMetadataService.builder()
                    .useBlob(downloader.loadCachedBlob())
                    .build();

            relyingParty = RelyingParty.builder()
                    .identity(relyingPartyIdentity)
                    .credentialRepository(customCredentialRepository)
                    .attestationTrustSource(mds)
                    .allowOriginPort(properties.isAllowOriginPort())
                    .allowOriginSubdomain(true)
                    .origins(properties.getAllowedOrigins())
                    .attestationConveyancePreference(AttestationConveyancePreference.DIRECT)
                    .allowUntrustedAttestation(true)
                    .build();

        } catch (CertPathValidatorException | InvalidAlgorithmParameterException | Base64UrlException |
                 DigestException | FidoMetadataDownloaderException | CertificateException | UnexpectedLegalHeader |
                 IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
