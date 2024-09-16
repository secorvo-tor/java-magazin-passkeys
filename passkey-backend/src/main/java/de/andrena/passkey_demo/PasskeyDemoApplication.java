package de.andrena.passkey_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import de.andrena.passkey_demo.model.RelyingPartyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RelyingPartyProperties.class)
public class PasskeyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasskeyDemoApplication.class, args);
	}

}
