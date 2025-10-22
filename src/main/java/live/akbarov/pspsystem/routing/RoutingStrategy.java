package live.akbarov.pspsystem.routing;

import live.akbarov.pspsystem.payment.model.PaymentRequest;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Strategy interface for routing payment requests to appropriate acquirers.
 * <p>
 * This interface defines the contract for routing strategies that determine
 * which acquirer should process a given payment request. Different implementations
 * can use different algorithms or rules for making this decision.
 */
public interface RoutingStrategy {

    /**
     * Routes a payment request to an appropriate acquirer.
     * <p>
     * This method selects one acquirer from the list of candidates based on
     * the payment request details and the strategy's routing logic.
     *
     * @param req        the payment request to route
     * @param candidates a list of available acquirer names to choose from
     * @return a Mono that emits the name of the selected acquirer,
     * or an error if no suitable acquirer can be found
     */
    Mono<String> route(PaymentRequest req, List<String> candidates);
}
