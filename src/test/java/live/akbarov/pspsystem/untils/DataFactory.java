package live.akbarov.pspsystem.untils;

import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.entity.Payment;
import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class DataFactory {

    public static PaymentRequest createValidPaymentRequest() {
        return new PaymentRequest(
                "4539578763621486", // Luhn ok, last digit 6 (even) => approved
                "01/27",
                "123",
                new BigDecimal("120.50"),
                "USD",
                "merchant-001",
                null
        );

    }

    public static PaymentRequest createEmptyPaymentRequest() {
        return new PaymentRequest();

    }


    public static PaymentResponse createPaymentResponse(Status status) {
        return new PaymentResponse(UUID.randomUUID(), status);
    }

    public static Payment pendingPayment(UUID id) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setStatus(Status.PENDING);
        return payment;
    }

    public static Payment updatePayment(UUID id, AcquirerResponse response) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setExternalId(response.referenceId());
        payment.setStatus(Status.valueOf(response.status().name()));
        return payment;
    }


    public static AcquirerResponse createAcquirerResponse(AcquirerResponse.Status status) {
        return new AcquirerResponse(status, "test", UUID.randomUUID());
    }

    public static PaymentRequest requestWithCard(String cardNumber) {
        var request = createValidPaymentRequest();
        request.setCardNumber(cardNumber);
        return request;
    }
}
