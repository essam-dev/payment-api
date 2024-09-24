package fr.essam.payment.paymentapi.application.rest.exception;

import fr.essam.payment.paymentapi.domain.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(PaymentException.class)
    protected ResponseEntity<Object> handleIamOtpSmsException(final PaymentException paymentException) {
        log.error("PaymentException occurred: {}", paymentException.getMessage());
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST.value(), paymentException.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }






}
