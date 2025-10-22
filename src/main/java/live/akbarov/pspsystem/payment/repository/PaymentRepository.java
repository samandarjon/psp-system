package live.akbarov.pspsystem.payment.repository;

import live.akbarov.pspsystem.payment.entity.Payment;
import live.akbarov.pspsystem.payment.entity.Status;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for managing payment transactions.
 * <p>
 * This interface defines methods for saving, retrieving, and updating
 * payment transactions in the data store. It uses reactive programming
 * to handle operations asynchronously.
 */
public interface PaymentRepository {

    /**
     * Saves a payment transaction to the data store.
     * <p>
     * If the payment has no ID, a new ID will be generated.
     * If the payment has an ID, the existing payment with that ID will be updated.<b>(need to implement)</b>
     *
     * @param payment the payment transaction to save
     * @return a Mono that emits the saved payment transaction with its ID
     */
    Mono<Payment> save(Payment payment);

    /**
     * Finds a payment transaction by its ID.
     *
     * @param id the unique identifier of the payment transaction
     * @return a Mono that emits the payment transaction if found,
     * or an empty Mono if no transaction with the given ID exists
     */
    Mono<Payment> findById(UUID id);

    /**
     * Updates the status and external ID of a payment transaction.
     *
     * @param id         the unique identifier of the payment transaction to update
     * @param externalId the external reference ID provided by the acquirer
     * @param status     the new status of the payment transaction
     * @return a Mono that emits the updated payment transaction,
     * or an empty Mono if no transaction with the given ID exists
     */
    Mono<Payment> updateStatusAndExternalId(UUID id, UUID externalId, Status status);
}
