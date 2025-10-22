package live.akbarov.pspsystem.integration.routing;


import live.akbarov.pspsystem.common.exception.RoutingException;
import live.akbarov.pspsystem.routing.RoutingStrategy;
import live.akbarov.pspsystem.routing.properties.BinRoutingProperties;
import live.akbarov.pspsystem.routing.rule.BinrayBinRoutingRule;
import live.akbarov.pspsystem.routing.strategy.BinBasedRoutingStrategy;
import live.akbarov.pspsystem.untils.DataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class BinBasedRoutingStrategyTest {

    private RoutingStrategy routingStrategy;
    private final BinRoutingProperties properties = new BinRoutingProperties("ACQ_A", "ACQ_B");
    private final List<String> candidates = List.of("ACQ_A", "ACQ_B");

    @BeforeEach
    public void setup() {
        var rule = new BinrayBinRoutingRule(properties);
        this.routingStrategy = new BinBasedRoutingStrategy(rule);
    }

    @Test
    void shouldRouteToAcquirerAWhenBinDigitSumIsEven() {
        // BIN 400000 → digits = 4+0+0+0+0+0 = 4 (even) → ACQ_A
        var req = DataFactory.requestWithCard("4000001234567890");
        var acquirer = routingStrategy.route(req, candidates);
        StepVerifier.create(acquirer).expectNext("ACQ_A").verifyComplete();

    }

    @Test
    void shouldRouteToAcquirerBWhenBinDigitSumIsOdd() {
        // BIN 510001 → digits = 5+1+0+0+0+1 = 7 (odd) → ACQ_B
        var req = DataFactory.requestWithCard("5100019876543210");
        var acquirer = routingStrategy.route(req, candidates);
        StepVerifier.create(acquirer).expectNext("ACQ_B").verifyComplete();

    }

    @Test
    void shouldUseFirstSixDigitsAsBINAndIgnoreRest() {
        // First six define decision: 400002 → 4+0+0+0+0+2 = 6 (even) → ACQ_A
        var req = DataFactory.requestWithCard("4000029999999999");
        var acquirer = routingStrategy.route(req, candidates);
        StepVerifier.create(acquirer).expectNext("ACQ_A").verifyComplete();
    }

    @Test
    void shouldThrowWhenAvailableRoutingNotExist() {
        var req = DataFactory.requestWithCard("4000029999999996");
        var acquirer = routingStrategy.route(req, List.of());
        StepVerifier.create(acquirer).expectError(RoutingException.class).verify();
    }
}
