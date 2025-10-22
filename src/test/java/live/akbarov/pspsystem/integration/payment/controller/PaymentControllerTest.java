package live.akbarov.pspsystem.integration.payment.controller;

import live.akbarov.pspsystem.common.exception.AcquirerException;
import live.akbarov.pspsystem.common.exception.GlobalErrorHandler;
import live.akbarov.pspsystem.common.exception.IdempotencyException;
import live.akbarov.pspsystem.common.exception.RoutingException;
import live.akbarov.pspsystem.payment.controller.PaymentController;
import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.service.PaymentService;
import live.akbarov.pspsystem.untils.DataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebFluxTest(PaymentController.class)
@Import(GlobalErrorHandler.class)
class PaymentControllerTest {

    @Autowired
    private WebTestClient client;
    @MockitoBean
    private PaymentService paymentService;

    @Test
    void shouldReturnBadRequest_onBeanValidationErrors() {
        client.post()
                .uri("/api/v1/payments")
                .bodyValue(DataFactory.createEmptyPaymentRequest())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("VALIDATION_ERROR")
                .jsonPath("$.message").isEqualTo("Validation failed")
                .jsonPath("$.details.cardNumber").exists()
                .jsonPath("$.details.expiryDate").exists()
                .jsonPath("$.details.cvv").exists()
                .jsonPath("$.details.amount").exists()
                .jsonPath("$.details.currency").exists()
                .jsonPath("$.details.merchantId").exists();

        verifyNoInteractions(paymentService);
    }

    @Test
    void shouldReturnApproved_whenServiceApproves() {
        var response = DataFactory.createPaymentResponse(Status.APPROVED);
        when(paymentService.pay(any(PaymentRequest.class))).thenReturn(Mono.just(response));

        var request = DataFactory.createValidPaymentRequest();
        client.post()
                .uri("/api/v1/payments")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.transactionId").isEqualTo(response.getTransactionId())
                .jsonPath("$.status").isEqualTo("APPROVED");

        verify(paymentService, times(1)).pay(any(PaymentRequest.class));
    }

    @Test
    void shouldReturnDenied_whenServiceDenies() {
        var response = DataFactory.createPaymentResponse(Status.DENIED);
        when(paymentService.pay(any(PaymentRequest.class))).thenReturn(Mono.just(response));

        var request = DataFactory.createValidPaymentRequest();
        client.post()
                .uri("/api/v1/payments")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.transactionId").isEqualTo(response.getTransactionId())
                .jsonPath("$.status").isEqualTo("DENIED");
    }


    @Test
    void shouldReturnConflict_onDuplicateIdempotencyKey() {
        when(paymentService.pay(any(PaymentRequest.class)))
                .thenReturn(Mono.error(new IdempotencyException("Idempotency key already used")));

        client.post()
                .uri("/api/v1/payments")
                .bodyValue(DataFactory.createValidPaymentRequest())
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.code").isEqualTo("IDEMPOTENCY_VIOLATION")
                .jsonPath("$.message").isEqualTo("Idempotency key already used");
    }

    @Test
    void shouldReturnUnprocessableEntity_onRoutingError() {
        when(paymentService.pay(any(PaymentRequest.class)))
                .thenReturn(Mono.error(new RoutingException("Desired acquirer not available")));

        client.post()
                .uri("/api/v1/payments")
                .bodyValue(DataFactory.createValidPaymentRequest())
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectBody()
                .jsonPath("$.code").isEqualTo("ROUTING_ERROR")
                .jsonPath("$.message").isEqualTo("Desired acquirer not available");
    }

    @Test
    void shouldReturnBadGateway_onAcquirerFailure() {
        when(paymentService.pay(any(PaymentRequest.class)))
                .thenReturn(Mono.error(new AcquirerException("Acquirer is not exist")));

        client.post()
                .uri("/api/v1/payments")
                .bodyValue(DataFactory.createValidPaymentRequest())
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectBody()
                .jsonPath("$.code").isEqualTo("ACQUIRER_ERROR")
                .jsonPath("$.message").exists();
    }
}
