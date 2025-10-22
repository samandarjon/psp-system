package live.akbarov.pspsystem.payment.service.impl;


import live.akbarov.pspsystem.payment.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InMemoryIdempotencyService implements IdempotencyService {
    private final Map<String, Boolean> usedKeys = new ConcurrentHashMap<>();

    public Mono<Boolean> checkAndRegister(UUID idempotencyKey, String merchantId) {
        //TODO: Key needs to be expired at some point
        var compositeKey = key(idempotencyKey, merchantId);
        Boolean existing = usedKeys.putIfAbsent(compositeKey, Boolean.TRUE);
        if (existing != null) {
            return Mono.error(new IllegalArgumentException("Duplicate request: Idempotency key already used"));
        }
        return Mono.just(Boolean.TRUE);
    }


    @Override
    public void remove(UUID idempotencyKey, String merchantId) {
        var compositeKey = key(idempotencyKey, merchantId);
        usedKeys.remove(compositeKey);
    }

    private String key(UUID idempotencyKey, String merchantId) {
        return merchantId + ":" + idempotencyKey.toString();
    }
}
