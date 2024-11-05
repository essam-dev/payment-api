package fr.essam.payment.paymentapi.application.rest;

import fr.essam.payment.paymentapi.application.rest.mapper.TransactionRequestMapper;
import fr.essam.payment.paymentapi.application.rest.request.TransactionRequest;
import fr.essam.payment.paymentapi.application.rest.request.TransactionUpdateRequest;
import fr.essam.payment.paymentapi.application.rest.response.TransactionResponse;
import fr.essam.payment.paymentapi.application.rest.utils.BasicAuthValidator;
import fr.essam.payment.paymentapi.domain.model.TransactionOutput;
import fr.essam.payment.paymentapi.domain.model.TransactionUpdateOutput;
import fr.essam.payment.paymentapi.domain.ports.PaymentServicePort;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fr.essam.payment.paymentapi.application.rest.utils.PaymentResources.API_ENDPOINT;
import static fr.essam.payment.paymentapi.application.rest.utils.PaymentResources.API_VERSION_TITLE;

@Slf4j
@RestController
@RequestMapping(API_ENDPOINT)
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(
                title = "Payment API",
                version = API_VERSION_TITLE,
                description = "Api documentation"
        )
)
public class PaymentController {

    private final AuthenticationManager authenticationManager;
    private final PaymentServicePort paymentServicePort;
    private final TransactionRequestMapper transactionRequestMapper;


    @PostMapping()
    public ResponseEntity<TransactionResponse> createPayment(@RequestBody TransactionRequest transactionRequest,
                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        log.info("createPayment with body: {}", transactionRequest);

        BasicAuthValidator.validateBasicAuth(authorization, authenticationManager);

        TransactionOutput transactionOutput = paymentServicePort
                .generatePayments(transactionRequestMapper.toTransactionInput(transactionRequest));

        return ResponseEntity.ok(new TransactionResponse(transactionOutput.getTransactions()));
    }

    @GetMapping()
    public ResponseEntity<TransactionResponse> getPayments(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        log.info("get all payments");

        BasicAuthValidator.validateBasicAuth(authorization, authenticationManager);

        TransactionOutput transactionOutput = paymentServicePort.getPayments();

        return ResponseEntity.ok(new TransactionResponse(transactionOutput.getTransactions()));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<TransactionResponse> getPayment(@PathVariable String paymentId,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        log.info("getPayment with id: {}", paymentId);

        BasicAuthValidator.validateBasicAuth(authorization, authenticationManager);

        TransactionOutput transactionOutput = paymentServicePort.getPayment(paymentId);

        return ResponseEntity.ok(new TransactionResponse(transactionOutput.getTransactions()));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<TransactionResponse> updatePayment(@PathVariable String paymentId, @RequestBody TransactionUpdateRequest transactionUpdateRequest,
                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        log.info("updatePayment with id: {} to status: {}", paymentId, transactionUpdateRequest);

        BasicAuthValidator.validateBasicAuth(authorization, authenticationManager);

        TransactionUpdateOutput transactionUpdateOutput = paymentServicePort
                .updatePayment(transactionRequestMapper.toTransactionUpdateInput(transactionUpdateRequest, paymentId));

        return ResponseEntity.ok(new TransactionResponse(List.of(transactionUpdateOutput.getTransactions())));
    }

}
