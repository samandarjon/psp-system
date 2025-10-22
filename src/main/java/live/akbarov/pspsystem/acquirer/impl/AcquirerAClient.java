package live.akbarov.pspsystem.acquirer.impl;

import live.akbarov.pspsystem.acquirer.AcquirerClient;
import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AcquirerAClient implements AcquirerClient {

    @Override
    public String name() {
        return "AcquirerA";
    }

    @Override
    public Mono<AcquirerResponse> authorize(PaymentRequest request) {
        return Mono.empty();
    }
}
