package uniresolver.driver.did.icon;


import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DidPatternTest {

    @Test
    public void testValidation() {
        String did = "did:icon:02:1b7aef9ba939d5730e2c2a52fe35578fe8d7a67ac137c4c8";
        Pattern p = Pattern.compile("^(did:icon:.+)$");
        Matcher matcher = p.matcher(did);
        assertThat(matcher.find()).isTrue();
    }

    @Test
    public void testFindNetworkId() {
        String did = "did:icon:02:1b7aef9ba939d5730e2c2a52fe35578fe8d7a67ac137c4c8";
        Pattern p = Pattern.compile("^did:icon:(.+):.+$");
        Matcher matcher = p.matcher(did);
        assertThat(matcher.find()).isTrue();
        String networkId = matcher.group(1);
        assertThat(networkId).isEqualTo("02");
    }

}