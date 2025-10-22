package live.akbarov.pspsystem.common.exception;

public class IdempotencyException extends AppException {
    public IdempotencyException(String message) {
        super("IDEMPOTENCY_VIOLATION", message);
    }

}
