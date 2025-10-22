package live.akbarov.pspsystem.common.exception;

import org.springframework.http.HttpStatus;

public class AcquirerException extends AppException {
    public AcquirerException(String message) {
        super("ACQUIRER_ERROR", message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
