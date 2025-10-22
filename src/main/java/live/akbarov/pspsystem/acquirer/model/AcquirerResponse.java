package live.akbarov.pspsystem.acquirer.model;

import java.util.UUID;

public record AcquirerResponse(Status status,
                               String message,
                               UUID referenceId) {
    public enum Status {
        APPROVED, DENIED
    }
}
