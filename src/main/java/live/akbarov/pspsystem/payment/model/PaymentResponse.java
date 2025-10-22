package live.akbarov.pspsystem.payment.model;

import live.akbarov.pspsystem.payment.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private UUID transactionId;
    private Status status;
    //add more fields....
}
