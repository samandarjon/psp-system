package live.akbarov.pspsystem.acquirer;

import live.akbarov.pspsystem.acquirer.model.AcquirerResponse;
import live.akbarov.pspsystem.payment.model.PaymentRequest;
import reactor.core.publisher.Mono;

/**
 * Interface for clients that communicate with payment acquirers.
 * 
 * This interface defines the contract for clients that handle communication
 * with payment acquirers (payment processors). Each client is responsible for
 * sending payment requests to a specific acquirer and handling the responses.
 */
public interface AcquirerClient {

    /**
     * Returns the name of the acquirer that this client communicates with.
     * 
     * @return a string that uniquely identifies the acquirer
     */
    String name();

    /**
     * Sends a payment authorization request to the acquirer.
     * 
     * This method processes the payment request by sending it to the acquirer
     * and returning the acquirer's response. The implementation should handle
     * any necessary communication protocols and data transformations.
     * 
     * @param request the payment request to authorize
     * @return a Mono that emits the acquirer's response to the authorization request
     */
    Mono<AcquirerResponse> authorize(PaymentRequest request);
}
