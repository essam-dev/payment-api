package fr.essam.payment.paymentapi.application.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionRequest {

    @NotNull(message = "Transactions field is missing")
    private List<Transaction> transactions;

}
