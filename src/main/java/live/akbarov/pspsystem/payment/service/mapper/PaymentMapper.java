package live.akbarov.pspsystem.payment.service.mapper;


import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.entity.Payment;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMapper {
    public static final PaymentMapper INSTANCE = new PaymentMapper();

    public Payment toTransaction(PaymentRequest request) {
        return Payment.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .cardNumber(request.getCardNumber())
                .expiryDate(request.getExpiryDate())
                .cvv(request.getCvv())
                .status(Status.PENDING)
                .merchantId(request.getMerchantId())
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getStatus());
    }

    public Status toStatus(AcquirerResponse.Status status) {
        return switch (status) {
            case DENIED -> Status.DENIED;
            case APPROVED -> Status.APPROVED;
        };
    }
}
