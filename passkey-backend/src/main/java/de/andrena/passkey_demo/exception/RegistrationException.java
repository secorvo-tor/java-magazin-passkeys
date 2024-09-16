package de.andrena.passkey_demo.exception;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
