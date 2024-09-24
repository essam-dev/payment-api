package fr.essam.payment.paymentapi.domain.service;

import fr.essam.payment.paymentapi.domain.exception.PaymentException;
import fr.essam.payment.paymentapi.domain.model.*;
import fr.essam.payment.paymentapi.domain.ports.PaymentServicePort;
import fr.essam.payment.paymentapi.domain.ports.TransactionRepositoryPort;
import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService implements PaymentServicePort {

    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public TransactionOutput generatePayments(TransactionInput transactionInput) {
        TransactionOutput transactionOutput = new TransactionOutput();

        transactionInput.getTransactions().forEach(transactionDTO -> {

            checkTransactionCreation(transactionDTO);

            log.debug("Generating payment for transaction: {}", transactionDTO.getId());
            transactionOutput.addTransaction(transactionRepositoryPort.saveTransaction(transactionDTO));

        });

        return transactionOutput;
    }

    @Override
    public TransactionOutput getPayments() {
        log.debug("Getting all payments");
        return new TransactionOutput(transactionRepositoryPort.findAllTransaction());
    }

    @Override
    public TransactionOutput getPayment(String paymentId) {
        log.debug("Getting payment with id: {}", paymentId);
        TransactionOutput transactionOutput = new TransactionOutput();
        transactionOutput.addTransaction(transactionRepositoryPort.findTransactionById(paymentId));
        return transactionOutput;
    }

    @Override
    public TransactionUpdateOutput updatePayment(TransactionUpdateInput transactionUpdateInput) {
        log.debug("Updating payment with id: {} to status: {}", transactionUpdateInput.getTransactionId(),
                transactionUpdateInput.getTransaction().getPaymentStatus());


        TransactionDTO transactionDTO = transactionRepositoryPort.findTransactionById(transactionUpdateInput.getTransactionId());

        checkTransactionNewStatus(transactionDTO, transactionUpdateInput);

        checkTransactionImmutableStatus(transactionDTO);

        checkTransactionCaptureStatusChange(transactionDTO, transactionUpdateInput);

        return new TransactionUpdateOutput(
                transactionRepositoryPort.updateTransactionById(
                        transactionUpdateInput.getTransactionId(), transactionUpdateInput.getTransaction()));
    }

    private static void checkTransactionCreation(TransactionDTO transactionDTO) {
        if(PaymentStatus.NEW.compareTo(transactionDTO.getPaymentStatus()) != 0) {
            throw new PaymentException(String.format("New transaction must have the status: %s." +
                    " Provided status: %s", PaymentStatus.NEW, transactionDTO.getPaymentStatus()));
        }
    }

    private static void checkTransactionNewStatus(TransactionDTO transactionDTO, TransactionUpdateInput transactionUpdateInput) {
        if(PaymentStatus.NEW.compareTo(transactionDTO.getPaymentStatus()) != 0 &&
                !isRequestToUpdateOnlyStatus(transactionUpdateInput, transactionDTO)) {
            throw new PaymentException("Only payment with new status can be changed.");
        }
    }

    private static void checkTransactionImmutableStatus(TransactionDTO transactionDTO) {
        if(PaymentStatus.CAPTURED.compareTo(transactionDTO.getPaymentStatus()) == 0 ||
                PaymentStatus.CANCELED.compareTo(transactionDTO.getPaymentStatus()) == 0) {
            throw new PaymentException(String.format("Captured and canceled Transactions cannot be changed." +
                    " Current status:: %s", transactionDTO.getPaymentStatus()));
        }
    }

    private static void checkTransactionCaptureStatusChange(TransactionDTO transactionDTO, TransactionUpdateInput transactionUpdateInput) {
        if(PaymentStatus.CAPTURED.compareTo(transactionUpdateInput.getTransaction().getPaymentStatus()) == 0 &&
                PaymentStatus.AUTHORIZED.compareTo(transactionDTO.getPaymentStatus()) != 0) {
            throw new PaymentException(String.format("Transaction must be authorized before capturing." +
                    " Current status:: %s", transactionDTO.getPaymentStatus()));
        }
    }


    private static boolean isRequestToUpdateOnlyStatus(TransactionUpdateInput transactionUpdateInput, TransactionDTO transactionDTO) {
        TransactionDTO inputTransaction = transactionUpdateInput.getTransaction();
        return inputTransaction.getPaymentType() == transactionDTO.getPaymentType() &&
                inputTransaction.getTotalAmount() == transactionDTO.getTotalAmount() &&
                isItemsAreSame(inputTransaction.getItems(), transactionDTO.getItems());
    }

    private static boolean isItemsAreSame(List<ItemDTO> inputItems, List<ItemDTO> items) {
        boolean isItemsAreSame = true;
        if(inputItems.size() != items.size()) {
            return false;
        }

        for(ItemDTO inputItem : inputItems) {
            if(items.stream().noneMatch(inputItem::equals)) {
                return false;
            }
        }

        return isItemsAreSame;
    }

}
