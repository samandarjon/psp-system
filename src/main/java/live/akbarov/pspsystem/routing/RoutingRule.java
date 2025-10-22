package live.akbarov.pspsystem.routing;


import live.akbarov.pspsystem.payment.model.PaymentRequest;

import java.util.List;
import java.util.Optional;

/**
 * Interface for defining rules that determine which acquirer should process a payment.
 * <p>
 * This interface defines the contract for routing rules that can be used by
 * routing strategies to select an appropriate acquirer for a payment request.
 * Each rule has a unique identifier and a decision method that applies the rule's logic.
 */
public interface RoutingRule {

    /**
     * Returns the unique identifier of this routing rule.
     *
     * @return a string that uniquely identifies this rule
     */
    String id();

    /**
     * Applies this rule to decide which acquirer should process the payment.
     * <p>
     * This method examines the payment request and the list of candidate acquirers,
     * and returns the name of the selected acquirer based on the rule's logic.
     * If no suitable acquirer is found, an empty Optional is returned.
     *
     * @param req        the payment request to route
     * @param candidates a list of available acquirer names to choose from
     * @return an Optional containing the name of the selected acquirer,
     * or empty if no suitable acquirer is found
     */
    Optional<String> decide(PaymentRequest req, List<String> candidates);
}
