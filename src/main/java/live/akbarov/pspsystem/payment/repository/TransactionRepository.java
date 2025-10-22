package live.akbarov.pspsystem.payment.repository;

import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.entity.Transaction;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TransactionRepository {

    Mono<Transaction> save(Transaction transaction);

    Mono<Transaction> findById(UUID id);

    Mono<Transaction> updateStatusAndExternalId(UUID id, UUID externalId, Status status);
}
