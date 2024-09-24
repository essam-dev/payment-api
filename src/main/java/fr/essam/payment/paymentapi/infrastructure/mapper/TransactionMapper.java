package fr.essam.payment.paymentapi.infrastructure.mapper;

import fr.essam.payment.paymentapi.domain.model.TransactionDTO;
import fr.essam.payment.paymentapi.infrastructure.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toEntity(TransactionDTO dto);
    TransactionDTO toDto(Transaction entity);
}
