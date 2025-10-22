package live.akbarov.pspsystem.payment.service;

import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
    Mono<PaymentResponse> pay(PaymentRequest paymentRequest);

    Mono<PaymentResponse> getPayment(UUID id);
}
