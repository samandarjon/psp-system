package live.akbarov.pspsystem.payment.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IdempotencyService {
    Mono<Boolean> checkAndRegister(UUID idempotencyKey, String merchantId);

    void remove(UUID idempotencyKey, String merchantId);
}
