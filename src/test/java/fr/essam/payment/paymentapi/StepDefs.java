package fr.essam.payment.paymentapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.essam.payment.paymentapi.application.rest.request.Item;
import fr.essam.payment.paymentapi.application.rest.request.Transaction;
import fr.essam.payment.paymentapi.application.rest.request.TransactionRequest;
import fr.essam.payment.paymentapi.application.rest.request.TransactionUpdateRequest;
import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import fr.essam.payment.paymentapi.domain.utils.PaymentType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefs  extends SpringIntegrationTest{

    private MvcResult mvcResult;
    private static final String BASIC_AUTH_USERNAME = "buyer1";
    private static final String BASIC_AUTH_PASSWORD = "buyerpassword";

    @Given("I have a credit card payment")
    public void iHaveACreditCardPayment() throws Exception{
        Item tShirt = new Item(null, "T-shirt", 19.99f, 5);
        Transaction transaction = new Transaction(null, 99.95f, PaymentType.CREDIT_CARD, PaymentStatus.NEW, List.of(tShirt));
        TransactionRequest transactionRequest = new TransactionRequest(List.of(transaction));

        getMockMvc().perform(
                        post("/v1/payments")
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(transactionRequest)));
    }

    @When("I modify the payment status to authorized then to captured")
    public void iModifyThePaymentStatusToAuthorizedThenToCaptured() throws Exception {
        Item tShirt = new Item(null, "T-shirt", 19.99f, 5);
        Transaction transaction = new Transaction(null, 99.95f, PaymentType.CREDIT_CARD, PaymentStatus.AUTHORIZED, List.of(tShirt));
        TransactionUpdateRequest transactionUpdateRequest = new TransactionUpdateRequest(transaction);

        getMockMvc().perform(
                        put("/v1/payments/{paymentId}", 1)
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(transactionUpdateRequest)));

        tShirt = new Item(null, "T-shirt", 19.99f, 5);
        transaction = new Transaction(null, 99.95f, PaymentType.CREDIT_CARD, PaymentStatus.CAPTURED, List.of(tShirt));
        transactionUpdateRequest = new TransactionUpdateRequest(transaction);

        getMockMvc().perform(
                        put("/v1/payments/{paymentId}", 1)
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(transactionUpdateRequest)));
    }

    @Then("I should see the payment is captured")
    public void iShouldSeeThePaymentIsCaptured() throws Exception{
        getMockMvc().perform(
                        get("/v1/payments/{paymentId}", 1)
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", hasSize(1)))
                .andExpect(jsonPath("$.transactions[0].paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.transactions[0].paymentStatus").value("CAPTURED"))
                .andExpect(jsonPath("$.transactions[0].totalAmount").value("99.95"))
                .andExpect(jsonPath("$.transactions[0].items[0].name").value("T-shirt"))
                .andExpect(jsonPath("$.transactions[0].items[0].price").value("19.99"))
                .andExpect(jsonPath("$.transactions[0].items[0].quantity").value("5"));
    }

    @Given("I have a paypal payment")
    public void iHaveAPaypalPayment() throws Exception{
        Item bike = new Item(null, "bike", 208.00f, 1);
        Item shoes = new Item(null, "shoes", 30.00f, 1);
        Transaction transaction = new Transaction(null, 238.00f, PaymentType.PAYPAL, PaymentStatus.NEW, List.of(bike, shoes));
        TransactionRequest transactionRequest = new TransactionRequest(List.of(transaction));

        getMockMvc().perform(
                        post("/v1/payments")
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(transactionRequest)));
    }

    @When("I modify the payment status to canceled")
    public void iModifyThePaymentStatusToCanceled() throws Exception {
        Item bike = new Item(null, "bike", 208.00f, 1);
        Item shoes = new Item(null, "shoes", 30.00f, 1);
        Transaction transaction = new Transaction(null, 238.00f, PaymentType.PAYPAL, PaymentStatus.CANCELED, List.of(bike, shoes));
        TransactionUpdateRequest transactionUpdateRequest = new TransactionUpdateRequest(transaction);

        getMockMvc().perform(
                        put("/v1/payments/{paymentId}", 2)
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(transactionUpdateRequest)));
    }

    @Then("I should see the payment is canceled")
    public void iShouldSeeThePaymentIsCanceled() throws Exception{
        getMockMvc().perform(
                        get("/v1/payments/{paymentId}", 2)
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", hasSize(1)))
                .andExpect(jsonPath("$.transactions[0].paymentType").value("PAYPAL"))
                .andExpect(jsonPath("$.transactions[0].paymentStatus").value("CANCELED"))
                .andExpect(jsonPath("$.transactions[0].totalAmount").value("238.0"))
                .andExpect(jsonPath("$.transactions[0].items", hasSize(2)));
    }

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() throws Exception {
        mvcResult = getMockMvc().perform(
                        get("/v1/payments")
                                .with(httpBasic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD)))
                .andReturn();
    }

    @Then("I should see two transactions")
    public void iShouldSeeTwoTransactions() throws Exception {
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        JSONArray transactions = new JSONObject(mvcResult.getResponse().getContentAsString()).getJSONArray("transactions");

        assertThat(transactions.length()).isEqualTo(2);

        transactions.getJSONObject(0).getString("paymentType").equals("CREDIT_CARD");
        transactions.getJSONObject(0).getString("paymentStatus").equals("CAPTURED");

        transactions.getJSONObject(1).getString("paymentType").equals("PAYPAL");
        transactions.getJSONObject(1).getString("paymentStatus").equals("CANCELED");
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
