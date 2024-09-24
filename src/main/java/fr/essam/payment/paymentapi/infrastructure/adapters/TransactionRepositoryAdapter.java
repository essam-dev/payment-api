package fr.essam.payment.paymentapi.infrastructure.adapters;

import fr.essam.payment.paymentapi.domain.model.ItemDTO;
import fr.essam.payment.paymentapi.domain.model.TransactionDTO;
import fr.essam.payment.paymentapi.domain.ports.TransactionRepositoryPort;
import fr.essam.payment.paymentapi.infrastructure.mapper.ItemMapper;
import fr.essam.payment.paymentapi.infrastructure.mapper.TransactionMapper;
import fr.essam.payment.paymentapi.infrastructure.model.Transaction;
import fr.essam.payment.paymentapi.infrastructure.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ItemMapper itemMapper;

    @Override
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        log.debug("saveTransaction with transactionDTO: {}", transactionDTO);

        return transactionMapper.toDto(transactionRepository.save(transactionMapper.toEntity(transactionDTO)));
    }

    @Override
    public List<TransactionDTO> findAllTransaction() {
        log.debug("findAllTransaction");
        List<TransactionDTO> transactions = new ArrayList<>();

        transactionRepository.findAll().forEach(transaction -> transactions.add(transactionMapper.toDto(transaction)));

        return transactions;

    }

    @Override
    public TransactionDTO findTransactionById(String id) {
        log.debug("findTransactionById with id: {}", id);
        return transactionRepository.findById(Long.valueOf(id)).map(transactionMapper::toDto).orElseGet(() -> {
            log.info("Transaction not found with id: {}", id);
            return null;
        });
    }

    @Override
    public TransactionDTO updateTransactionById(String id, TransactionDTO transactionDTO) {
        log.debug("updateTransactionStatusById with id: {} to status: {}", id, transactionDTO.getPaymentStatus());

        return transactionRepository.findById(Long.valueOf(id)).map(transaction -> {
            updateCommonTransaction(transactionDTO, transaction);
            removeUnwantedProducts(transactionDTO, transaction);
            updateAndAddProducts(transactionDTO, transaction);

            return transactionMapper.toDto(transactionRepository.save(transaction));
        }).orElseGet(() -> {
            log.info("Transaction not found with id: {}", id);
            return null;
        });
    }

    private static void updateCommonTransaction(TransactionDTO transactionDTO, Transaction transaction) {
        transaction.setPaymentType(transactionDTO.getPaymentType());
        transaction.setTotalAmount(BigDecimal.valueOf( transactionDTO.getTotalAmount()));
        transaction.setPaymentStatus(transactionDTO.getPaymentStatus());
    }

    private void updateAndAddProducts(TransactionDTO transactionDTO, Transaction transaction) {
        List<ItemDTO> itemsToAdd = new ArrayList<>();

        transactionDTO.getItems()
                        .forEach(itemDTO -> transaction.getItems()
                                .stream()
                                .filter(item -> StringUtils.compare(item.getName(), itemDTO.getName()) == 0)
                                .findFirst().
                                ifPresentOrElse(item -> {
                                    item.setName(itemDTO.getName());
                                    item.setPrice(BigDecimal.valueOf(itemDTO.getPrice()));
                                    item.setQuantity(itemDTO.getQuantity());
                                    }, () -> itemsToAdd.add(itemDTO)));

        transaction.getItems().addAll(itemMapper.toEntityList(itemsToAdd));
    }

    private static void removeUnwantedProducts(TransactionDTO transactionDTO, Transaction transaction) {
        transaction.getItems()
                .removeIf(item -> transactionDTO.getItems()
                        .stream().noneMatch(itemDTO -> StringUtils.compare(item.getName(), itemDTO.getName()) == 0));
    }
}
