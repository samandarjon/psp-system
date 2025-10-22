package live.akbarov.pspsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"live.akbarov.pspsystem.routing.properties"})
public class PspSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PspSystemApplication.class, args);
    }

}
