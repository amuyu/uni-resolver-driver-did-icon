![DIF Logo](https://raw.githubusercontent.com/decentralized-identity/decentralized-identity.github.io/master/images/logo-small.png)

# Universal Resolver Driver: did:icon

This is a [Universal Resolver](https://github.com/decentralized-identity/universal-resolver/) driver for **did:stack** identifiers.

## Specifications

* [Decentralized Identifiers](https://w3c-ccg.github.io/did-spec/)
* Blockicon DID Method Specification (missing)

## Example DIDs

```
did:icon:02:fcff15c4fd26b961031264005e2fd8a12404153fd8774f0a
```

## Build and Run (Docker)

```
docker build -f ./docker/Dockerfile . -t amuyu/driver-did-icon
docker run -p 8080:8080 amuyu/driver-did-icon
curl -X GET http://localhost:8080/1.0/identifiers/did:icon:02:fcff15c4fd26b961031264005e2fd8a12404153fd8774f0a
```

## Build (native Java)
```
./gradlew clean build
```

## Driver Environment Variables

The driver recognizes the following environment variables:

### `uniresolver_driver_did_icon_node_url`

  * The JSON-RPC URL of a node.
  * Default value(Testnet): `https://test-ctz.solidwallet.io/api/v3`
 
### `uniresolver_driver_did_icon_score_addr`

  * The SCORE Address for did.
  * Default value(Testnet): `cx4b4543847d552e6cfbbfa55fbd4a9378534839f9`
  
### `uniresolver_driver_did_icon_network_id`

  * The Network ID of a node.
  * Default value(Testnet): `https://test-ctz.solidwallet.io/api/v3`
  
## Driver Metadata

The driver returns the following metadata in addition to a DID document:

(none)