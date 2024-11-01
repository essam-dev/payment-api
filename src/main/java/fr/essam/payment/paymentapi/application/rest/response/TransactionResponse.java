package fr.essam.payment.paymentapi.application.rest.response;

import fr.essam.payment.paymentapi.domain.model.TransactionDTO;

import java.util.List;

public record TransactionResponse (List<TransactionDTO> transactions) { }
