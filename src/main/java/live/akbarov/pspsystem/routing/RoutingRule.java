package live.akbarov.pspsystem.routing;


import live.akbarov.pspsystem.payment.model.PaymentRequest;

import java.util.List;
import java.util.Optional;

public interface RoutingRule {
    String id();

    Optional<String> decide(PaymentRequest req, List<String> candidates);
}
