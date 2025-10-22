package live.akbarov.pspsystem.routing.strategy;

import live.akbarov.pspsystem.common.exception.RoutingException;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.routing.RoutingRule;
import live.akbarov.pspsystem.routing.RoutingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of RoutingStrategy that routes payments based on BIN (Bank Identification Number) rules.
 * <p>
 * This strategy uses a RoutingRule to determine which acquirer should process a payment
 * based on the card's BIN number. It delegates the decision to the injected rule and
 * converts the result to a reactive Mono.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BinBasedRoutingStrategy implements RoutingStrategy {
    private final RoutingRule rule;

    /**
     * {@inheritDoc}
     * <p>
     * This implementation delegates to the injected RoutingRule to make the routing decision.
     * If the rule returns an empty Optional (no suitable acquirer found), a RoutingException is thrown.
     */
    @Override
    public Mono<String> route(PaymentRequest req, List<String> candidates) {
        var acquirer = rule.decide(req, candidates);
        log.info("Decided to route payment to {} using rule {}", acquirer, rule.id());
        //TODO: Apply fallback strategy with chain of responsibility if needed
        return acquirer.map(Mono::just).orElseGet(() -> Mono.error(new RoutingException("No acquirer found")));
    }
}
