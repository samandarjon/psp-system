package live.akbarov.pspsystem.common.exception;

public class RoutingException extends AppException {
    public RoutingException(String message) {
        super("ROUTING_ERROR", message);
    }
}
