package live.akbarov.pspsystem.payment.repository;

import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.entity.Payment;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentRepository {

    Mono<Payment> save(Payment payment);

    Mono<Payment> findById(UUID id);

    Mono<Payment> updateStatusAndExternalId(UUID id, UUID externalId, Status status);
}
