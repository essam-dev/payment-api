package fr.essam.payment.paymentapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.essam.payment.paymentapi.application.rest.request.Item;
import fr.essam.payment.paymentapi.application.rest.request.Transaction;
import fr.essam.payment.paymentapi.application.rest.request.TransactionRequest;
import fr.essam.payment.paymentapi.application.rest.request.TransactionUpdateRequest;
import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import fr.essam.payment.paymentapi.domain.utils.PaymentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void validationBusinessRules() throws Exception {
		createFistTransaction();
		modifyFirstTransactionStatusAsAuthorized();
		modifyFirstTransactionStatusAsCaptured();

		createSecondTransaction();
		modifySecondTransactionStatusAsCanceled();

		retrieveAllTransactions();
	}

	private void createFistTransaction() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequest();
		Item tShirt = new Item(null, "T-shirt", 19.99f, 5);
		Transaction transaction = new Transaction(null, 99.95f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, List.of(tShirt));
		transactionRequest.setTransactions(List.of(transaction));

		this.mockMvc.perform(
						post("/v1/payments")
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(transactionRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactions[0].paymentType").value("CREDIT_CARD"))
				.andExpect(jsonPath("$.transactions[0].paymentStatus").value("NEW"))
				.andExpect(jsonPath("$.transactions[0].totalAmount").value("99.95"))
				.andExpect(jsonPath("$.transactions[0].items[0].name").value("T-shirt"))
				.andExpect(jsonPath("$.transactions[0].items[0].price").value("19.99"))
				.andExpect(jsonPath("$.transactions[0].items[0].quantity").value("5"));
	}

	private void modifyFirstTransactionStatusAsAuthorized() throws Exception {
		TransactionUpdateRequest transactionUpdateRequest = new TransactionUpdateRequest();
		Item tShirt = new Item(null, "T-shirt", 19.99f, 5);
		Transaction transaction = new Transaction(null, 99.95f, PaymentType.CREDIT_CARD, PaymentStatus.AUTHORIZED, List.of(tShirt));
		transactionUpdateRequest.setTransaction(transaction);

		this.mockMvc.perform(
						put("/v1/payments/{paymentId}", 1)
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(transactionUpdateRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactions[0].paymentType").value("CREDIT_CARD"))
				.andExpect(jsonPath("$.transactions[0].paymentStatus").value("AUTHORIZED"))
				.andExpect(jsonPath("$.transactions[0].totalAmount").value("99.95"))
				.andExpect(jsonPath("$.transactions[0].items[0].name").value("T-shirt"))
				.andExpect(jsonPath("$.transactions[0].items[0].price").value("19.99"))
				.andExpect(jsonPath("$.transactions[0].items[0].quantity").value("5"));
	}

	private void modifyFirstTransactionStatusAsCaptured() throws Exception {
		TransactionUpdateRequest transactionUpdateRequest = new TransactionUpdateRequest();
		Item tShirt = new Item(null, "T-shirt", 19.99f, 5);
		Transaction transaction = new Transaction(null, 99.95f, PaymentType.CREDIT_CARD, PaymentStatus.CAPTURED, List.of(tShirt));
		transactionUpdateRequest.setTransaction(transaction);

		this.mockMvc.perform(
						put("/v1/payments/{paymentId}", 1)
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(transactionUpdateRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactions[0].paymentType").value("CREDIT_CARD"))
				.andExpect(jsonPath("$.transactions[0].paymentStatus").value("CAPTURED"))
				.andExpect(jsonPath("$.transactions[0].totalAmount").value("99.95"))
				.andExpect(jsonPath("$.transactions[0].items[0].name").value("T-shirt"))
				.andExpect(jsonPath("$.transactions[0].items[0].price").value("19.99"))
				.andExpect(jsonPath("$.transactions[0].items[0].quantity").value("5"));
	}

	private void createSecondTransaction() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequest();
		Item bike = new Item(null, "bike", 208.00f, 1);
		Item shoes = new Item(null, "shoes", 30.00f, 1);
		Transaction transaction = new Transaction(null, 238.00f, PaymentType.PAYPAL, PaymentStatus.NEW, List.of(bike, shoes));
		transactionRequest.setTransactions(List.of(transaction));

		this.mockMvc.perform(
						post("/v1/payments")
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(transactionRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactions[0].paymentType").value("PAYPAL"))
				.andExpect(jsonPath("$.transactions[0].paymentStatus").value("NEW"))
				.andExpect(jsonPath("$.transactions[0].totalAmount").value("238.0"))
				.andExpect(jsonPath("$.transactions[0].items", hasSize(2)));
	}

	private void modifySecondTransactionStatusAsCanceled() throws Exception {
		TransactionUpdateRequest transactionUpdateRequest = new TransactionUpdateRequest();
		Item bike = new Item(null, "bike", 208.00f, 1);
		Item shoes = new Item(null, "shoes", 30.00f, 1);
		Transaction transaction = new Transaction(null, 238.00f, PaymentType.PAYPAL, PaymentStatus.NEW, List.of(bike, shoes));
		transactionUpdateRequest.setTransaction(transaction);

		this.mockMvc.perform(
						put("/v1/payments/{paymentId}", 2)
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(transactionUpdateRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactions[0].paymentType").value("PAYPAL"))
				.andExpect(jsonPath("$.transactions[0].paymentStatus").value("NEW"))
				.andExpect(jsonPath("$.transactions[0].totalAmount").value("238.0"))
				.andExpect(jsonPath("$.transactions[0].items", hasSize(2)));
	}

	private void retrieveAllTransactions() throws Exception {
		this.mockMvc.perform(
						get("/v1/payments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactions", hasSize(2)));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
