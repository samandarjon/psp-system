package live.akbarov.pspsystem.common.exception;

import org.springframework.http.HttpStatus;

public class RoutingException extends AppException {
    public RoutingException(String message) {
        super("ROUTING_ERROR", message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
