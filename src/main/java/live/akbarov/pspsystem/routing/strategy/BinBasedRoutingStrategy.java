package live.akbarov.pspsystem.routing.strategy;

import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.routing.RoutingStrategy;
import live.akbarov.pspsystem.routing.rule.BinRoutingRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BinBasedRoutingStrategy implements RoutingStrategy {
    private final BinRoutingRule routingRule;

    @Override
    public Mono<String> route(PaymentRequest req, List<String> candidates) {
        var acquirer = routingRule.decide(req, candidates);
        //TODO: Apply fallback strategy with chain of responsibility if needed
        return acquirer.map(Mono::just).orElseGet(() -> Mono.error(new IllegalStateException("No acquirer found")));

    }
}
