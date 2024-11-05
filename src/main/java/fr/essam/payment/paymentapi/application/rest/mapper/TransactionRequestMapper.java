package fr.essam.payment.paymentapi.application.rest.mapper;

import fr.essam.payment.paymentapi.application.rest.request.Item;
import fr.essam.payment.paymentapi.application.rest.request.Transaction;
import fr.essam.payment.paymentapi.application.rest.request.TransactionRequest;
import fr.essam.payment.paymentapi.application.rest.request.TransactionUpdateRequest;
import fr.essam.payment.paymentapi.domain.model.TransactionDTO;
import fr.essam.payment.paymentapi.domain.model.TransactionInput;
import fr.essam.payment.paymentapi.domain.model.TransactionUpdateInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionRequestMapper {

    TransactionInput toTransactionInput(TransactionRequest transactionRequest);

    @Mapping(target = "totalAmount", source = "items", qualifiedByName = "totalAmount")
    TransactionDTO toTransactionDTO(Transaction transaction);

    @Mapping(target = "transactionId", source = "paymentId")
    TransactionUpdateInput toTransactionUpdateInput(TransactionUpdateRequest transactionUpdateRequest, String paymentId);

    @Named("totalAmount")
    default float totalAmount(List<Item> items) {
        return items.stream().map(item -> item.price() * item.quantity()).reduce(0.0f, Float::sum);
    }
}
