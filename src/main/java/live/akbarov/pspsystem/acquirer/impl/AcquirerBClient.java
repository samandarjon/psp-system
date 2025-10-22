package live.akbarov.pspsystem.acquirer.impl;

import live.akbarov.pspsystem.acquirer.AcquirerClient;
import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AcquirerBClient implements AcquirerClient {

    @Override
    public String name() {
        return "AcquirerB";
    }

    @Override
    public Mono<AcquirerResponse> authorize(PaymentRequest request) {
        var lastDigit = Character.getNumericValue(
                request.getCardNumber().charAt(request.getCardNumber().length() - 1)
        );
        var approved = lastDigit % 2 == 0;
        simulate();
        var status = approved ? AcquirerResponse.Status.APPROVED : AcquirerResponse.Status.DENIED;
        var msg = (approved ? "Approved by %s" : "Denied by %s").formatted(name());

        return Mono.just(new AcquirerResponse(status, msg, UUID.randomUUID()));
    }

    @SneakyThrows
    private static void simulate() {
        Thread.sleep(5000);
    }
}
