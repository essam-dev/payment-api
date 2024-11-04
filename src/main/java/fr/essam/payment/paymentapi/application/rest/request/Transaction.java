package fr.essam.payment.paymentapi.application.rest.request;

import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import fr.essam.payment.paymentapi.domain.utils.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record Transaction (String id,
                           @NotNull(message = "totalAmount field is missing") float totalAmount,
                           @NotNull(message = "paymentType field is missing") PaymentType paymentType,
                           @NotNull(message = "paymentStatus field is missing") PaymentStatus paymentStatus,
                           @NotNull(message = "items field is missing") List<Item> items) { }
