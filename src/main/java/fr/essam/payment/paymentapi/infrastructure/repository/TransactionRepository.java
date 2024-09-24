package fr.essam.payment.paymentapi.infrastructure.repository;

import fr.essam.payment.paymentapi.infrastructure.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository  extends CrudRepository<Transaction, Long> {
}
