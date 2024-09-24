package fr.essam.payment.paymentapi.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionInput {
    private List<TransactionDTO> transactions;

}
