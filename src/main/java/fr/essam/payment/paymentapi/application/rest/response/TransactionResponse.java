package fr.essam.payment.paymentapi.application.rest.response;

import fr.essam.payment.paymentapi.domain.model.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponse {
    private List<TransactionDTO> transactions;

}
