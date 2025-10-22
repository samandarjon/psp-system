package live.akbarov.pspsystem.routing.rule;


import live.akbarov.pspsystem.payment.model.PaymentRequest;
import live.akbarov.pspsystem.routing.RoutingRule;
import live.akbarov.pspsystem.routing.properties.BinRoutingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BinRoutingRule implements RoutingRule {
    private final BinRoutingProperties props;

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

    @Override
    public String id() {
        return "bin";
    }

}
