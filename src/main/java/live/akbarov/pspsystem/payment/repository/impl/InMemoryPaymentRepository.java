package live.akbarov.pspsystem.payment.repository.impl;

import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.entity.Payment;
import live.akbarov.pspsystem.payment.repository.PaymentRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<UUID, Payment> transactions = new ConcurrentHashMap<>();

    @Override
    public Mono<Payment> save(Payment payment) {
        //TODO: check if from object if it's exist update data, else generate id and save it
        var id = UUID.randomUUID();
        payment.setId(id);
        transactions.put(id, payment);
        return Mono.just(payment);
    }

    @Override
    public Mono<Payment> findById(UUID id) {
        return Mono.justOrEmpty(transactions.get(id));
    }

    @Override
    public Mono<Payment> updateStatusAndExternalId(UUID id, UUID externalId, Status status) {
        return Mono.justOrEmpty(transactions.computeIfPresent(id, (_id, payment) -> {
            payment.setStatus(status);
            payment.setExternalId(externalId);
            return payment;
        }));
    }


}
