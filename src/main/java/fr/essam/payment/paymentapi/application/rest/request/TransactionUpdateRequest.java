package fr.essam.payment.paymentapi.application.rest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionUpdateRequest {
    private Transaction transaction;

}
