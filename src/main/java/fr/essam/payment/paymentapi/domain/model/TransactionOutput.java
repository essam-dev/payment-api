package fr.essam.payment.paymentapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutput {
    private List<TransactionDTO> transactions;

    @JsonIgnore
    public void addTransaction(TransactionDTO transaction) {
        if(transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
    }

}
