package fr.essam.payment.paymentapi.domain.ports;

import fr.essam.payment.paymentapi.domain.model.TransactionInput;
import fr.essam.payment.paymentapi.domain.model.TransactionOutput;
import fr.essam.payment.paymentapi.domain.model.TransactionUpdateInput;
import fr.essam.payment.paymentapi.domain.model.TransactionUpdateOutput;

public interface PaymentServicePort {

    TransactionOutput generatePayments(TransactionInput transactionInput);

    TransactionOutput getPayments();

    TransactionOutput getPayment(String paymentId);

    TransactionUpdateOutput updatePayment(TransactionUpdateInput transactionUpdateInput);
}
