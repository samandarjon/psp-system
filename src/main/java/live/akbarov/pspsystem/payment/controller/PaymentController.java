package live.akbarov.pspsystem.payment.controller;

import jakarta.validation.Valid;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import live.akbarov.pspsystem.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public Mono<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }
}
