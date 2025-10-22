package live.akbarov.pspsystem.payment.repository.impl;

import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.entity.Transaction;
import live.akbarov.pspsystem.payment.repository.TransactionRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Mono<Transaction> save(Transaction transaction) {
        //TODO: check if from object if it's exist update data, else generate id and save it
        var id = UUID.randomUUID();
        transaction.setId(id);
        transactions.put(id, transaction);
        return Mono.just(transaction);
    }

    @Override
    public Mono<Transaction> findById(UUID id) {
        return Mono.justOrEmpty(transactions.get(id));
    }

    @Override
    public Mono<Transaction> updateStatusAndExternalId(UUID id, UUID externalId, Status status) {
        return Mono.justOrEmpty(transactions.computeIfPresent(id, (_id, transaction) -> {
            transaction.setStatus(status);
            transaction.setExternalId(externalId);
            return transaction;
        }));
    }


}
