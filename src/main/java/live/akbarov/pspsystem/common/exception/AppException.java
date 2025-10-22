package live.akbarov.pspsystem.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for application-specific exceptions.
 * <p>
 * This class extends RuntimeException and adds a code field to identify
 * the type of exception. It is used as the base class for more specific
 * exceptions in the application.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppException extends RuntimeException {
    /**
     * A code that identifies the type of exception.
     * This code can be used for error handling and localization.
     */
    private final String code;
    private final HttpStatus status;

    /**
     * Constructs a new AppException with the specified code and message.
     *
     * @param code    an error code that identifies the type of exception
     * @param message a detailed message explaining the exception
     */
    public AppException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
