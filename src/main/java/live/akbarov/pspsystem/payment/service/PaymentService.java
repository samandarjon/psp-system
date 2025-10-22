package live.akbarov.pspsystem.payment.service;

import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
    /**
     * Processes a payment request.
     * <p>
     * This method validates the payment request, routes it to the appropriate
     * acquirer based on routing rules, and updates the transaction status
     * based on the acquirer's response.
     *
     * @param paymentRequest the payment request containing card details, amount, currency, and merchant ID
     * @return a Mono that emits the payment response with transaction ID and status
     */
    Mono<PaymentResponse>   pay(PaymentRequest paymentRequest);

    /**
     * Retrieves a payment transaction by its ID.
     *
     * @param id the unique identifier of the payment transaction
     * @return a Mono that emits the payment response with transaction details,
     * or an empty Mono if no transaction with the given ID exists
     */
    Mono<PaymentResponse> getPayment(UUID id);
}
