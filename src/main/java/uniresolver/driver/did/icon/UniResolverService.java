package uniresolver.driver.did.icon;

import did.Authentication;
import did.DIDDocument;
import did.PublicKey;
import foundation.icon.did.DidService;
import foundation.icon.did.core.Algorithm;
import foundation.icon.did.core.AlgorithmProvider;
import foundation.icon.did.document.AuthenticationProperty;
import foundation.icon.did.document.Document;
import foundation.icon.did.document.EncodeType;
import foundation.icon.did.document.PublicKeyProperty;
import foundation.icon.icx.IconService;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.transport.http.HttpProvider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uniresolver.ResolutionException;
import uniresolver.driver.Driver;
import uniresolver.result.ResolveResult;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UniResolverService implements Driver {

    private static Logger log = LoggerFactory.getLogger(UniResolverService.class);

    private final IconNetworkConfig iconNetworkConfig;
    private final IconService iconService;
    private final DidService didService;

    public UniResolverService(IconNetworkConfig iconNetworkConfig) {
        this.iconNetworkConfig = iconNetworkConfig;
        log.debug("#### uniresolver_driver_did_icon_node_url=" + iconNetworkConfig.getNodeUrl());
        log.debug("#### uniresolver_driver_did_icon_score_addr=" + iconNetworkConfig.getDidScore());
        log.debug("#### uniresolver_driver_did_icon_network_id=" + iconNetworkConfig.getNetworkId());

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level level = iconNetworkConfig.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        logging.setLevel(level);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        this.iconService = new IconService(new HttpProvider(client, iconNetworkConfig.nodeUrl));
        BigInteger networkId = new BigInteger(iconNetworkConfig.getNetworkId());
        this.didService = new foundation.icon.did.DidService(iconService, networkId, new Address(iconNetworkConfig.getDidScore()));
    }

    @Override
    public ResolveResult resolve(String identifier) throws ResolutionException {
        long start = System.currentTimeMillis();

        try {
            Document doc = didService.readDocument(identifier);
            if (doc == null) {
                log.error("No resolve result for {}", identifier);
                throw new ResolutionException("No resolve result");
            }


            long elapsed = System.currentTimeMillis() - start;
            log.debug("DidService.readDocument() returned. elapsed time:{} ms", elapsed);

            List<PublicKey> publicKeyList = doc.getPublicKeyProperty().values().stream()
                    .map(this::convertIconPublicKeyToUR).collect(Collectors.toList());

            List<Authentication> authList = doc.getAuthentication().stream()
                    .map(this::convertIconAuthenticationToUR).collect(Collectors.toList());

            List<did.Service> services = new ArrayList<>();
            DIDDocument didDocument = DIDDocument.build(identifier, publicKeyList, authList, services);

            elapsed = System.currentTimeMillis() - start;
            log.debug("{} resolved. elapsed time:{} ms", identifier, elapsed);
            return ResolveResult.build(didDocument);

        } catch (IOException e) {
            throw new ResolutionException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> properties() throws ResolutionException {
        return iconNetworkConfig.getProperties();
    }

    /**
     * Convert ICON's PublicKeyProperty to UR's PublicKey
     *
     * @param iconKey
     * @return
     */
    PublicKey convertIconPublicKeyToUR(PublicKeyProperty iconKey) {
        String id = null;
        String[] types = null;
        String publicKeyBase64 = null;
        String publicKeyBase58 = null;
        String publicKeyHex = null;
        String publicKeyPem = null;

        long created = iconKey.getCreated();
        long revoked = iconKey.getRevoked();

        Algorithm algorithm = AlgorithmProvider.create(iconKey.getAlgorithmType());
        byte[] pub = algorithm.publicKeyToByte(iconKey.getPublicKey());

        id = iconKey.getId();
        List<String> pTypes = iconKey.getType();
        types = pTypes.toArray(new String[0]);
        if (iconKey.getEncodeType() == EncodeType.BASE64) {
            publicKeyBase64 = EncodeType.BASE64.encode(pub);
        } else if (iconKey.getEncodeType() == EncodeType.HEX) {
            publicKeyHex = EncodeType.HEX.encode(pub);
        }
        PublicKey pKey = PublicKey.build(id, types, publicKeyBase64, publicKeyBase58, publicKeyHex, publicKeyPem);
        pKey.setJsonLdObjectKeyValue("created", created);
        pKey.setJsonLdObjectKeyValue("revoked", revoked);
        return pKey;
    }

    /**
     * Convert ICON's Authentication List to UR's Authentication List.
     *
     * @param iconAuth
     * @return
     */
    Authentication convertIconAuthenticationToUR(AuthenticationProperty iconAuth) {
        String[] types = null;
        if (iconAuth.getType() != null) {
            types = new String[]{iconAuth.getType()};
        }
        String publicKey = iconAuth.getPublicKey();
        return Authentication.build(null, types, publicKey);
    }


}
