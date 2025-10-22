package live.akbarov.pspsystem.common.error;


import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        String code,        // e.g., VALIDATION_ERROR
        String message,
        LocalDateTime timestamp,
        Map<String, String> details) {
}
