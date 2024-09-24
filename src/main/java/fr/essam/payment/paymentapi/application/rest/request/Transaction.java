package fr.essam.payment.paymentapi.application.rest.request;

import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import fr.essam.payment.paymentapi.domain.utils.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private final String id;

    @NotNull(message = "totalAmount field is missing")
    private float totalAmount;

    @NotNull(message = "paymentType field is missing")
    private PaymentType paymentType;

    @NotNull(message = "paymentStatus field is missing")
    private PaymentStatus paymentStatus;

    @NotNull(message = "items field is missing")
    private List<Item> items;
}
