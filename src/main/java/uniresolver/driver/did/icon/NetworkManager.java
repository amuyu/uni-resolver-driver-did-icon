package uniresolver.driver.did.icon;

import foundation.icon.did.DidService;
import foundation.icon.icx.IconService;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.transport.http.HttpProvider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Service;
import uniresolver.ResolutionException;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NetworkManager {

    private final AppConfig appConfig;
    private Map<BigInteger, IconNetwork> networkMap;

    public NetworkManager(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    void initialize() {
        networkMap = appConfig.getNetworks().stream()
                .collect(Collectors.toMap(IconNetwork::getNetworkId, n -> n, (n1, n2) -> n2));
        networkMap.values().forEach(n -> {
            log.debug("#### icon_node_url=" + n.getNodeUrl());
            log.debug("#### icon_score_addr=" + n.getDidScore());
            log.debug("#### icon_network_id=" + n.getNetworkId());
            log.debug("");
        });
        log.debug("Icon-network has been initialized.");
    }

    public IconNetwork getIconNetwork(String networkId) throws ResolutionException {
        BigInteger nid = new BigInteger(networkId);
        if (!networkMap.containsKey(nid)) {
            log.error("Could not find network for {}", networkId);
            throw new ResolutionException("Could not find network");
        }
        return networkMap.get(nid);
    }

    public DidService getDidService(String networkId) throws ResolutionException {
        IconNetwork network = getIconNetwork(networkId);
        return createDidService(network.getNodeUrl(), network.getNetworkId(), network.getDidScore());
    }


    OkHttpClient createHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level level = appConfig.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        logging.setLevel(level);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    DidService createDidService(String url, BigInteger networkId, String scoreAddress) {
        OkHttpClient httpClient = createHttpClient();
        IconService iconService = new IconService(new HttpProvider(httpClient, url));
        return new foundation.icon.did.DidService(iconService, networkId, new Address(scoreAddress));
    }
}
