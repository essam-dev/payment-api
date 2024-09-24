package fr.essam.payment.paymentapi.application.rest.mapper;

import fr.essam.payment.paymentapi.application.rest.request.TransactionRequest;
import fr.essam.payment.paymentapi.application.rest.request.TransactionUpdateRequest;
import fr.essam.payment.paymentapi.domain.model.TransactionInput;
import fr.essam.payment.paymentapi.domain.model.TransactionUpdateInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionRequestMapper {

    TransactionInput toTransactionInput(TransactionRequest transactionRequest);

    @Mapping(target = "transactionId", source = "paymentId")
    TransactionUpdateInput toTransactionUpdateInput(TransactionUpdateRequest transactionUpdateRequest, String paymentId);
}
