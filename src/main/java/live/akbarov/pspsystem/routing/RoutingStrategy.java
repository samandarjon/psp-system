package live.akbarov.pspsystem.routing;

import live.akbarov.pspsystem.payment.model.PaymentRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoutingStrategy {
    Mono<String> route(PaymentRequest req, List<String> candidates);
}
