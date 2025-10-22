package live.akbarov.pspsystem.payment.controller;

import jakarta.validation.Valid;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import live.akbarov.pspsystem.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Creates a new payment transaction.
     * <p>
     * This endpoint accepts payment details and processes the payment through
     * the appropriate acquirer based on routing rules.
     *
     * @param request the payment request containing card details, amount, currency, and merchant ID
     * @return a Mono that emits the payment response with transaction ID and status
     */
    @PostMapping
    public Mono<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    /**
     * Retrieves a payment transaction by its ID.
     *
     * @param id the unique identifier of the payment transaction
     * @return a Mono that emits the payment response with transaction details
     */
    @GetMapping("/{id}")
    public Mono<PaymentResponse> getPayment(@PathVariable UUID id) {
        return paymentService.getPayment(id);
    }
}
