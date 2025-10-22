package live.akbarov.pspsystem.payment.model;

import live.akbarov.pspsystem.payment.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String transactionId;
    private Status status;
    //add more fields....
}
