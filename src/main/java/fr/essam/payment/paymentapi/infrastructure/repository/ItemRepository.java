package fr.essam.payment.paymentapi.infrastructure.repository;

import fr.essam.payment.paymentapi.infrastructure.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
}
