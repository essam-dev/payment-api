package fr.essam.payment.paymentapi.application.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.essam.payment.paymentapi.domain.model.TransactionDTO;

import java.util.List;

public record TransactionResponse (@JsonProperty("transactions") List<TransactionDTO> transactions) { }
