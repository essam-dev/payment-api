package fr.essam.payment.paymentapi.application.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private final String id;

    @NotBlank(message = "name field is missing")
    private String name;
    @NotBlank(message = "price field is missing")
    private float price;
    @NotBlank(message = "quantity field is missing")
    private int quantity;

}
