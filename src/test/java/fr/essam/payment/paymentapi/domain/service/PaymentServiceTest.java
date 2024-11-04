package fr.essam.payment.paymentapi.domain.service;

import fr.essam.payment.paymentapi.domain.exception.PaymentException;
import fr.essam.payment.paymentapi.domain.model.*;
import fr.essam.payment.paymentapi.domain.ports.TransactionRepositoryPort;
import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import fr.essam.payment.paymentapi.domain.utils.PaymentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PaymentService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @MockBean
    private TransactionRepositoryPort transactionRepositoryPort;

    @Test
    @DisplayName("Generate payment with unauthorized status")
    void testPaymentErrorCreation() {
        // Arrange
        ArrayList<TransactionDTO> transactionDTOList = new ArrayList<>();
        transactionDTOList
                .add(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.AUTHORIZED, new ArrayList<>()));
        TransactionInput transactionInput = mock(TransactionInput.class);
        when(transactionInput.getTransactions()).thenReturn(transactionDTOList);
        doNothing().when(transactionInput).setTransactions(Mockito.<List<TransactionDTO>>any());
        transactionInput.setTransactions(new ArrayList<>());

        // Act and Assert
        PaymentException paymentException = assertThrows(PaymentException.class, () -> paymentService.generatePayments(transactionInput));
        Assertions.assertThat(paymentException).hasMessage(String.format("New transaction must have the status: %s." +
                " Provided status: %s", PaymentStatus.NEW, PaymentStatus.AUTHORIZED));
    }

    @Test
    @DisplayName("Generate payment successfully")
    void testGeneratePayments() {
        // Arrange
        TransactionDTO transactionDTO = new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW,
                new ArrayList<>());

        when(transactionRepositoryPort.saveTransaction(Mockito.<TransactionDTO>any())).thenReturn(transactionDTO);

        ArrayList<TransactionDTO> transactionDTOList = new ArrayList<>();
        transactionDTOList
                .add(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));
        TransactionInput transactionInput = mock(TransactionInput.class);
        when(transactionInput.getTransactions()).thenReturn(transactionDTOList);
        doNothing().when(transactionInput).setTransactions(Mockito.<List<TransactionDTO>>any());
        transactionInput.setTransactions(new ArrayList<>());

        // Act
        TransactionOutput actualGeneratePaymentsResult = paymentService.generatePayments(transactionInput);

        // Assert
        verify(transactionInput).getTransactions();
        verify(transactionInput).setTransactions(isA(List.class));
        verify(transactionRepositoryPort).saveTransaction(isA(TransactionDTO.class));
        List<TransactionDTO> transactions = actualGeneratePaymentsResult.getTransactions();
        assertEquals(1, transactions.size());
        assertSame(transactionDTO, transactions.get(0));
    }

    /**
     * Test {@link PaymentService#getPayments()}.
     * <ul>
     *   <li>Then return Transactions Empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link PaymentService#getPayments()}
     */
    @Test
    @DisplayName("Get payments successfully")
    void testGetPayments() {

        // Arrange
        when(transactionRepositoryPort.findAllTransaction()).thenReturn(List.of(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW,
                new ArrayList<>())));

        // Act
        TransactionOutput actualPayments = paymentService.getPayments();

        // Assert
        verify(transactionRepositoryPort).findAllTransaction();
        assertEquals(actualPayments.getTransactions().size(), 1);
    }

    @Test
    @DisplayName("Get payments with exception")
    void testGetPaymentsFailure() {
        // Arrange
        when(transactionRepositoryPort.findAllTransaction()).thenThrow(new PaymentException("An error occurred"));

        // Act and Assert
        assertThrows(PaymentException.class, () -> paymentService.getPayments());
        verify(transactionRepositoryPort).findAllTransaction();
    }

    /**
     * Test {@link PaymentService#getPayment(String)}.
     * <ul>
     *   <li>Then return Transactions size is one.</li>
     * </ul>
     * <p>
     * Method under test: {@link PaymentService#getPayment(String)}
     */
    @Test
    @DisplayName("Get payment by id successfully")
    void getPaymentById() {
        // Arrange
        TransactionDTO transactionDTO = new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW,
                new ArrayList<>());

        when(transactionRepositoryPort.findTransactionById(Mockito.<String>any())).thenReturn(transactionDTO);

        // Act
        TransactionOutput actualPayment = paymentService.getPayment("42");

        // Assert
        verify(transactionRepositoryPort).findTransactionById(eq("42"));
        List<TransactionDTO> transactions = actualPayment.getTransactions();
        assertEquals(1, transactions.size());
        assertSame(transactionDTO, transactions.get(0));
    }

    @Test
    @DisplayName("Get payment by id with exception")
    void getPaymentByIdFailure() {
        // Arrange
        when(transactionRepositoryPort.findTransactionById(Mockito.<String>any()))
                .thenThrow(new PaymentException("An error occurred"));

        // Act and Assert
        assertThrows(PaymentException.class, () -> paymentService.getPayment("42"));
    }

    @Test
    @DisplayName("update payment successfully")
    void updatePayment() {
        // Arrange
        TransactionDTO transactionDTO = new TransactionDTO("42", 20.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW,
                new ArrayList<>());

        when(transactionRepositoryPort.updateTransactionById(Mockito.<String>any(), Mockito.<TransactionDTO>any()))
                .thenReturn(transactionDTO);
        when(transactionRepositoryPort.findTransactionById(Mockito.<String>any()))
                .thenReturn(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));

        TransactionUpdateInput transactionUpdateInput = new TransactionUpdateInput();
        transactionUpdateInput
                .setTransaction(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));
        transactionUpdateInput.setTransactionId("42");

        // Act
        TransactionUpdateOutput actualUpdatePaymentResult = paymentService.updatePayment(transactionUpdateInput);

        // Assert
        verify(transactionRepositoryPort).findTransactionById(eq("42"));
        verify(transactionRepositoryPort).updateTransactionById(eq("42"), isA(TransactionDTO.class));
        assertSame(transactionDTO, actualUpdatePaymentResult.getTransactions());
    }

    @Test
    @DisplayName("Update payment with exception - port issue")
    void updatePaymentFailurePortIssue() {
        // Arrange
        when(transactionRepositoryPort.updateTransactionById(Mockito.<String>any(), Mockito.<TransactionDTO>any()))
                .thenThrow(new PaymentException("An error occurred"));
        when(transactionRepositoryPort.findTransactionById(Mockito.<String>any()))
                .thenReturn(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));

        TransactionUpdateInput transactionUpdateInput = new TransactionUpdateInput();
        transactionUpdateInput
                .setTransaction(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));
        transactionUpdateInput.setTransactionId("42");

        // Act and Assert
        assertThrows(PaymentException.class, () -> paymentService.updatePayment(transactionUpdateInput));
    }

    @Test
    @DisplayName("Update payment with exception - Captured and canceled status issue")
    void updatePaymentFailureCapturedAndCanceledStatusIssue() {
        // Arrange
        when(transactionRepositoryPort.findTransactionById(Mockito.<String>any())).thenReturn(
                new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.CAPTURED, new ArrayList<>()));

        TransactionUpdateInput transactionUpdateInput = new TransactionUpdateInput();
        transactionUpdateInput
                .setTransaction(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));
        transactionUpdateInput.setTransactionId("42");

        // Act and Assert
        PaymentException paymentException = assertThrows(PaymentException.class, () -> paymentService.updatePayment(transactionUpdateInput));
        Assertions.assertThat(paymentException).hasMessage(String.format("Captured and canceled Transactions cannot be changed." +
                " Current status:: %s", PaymentStatus.CAPTURED));
    }

    @Test
    @DisplayName("Update payment with exception - new status issue")
    void updatePaymentFailureNewStatusIssue() {
        // Arrange
        TransactionDTO transactionDTO = mock(TransactionDTO.class);
        when(transactionDTO.getPaymentType()).thenReturn(PaymentType.GIFT_CARD);
        when(transactionDTO.getPaymentStatus()).thenReturn(PaymentStatus.AUTHORIZED);
        when(transactionRepositoryPort.findTransactionById(Mockito.<String>any())).thenReturn(transactionDTO);

        TransactionUpdateInput transactionUpdateInput = new TransactionUpdateInput();
        transactionUpdateInput
                .setTransaction(new TransactionDTO("42", 10.0f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>()));
        transactionUpdateInput.setTransactionId("42");

        // Act and Assert
        PaymentException paymentException = assertThrows(PaymentException.class, () -> paymentService.updatePayment(transactionUpdateInput));
        Assertions.assertThat(paymentException).hasMessage("Only payment with new status can be changed.");
    }

}
