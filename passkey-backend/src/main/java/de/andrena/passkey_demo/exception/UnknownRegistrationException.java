package de.andrena.passkey_demo.exception;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
public class UnknownRegistrationException extends RuntimeException {
    public UnknownRegistrationException(String message) {
        super(message);
    }
}
