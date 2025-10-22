package live.akbarov.pspsystem.payment.service.impl;


import live.akbarov.pspsystem.common.exception.AppException;
import live.akbarov.pspsystem.common.utils.CardValidation;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import live.akbarov.pspsystem.payment.service.IdempotencyService;
import live.akbarov.pspsystem.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class PrePaymentServiceImpl implements PaymentService {
    private final IdempotencyService idempotencyService;
    private final PaymentServiceImpl paymentService;

    @Override
    public Mono<PaymentResponse> pay(PaymentRequest request) {
        //Pre-payment validation written here.
        return validate(request)
                .delayUntil(req ->
                        idempotencyService.checkAndRegister(req.getIdempotencyKey(), req.getMerchantId()))
                .flatMap(paymentService::pay)
                .doOnError(err -> {
                    log.error("Payment failed", err);
                    idempotencyService.remove(request.getIdempotencyKey(), request.getMerchantId());
                })
                .doOnSuccess(res ->
                        idempotencyService.remove(request.getIdempotencyKey(), request.getMerchantId())
                );
    }

    @Override
    public Mono<PaymentResponse> getPayment(UUID id) {
        return paymentService.getPayment(id);
    }

    private Mono<PaymentRequest> validate(PaymentRequest request) {
        if (!CardValidation.isValidCardNumber(request.getCardNumber())) {
            return Mono.error(new AppException("ALERT_EXCEPTION", "Invalid card number", HttpStatus.BAD_REQUEST));
        }
        //Add additional validation flows
        return Mono.just(request);
    }
}
