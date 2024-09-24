package fr.essam.payment.paymentapi.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionUpdateInput {
    private TransactionDTO transaction;
    private String transactionId;

}
