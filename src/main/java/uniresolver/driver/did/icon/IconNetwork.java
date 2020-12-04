package uniresolver.driver.did.icon;

import lombok.Data;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Data
public class IconNetwork {
    String nodeUrl;
    String didScore;
    String networkId;

    public BigInteger getNetworkId() {
        return new BigInteger(networkId);
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap();
        map.put("uniresolver_driver_did_icon_node_url", nodeUrl);
        map.put("uniresolver_driver_did_icon_score_addr", didScore);
        map.put("uniresolver_driver_did_icon_network_id", networkId);
        return map;
    }
}
