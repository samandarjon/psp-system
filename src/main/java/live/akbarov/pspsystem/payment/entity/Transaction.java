package live.akbarov.pspsystem.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID id;

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    private BigDecimal amount;
    private String currency;

    private String merchantId;

    private UUID externalId;
    private Status status;
}
