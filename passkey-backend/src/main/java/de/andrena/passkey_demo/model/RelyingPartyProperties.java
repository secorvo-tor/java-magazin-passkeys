package de.andrena.passkey_demo.model;

import java.util.Set;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Data
@ConfigurationProperties(prefix = "relying-party")
public class RelyingPartyProperties {
    private String id;
    private String name;
    private boolean allowOriginPort;
    private Set<String> allowedOrigins;
}
