package live.akbarov.pspsystem.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppException extends RuntimeException {
    private final String code;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }
}
