package live.akbarov.pspsystem.common.exception;

import org.springframework.http.HttpStatus;

public class IdempotencyException extends AppException {
    public IdempotencyException(String message) {
        super("IDEMPOTENCY_VIOLATION", message, HttpStatus.CONFLICT);
    }

}
