package live.akbarov.pspsystem.routing.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "psp.routing.bin")
public class BinRoutingProperties {
    private String evenAcquirer = "AcquirerA";
    private String oddAcquirer = "AcquirerB";
}
