package live.akbarov.pspsystem.payment.service.impl;

import live.akbarov.pspsystem.acquirer.AcquirerRegistry;
import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.entity.Transaction;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import live.akbarov.pspsystem.payment.repository.TransactionRepository;
import live.akbarov.pspsystem.payment.service.PaymentService;
import live.akbarov.pspsystem.payment.service.mapper.TransactionMapper;
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
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<PaymentResponse> pay(PaymentRequest request) {
        return transactionRepository.save(TransactionMapper.INSTANCE.toTransaction(request))
                .doOnNext(transaction -> log.info("Transaction created: {}", transaction))
                .map(transaction -> Context.of(transaction.getId(), request))
                .flatMap(this::findAcquirer)
                .flatMap(this::acquirerPayment)
                .flatMap(this::updateTransaction)
                .map(TransactionMapper.INSTANCE::toPaymentResponse);
    }

    private Mono<Transaction> updateTransaction(Context context) {
        log.info("Transaction status updated: txId={}, acquirer={}, acquirerResponse={}",
                context.transactionId(), context.acquirer(), context.acquirerResponse());
        //TODO: Add more details about transaction. Alternatively track all actions and pushed it some event bus.
        return transactionRepository.updateStatusAndExternalId(context.transactionId(),
                context.acquirerResponse().referenceId(),
                TransactionMapper.INSTANCE.toStatus(context.acquirerResponse().status()));
    }

    private Mono<Context> acquirerPayment(Context context) {
        log.info("Acquiring payment: txId={}, acquirer={}", context.transactionId(), context.acquirer());
        return acquirerRegistry.get(context.acquirer())
                .authorize(context.request())
                .map(context::withAcquirerResponse);
    }

    private Mono<Context> findAcquirer(Context context) {
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
