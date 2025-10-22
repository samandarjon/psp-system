package live.akbarov.pspsystem.acquirer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AcquirerRegistry {
    private final Map<String, AcquirerClient> byName;
    private final List<String> orderedNames;

    public AcquirerRegistry(List<AcquirerClient> clients) {
        this.byName = clients.stream().collect(Collectors.toMap(AcquirerClient::name, Function.identity()));
        this.orderedNames = byName.keySet().stream().sorted().toList(); // stable order
    }

    public AcquirerClient get(String name) {
        return Optional.ofNullable(byName.get(name))
                .orElseThrow(() -> new IllegalArgumentException("No acquirer client with name " + name));
    }

    public List<String> allNames() {
        return orderedNames;
    }
}
