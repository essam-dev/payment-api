package fr.essam.payment.paymentapi.infrastructure.mapper;

import fr.essam.payment.paymentapi.domain.model.ItemDTO;
import fr.essam.payment.paymentapi.infrastructure.model.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toEntity(ItemDTO dto);
    ItemDTO toDto(Item entity);

    List<Item> toEntityList(List<ItemDTO> dtos);
}
