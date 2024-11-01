package fr.essam.payment.paymentapi.application.rest.request;

import jakarta.validation.constraints.NotBlank;

public record Item (String id,
                    @NotBlank(message = "name field is missing") String name,
                    @NotBlank(message = "price field is missing") float price,
                    @NotBlank(message = "quantity field is missing") int quantity) { }