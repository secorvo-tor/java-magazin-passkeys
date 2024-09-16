package de.andrena.passkey_demo.exception;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
