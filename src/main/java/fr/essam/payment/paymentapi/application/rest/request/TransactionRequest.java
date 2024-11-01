package fr.essam.payment.paymentapi.application.rest.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TransactionRequest(@NotNull(message = "Transactions field is missing") List<Transaction> transactions) {
}
