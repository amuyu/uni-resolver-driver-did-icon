package uniresolver.driver.did.icon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    boolean debug;
    List<IconNetwork> networks;
}
