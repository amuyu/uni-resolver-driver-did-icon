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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uniresolver.ResolutionException;
import uniresolver.driver.Driver;
import uniresolver.result.ResolveResult;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UniResolverService implements Driver {

    private static Logger log = LoggerFactory.getLogger(UniResolverService.class);

    private NetworkManager networkManager;

    public UniResolverService(NetworkManager networkManager) {
        this.networkManager = networkManager;
        networkManager.initialize();
    }

    @Override
    public ResolveResult resolve(String identifier) throws ResolutionException {
        long start = System.currentTimeMillis();

        try {
            String networkId = findNetworkId(identifier);
            DidService didService = networkManager.getDidService(networkId);
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

            IconNetwork iconNetwork = networkManager.getIconNetwork(networkId);

            // document metadata
            Map<String, Object> methodMetadata = new LinkedHashMap<>();
            methodMetadata.put("nodeUrl", iconNetwork.getNodeUrl());
            methodMetadata.put("scoreAddress", iconNetwork.getDidScore());
            // service name
            Map<String, Object> serviceMetadata = new LinkedHashMap<>();
            serviceMetadata.put("id", "ZZEUNG");
            serviceMetadata.put("serviceEndpoint", "https://zzeung.id/#/");
            methodMetadata.put("service", serviceMetadata);

            ResolveResult resolveResult = ResolveResult.build(didDocument);
            resolveResult.setMethodMetadata(methodMetadata);

            elapsed = System.currentTimeMillis() - start;
            log.debug("{} resolved. elapsed time:{} ms", identifier, elapsed);
            return resolveResult;

        } catch (IOException e) {
            throw new ResolutionException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> properties() throws ResolutionException {
        Map<String, Object> properties = new HashMap<>();

        String uniresolver_driver_did_icon_node_url = System.getenv("uniresolver_driver_did_icon_node_url");
        if (uniresolver_driver_did_icon_node_url != null)
            properties.put("uniresolver_driver_did_icon_node_url", uniresolver_driver_did_icon_node_url);
        String uniresolver_driver_did_icon_score_addr = System.getenv("uniresolver_driver_did_icon_score_addr");
        if (uniresolver_driver_did_icon_score_addr != null)
            properties.put("uniresolver_driver_did_icon_score_addr", uniresolver_driver_did_icon_score_addr);
        String uniresolver_driver_did_icon_network_id = System.getenv("uniresolver_driver_did_icon_network_id");
        if (uniresolver_driver_did_icon_network_id != null)
            properties.put("uniresolver_driver_did_icon_network_id", uniresolver_driver_did_icon_network_id);

        return properties;
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

    String findNetworkId(String did) throws ResolutionException {
        Pattern p = Pattern.compile("^did:icon:(.+):.+$");
        Matcher matcher = p.matcher(did);
        if (matcher.find()) {
            try {
                String networkId = matcher.group(1);
                if (networkId != null) {
                    return networkId;
                }
            } catch (Exception e) {
                log.error("Could not find networkId. did : {}, msg: {}", did, e.getMessage());
            }
        }

        throw new ResolutionException("Could not find networkId");
    }


}
