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
class PaymentServiceImpl implements PaymentService {
    private final RoutingStrategy routingStrategy;
    private final AcquirerRegistry acquirerRegistry;
    private final PaymentRepository paymentRepository;

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

    @Override
    public Mono<PaymentResponse> getPayment(UUID id) {
        return paymentRepository.findById(id)
                .map(PaymentMapper.INSTANCE::toPaymentResponse);
    }

    private Mono<Payment> updateTransaction(Context context) {
        log.info("Transaction status updated: txId={}, acquirer={}, acquirerResponse={}",
                context.transactionId(), context.acquirer(), context.acquirerResponse());
        //TODO: Add more details about transaction. Alternatively track all actions and push it some event bus.
        return paymentRepository.updateStatusAndExternalId(context.transactionId(),
                context.acquirerResponse().referenceId(),
                PaymentMapper.INSTANCE.toStatus(context.acquirerResponse().status()));
    }

    private Mono<Context> acquirerPayment(Context context) {
        log.info("Acquiring payment: txId={}, acquirer={}", context.transactionId(), context.acquirer());
        return acquirerRegistry.get(context.acquirer())
                .authorize(context.request())
                .map(context::withAcquirerResponse);
    }

    private Mono<Context> findAcquirer(Context context) {
        //We can filter our acquirer based health check or some admin config
        List<String> candidates = acquirerRegistry.allNames();
        log.info("Finding acquirer: txId={}, candidates={}", context.transactionId(), candidates);
        return routingStrategy.route(context.request(), acquirerRegistry.allNames())
                .map(context::withAcquirer);
    }

    @With
    public record Context(UUID transactionId, PaymentRequest request,
                          String acquirer, AcquirerResponse acquirerResponse) {
        public static Context of(UUID transactionId, PaymentRequest request) {
            return new Context(transactionId, request, null, null);
        }
    }
}
