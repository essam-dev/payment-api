package fr.essam.payment.paymentapi.domain.ports;

import fr.essam.payment.paymentapi.domain.model.TransactionDTO;

import java.util.List;

public interface TransactionRepositoryPort {
    TransactionDTO saveTransaction(TransactionDTO transactionDTO);

    List<TransactionDTO> findAllTransaction();

    TransactionDTO findTransactionById(String id);

    TransactionDTO updateTransactionById(String id, TransactionDTO transactionDTO);

}
