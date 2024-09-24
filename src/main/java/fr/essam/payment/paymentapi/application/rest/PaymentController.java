package fr.essam.payment.paymentapi.application.rest;

import fr.essam.payment.paymentapi.application.rest.mapper.TransactionRequestMapper;
import fr.essam.payment.paymentapi.application.rest.request.TransactionRequest;
import fr.essam.payment.paymentapi.application.rest.request.TransactionUpdateRequest;
import fr.essam.payment.paymentapi.application.rest.response.TransactionResponse;
import fr.essam.payment.paymentapi.domain.model.TransactionOutput;
import fr.essam.payment.paymentapi.domain.model.TransactionUpdateOutput;
import fr.essam.payment.paymentapi.domain.ports.PaymentServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServicePort paymentServicePort;
    private final TransactionRequestMapper transactionRequestMapper;

    @PostMapping()
    public ResponseEntity<TransactionResponse> createPayment(@RequestBody TransactionRequest transactionRequest) {
        log.info("createPayment with body: {}", transactionRequest);

        TransactionOutput transactionOutput = paymentServicePort
                .generatePayments(transactionRequestMapper.toTransactionInput(transactionRequest));

        return ResponseEntity.ok(new TransactionResponse(transactionOutput.getTransactions()));
    }

    @GetMapping()
    public ResponseEntity<TransactionResponse> getPayments() {
        log.info("get all payments");
        TransactionOutput transactionOutput = paymentServicePort.getPayments();

        return ResponseEntity.ok(new TransactionResponse(transactionOutput.getTransactions()));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<TransactionResponse> getPayment(@PathVariable String paymentId) {
        log.info("getPayment with id: {}", paymentId);
        TransactionOutput transactionOutput = paymentServicePort.getPayment(paymentId);

        return ResponseEntity.ok(new TransactionResponse(transactionOutput.getTransactions()));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<TransactionResponse> updatePayment(@PathVariable String paymentId, @RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        log.info("updatePayment with id: {} to status: {}", paymentId, transactionUpdateRequest);

        TransactionUpdateOutput transactionUpdateOutput = paymentServicePort
                .updatePayment(transactionRequestMapper.toTransactionUpdateInput(transactionUpdateRequest, paymentId));

        return ResponseEntity.ok(new TransactionResponse(List.of(transactionUpdateOutput.getTransactions())));
    }

}
