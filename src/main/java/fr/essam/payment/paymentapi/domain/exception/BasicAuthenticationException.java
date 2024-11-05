package fr.essam.payment.paymentapi.domain.exception;

public class BasicAuthenticationException extends RuntimeException {
    public BasicAuthenticationException(String message) {
        super(message);
    }
}
