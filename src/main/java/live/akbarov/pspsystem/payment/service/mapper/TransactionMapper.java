package live.akbarov.pspsystem.payment.service.mapper;


import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.entity.Status;
import live.akbarov.pspsystem.payment.entity.Transaction;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.payment.model.PaymentResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionMapper {
    public static final TransactionMapper INSTANCE = new TransactionMapper();

    public Transaction toTransaction(PaymentRequest request) {
        return Transaction.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .cardNumber(request.getCardNumber())
                .expiryDate(request.getExpiryDate())
                .cvv(request.getCvv())
                .status(Status.PENDING)
                .merchantId(request.getMerchantId())
                .build();
    }

    public PaymentResponse toPaymentResponse(Transaction transaction) {
        return null;
    }

    public Status toStatus(AcquirerResponse.Status status) {
        return switch (status) {
            case DENIED -> Status.DENIED;
            case APPROVED -> Status.APPROVED;
        };
    }
}
