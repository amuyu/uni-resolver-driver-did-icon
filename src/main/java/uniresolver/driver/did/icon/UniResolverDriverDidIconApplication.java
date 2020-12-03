package uniresolver.driver.did.icon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ IconNetworkConfig.class })
public class UniResolverDriverDidIconApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniResolverDriverDidIconApplication.class, args);
    }

}
