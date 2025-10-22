package live.akbarov.pspsystem.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiError> handleWebFluxBind(WebExchangeBindException ex) {
        Map<String, String> details = new HashMap<>();
        for (FieldError fe : ex.getFieldErrors()) {
            details.put(fe.getField(), fe.getDefaultMessage());
        }

        ApiError error = new ApiError(
                "VALIDATION_ERROR",
                "Validation failed",
                LocalDateTime.now(),
                details
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolations(ConstraintViolationException ex) {
        Map<String, String> details = new HashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            details.put(v.getPropertyPath().toString(), v.getMessage());
        }

        ApiError error = new ApiError(
                "VALIDATION_ERROR",
                "Validation failed",
                LocalDateTime.now(),
                details
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAcquirerException(AppException ex) {
        return build(ex.getStatus(), ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR", "Something went wrong");
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message) {
        ApiError error = new ApiError(
                code,
                message,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(error);
    }

    public record ApiError(
            String code,
            String message,
            LocalDateTime timestamp,
            Map<String, String> details) {
    }
}
