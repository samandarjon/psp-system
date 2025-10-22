package live.akbarov.pspsystem.until.payment.service;

import live.akbarov.pspsystem.common.exception.AppException;
import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.service.IdempotencyService;
import live.akbarov.pspsystem.payment.service.impl.PaymentServiceImpl;
import live.akbarov.pspsystem.payment.service.impl.PrePaymentServiceImpl;
import live.akbarov.pspsystem.untils.DataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrePaymentServiceImplTest {
    @Mock
    private IdempotencyService idempotencyService;
    @Mock
    private PaymentServiceImpl paymentService;
    private PrePaymentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PrePaymentServiceImpl(idempotencyService, paymentService);
    }


    @Test
    void should_completePaymentAndCleanIdempotency() {
        var request = DataFactory.createValidPaymentRequest();
        var response = DataFactory.createPaymentResponse(Status.DENIED);
        when(idempotencyService.checkAndRegister(request.getIdempotencyKey(), request.getMerchantId()))
                .thenReturn(Mono.just(true));

        doNothing().when(idempotencyService).remove(request.getIdempotencyKey(), request.getMerchantId());
        when(paymentService.pay(request)).thenReturn(Mono.just(response));

        StepVerifier.create(service.pay(request))
                .assertNext(next -> {
                    assertThat(next.getTransactionId()).isEqualTo(response.getTransactionId());
                    assertThat(next.getStatus()).isEqualTo(Status.DENIED);
                })
                .verifyComplete();

        verify(idempotencyService, times(1)).checkAndRegister(request.getIdempotencyKey(), request.getMerchantId());
        verify(paymentService, times(1)).pay(request);
        verify(idempotencyService, times(1)).remove(request.getIdempotencyKey(), request.getMerchantId());
    }


    @Test
    void should_ThrowExceptionWhenCardNumberIsInvalid() {
        var request = DataFactory.createEmptyPaymentRequest();
        doNothing().when(idempotencyService).remove(request.getIdempotencyKey(), request.getMerchantId());

        StepVerifier.create(service.pay(request))
                .expectError(AppException.class)
                .verify();

        verify(idempotencyService, times(0)).checkAndRegister(request.getIdempotencyKey(), request.getMerchantId());
        verify(paymentService, times(0)).pay(request);
        verify(idempotencyService, times(1)).remove(request.getIdempotencyKey(), request.getMerchantId());

    }

}
