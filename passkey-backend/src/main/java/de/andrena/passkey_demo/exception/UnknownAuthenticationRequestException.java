package de.andrena.passkey_demo.exception;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
public class UnknownAuthenticationRequestException extends RuntimeException {
    public UnknownAuthenticationRequestException(String message) {
        super(message);
    }
}
