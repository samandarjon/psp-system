package live.akbarov.pspsystem.payment.service.impl;

import live.akbarov.pspsystem.acquirer.AcquirerRegistry;
import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.entity.Payment;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import live.akbarov.pspsystem.payment.repository.PaymentRepository;
import live.akbarov.pspsystem.payment.service.PaymentService;
import live.akbarov.pspsystem.payment.service.mapper.PaymentMapper;
import live.akbarov.pspsystem.routing.RoutingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final RoutingStrategy routingStrategy;
    private final AcquirerRegistry acquirerRegistry;
    private final PaymentRepository paymentRepository;

    /**
     * {@inheritDoc}
     * <p>
     * This implementation follows these steps:
     * <li>Save the payment request as a transaction with PENDING status</li>
     * <li>Create a context with the transaction ID and request</li>
     * <li>Find the appropriate acquirer using the routing strategy</li>
     * <li>Send the payment to the acquirer</li>
     * <li>Update the transaction with the acquirer's response</li>
     * <li>Map the updated transaction to a payment response</li>
     */
    @Override
    public Mono<PaymentResponse> pay(PaymentRequest request) {
        return paymentRepository.save(PaymentMapper.INSTANCE.toTransaction(request))
                .doOnNext(payment -> log.info("Transaction created: {}", payment))
                .map(payment -> Context.of(payment.getId(), request))
                .flatMap(this::findAcquirer)
                .flatMap(this::acquirerPayment)
                .flatMap(this::updateTransaction)
                .map(PaymentMapper.INSTANCE::toPaymentResponse);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation retrieves the payment transaction from the repository
     * and maps it to a payment response.
     */
    @Override
    public Mono<PaymentResponse> getPayment(UUID id) {
        return paymentRepository.findById(id)
                .map(PaymentMapper.INSTANCE::toPaymentResponse);
    }

    /**
     * Updates the transaction with the acquirer's response.
     *
     * @param context the context containing the transaction ID, request, acquirer, and acquirer response
     * @return a Mono that emits the updated payment transaction
     */
    private Mono<Payment> updateTransaction(Context context) {
        log.info("Transaction status updated: txId={}, acquirer={}, acquirerResponse={}",
                context.transactionId(), context.acquirer(), context.acquirerResponse());
        //TODO: Add more details about transaction. Alternatively track all actions and push it some event bus.
        return paymentRepository.updateStatusAndExternalId(context.transactionId(),
                context.acquirerResponse().referenceId(),
                PaymentMapper.INSTANCE.toStatus(context.acquirerResponse().status()));
    }

    /**
     * Sends the payment to the acquirer and updates the context with the acquirer's response.
     *
     * @param context the context containing the transaction ID, request, and acquirer
     * @return a Mono that emits the updated context with the acquirer's response
     */
    private Mono<Context> acquirerPayment(Context context) {
        log.info("Acquiring payment: txId={}, acquirer={}", context.transactionId(), context.acquirer());
        return acquirerRegistry.get(context.acquirer())
                .authorize(context.request())
                .map(context::withAcquirerResponse);
    }

    /**
     * Finds the appropriate acquirer for the payment using the routing strategy.
     *
     * @param context the context containing the transaction ID and request
     * @return a Mono that emits the updated context with the selected acquirer
     */
    private Mono<Context> findAcquirer(Context context) {
        //We can filter our acquirer based health check or some admin config
        List<String> candidates = acquirerRegistry.allNames();
        log.info("Finding acquirer: txId={}, candidates={}", context.transactionId(), candidates);
        return routingStrategy.route(context.request(), candidates).map(context::withAcquirer);
    }

    @With
    public record Context(UUID transactionId, PaymentRequest request,
                          String acquirer, AcquirerResponse acquirerResponse) {
        public static Context of(UUID transactionId, PaymentRequest request) {
            return new Context(transactionId, request, null, null);
        }
    }
}
