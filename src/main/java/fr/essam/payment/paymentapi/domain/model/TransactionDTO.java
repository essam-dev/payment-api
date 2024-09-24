package fr.essam.payment.paymentapi.domain.model;

import fr.essam.payment.paymentapi.domain.utils.PaymentStatus;
import fr.essam.payment.paymentapi.domain.utils.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    private final String id;
    private float totalAmount;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private List<ItemDTO> items;
}
