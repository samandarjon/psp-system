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

@Slf4j
@Component
@RequiredArgsConstructor
public class BinBasedRoutingStrategy implements RoutingStrategy {
    private final RoutingRule rule;

    @Override
    public Mono<String> route(PaymentRequest req, List<String> candidates) {
        var acquirer = rule.decide(req, candidates);
        log.info("Decided to decide {} for {} by {}", acquirer, acquirer, rule.id());
        //TODO: Apply fallback strategy with chain of responsibility if needed
        return acquirer.map(Mono::just).orElseGet(() -> Mono.error(new RoutingException("No acquirer found")));

    }
}
