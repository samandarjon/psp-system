package live.akbarov.pspsystem.until.payment.service;

import live.akbarov.pspsystem.acquirer.AcquirerClient;
import live.akbarov.pspsystem.acquirer.AcquirerRegistry;
import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.repository.PaymentRepository;
import live.akbarov.pspsystem.payment.service.impl.PaymentServiceImpl;
import live.akbarov.pspsystem.routing.RoutingStrategy;
import live.akbarov.pspsystem.untils.DataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private RoutingStrategy routingStrategy;
    @Mock
    private AcquirerRegistry acquirerRegistry;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private AcquirerClient acquirerClient;

    private PaymentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PaymentServiceImpl(routingStrategy, acquirerRegistry, paymentRepository);
    }


    @Test
    void should_completePayment() {
        var request = DataFactory.createValidPaymentRequest();
        var transactionId = UUID.randomUUID();
        var acquirerId = UUID.randomUUID().toString();
        var candidates = List.of(acquirerId);
        var acquirerResponse = DataFactory.createAcquirerResponse(AcquirerResponse.Status.APPROVED);
        when(paymentRepository.save(any())).thenReturn(Mono.just(DataFactory.pendingPayment(transactionId)));
        when(acquirerRegistry.get(acquirerId)).thenReturn(acquirerClient);
        when(acquirerRegistry.allNames()).thenReturn(candidates);
        when(routingStrategy.route(request, candidates)).thenReturn(Mono.just(acquirerId));
        when(acquirerClient.authorize(request)).thenReturn(Mono.just(acquirerResponse));
        when(paymentRepository.updateStatusAndExternalId(
                transactionId, acquirerResponse.referenceId(),
                Status.valueOf(acquirerResponse.status().name())))
                .thenReturn(Mono.just(DataFactory.updatePayment(transactionId, acquirerResponse)));

        StepVerifier.create(service.pay(request))
                .assertNext(next -> {
                    assertThat(next.getTransactionId()).isEqualTo(transactionId);
                    assertThat(next.getStatus()).isEqualTo(Status.APPROVED);
                })
                .verifyComplete();

        verify(paymentRepository, times(1)).save(any());
        verify(acquirerClient, times(1)).authorize(request);
        verify(acquirerRegistry, times(1)).allNames();
        verify(acquirerRegistry, times(1)).get(acquirerId);
        verify(routingStrategy).route(request, candidates);
        verify(acquirerClient).authorize(request);
        verify(paymentRepository, times(1)).updateStatusAndExternalId(
                transactionId, acquirerResponse.referenceId(), Status.APPROVED
        );
    }

}
