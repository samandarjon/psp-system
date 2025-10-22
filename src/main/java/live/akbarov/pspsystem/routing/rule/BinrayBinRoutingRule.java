package live.akbarov.pspsystem.routing.rule;


import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.routing.RoutingRule;
import live.akbarov.pspsystem.routing.properties.BinRoutingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of RoutingRule that routes payments based on the sum of digits in the card's BIN.
 * <p>
 * This rule extracts the BIN (Bank Identification Number, first 6 digits) from the card number,
 * calculates the sum of its digits, and routes to different acquirers based on whether
 * the sum is even or odd:
 * - Even sum: route to the configured "evenAcquirer"
 * - Odd sum: route to the configured "oddAcquirer"
 */
@Component
@RequiredArgsConstructor
public class BinrayBinRoutingRule implements RoutingRule {
    private final BinRoutingProperties props;

    /**
     * {@inheritDoc}
     * <p>
     * This implementation:
     * <li>1. Extracts the BIN (first 6 digits) from the card number</li>
     * <li>2. Calculates the sum of the digits in the BIN</li>
     * <li>3. Selects the appropriate acquirer based on whether the sum is even or odd</li>
     * <li>4. Returns the first candidate that matches the desired acquirer name (case-insensitive)</li>
     * <li>5. Returns empty if no matching acquirer is found in the candidates list</li>
     */
    @Override
    public Optional<String> decide(PaymentRequest req, List<String> candidates) {
        var bin = req.getCardNumber().substring(0, 6);
        var sum = bin.chars().map(c -> c - '0').sum();
        var desired = (sum % 2 == 0) ? props.getEvenAcquirer() : props.getOddAcquirer();
        //TODO: In case of empty, maybe it's better to have some fallbacks
        return candidates.stream()
                .filter(c -> c.equalsIgnoreCase(desired))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     *
     * @return the string "binary-bin-rule" as the unique identifier for this rule
     */
    @Override
    public String id() {
        return "binary-bin-rule";
    }

}
