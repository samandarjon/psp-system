package live.akbarov.pspsystem.acquirer;

import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import reactor.core.publisher.Mono;

public interface AcquirerClient {
    String name();

    Mono<AcquirerResponse> authorize(PaymentRequest request);
}
