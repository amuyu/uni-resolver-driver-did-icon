[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)  
![DIF Logo](https://raw.githubusercontent.com/decentralized-identity/decentralized-identity.github.io/master/images/logo-small.png)

# Universal Resolver Driver: did:icon

This is a [Universal Resolver](https://github.com/decentralized-identity/universal-resolver/) driver for **did:icon** identifiers.

## Specifications

* [Decentralized Identifiers](https://w3c-ccg.github.io/did-spec/)
* [DID Method Spec](https://github.com/icon-project/icon-DID/blob/master/docs/ICON-DID-method.md)

## Example DIDs

```
did:icon:01:64aa0a2a479cb47afbf2d18d6f9f216bcdcbecdda27ccba3
did:icon:02:6f7a00a29deb82cb36d501d687c18bad79a8f1c154ef0c78
```

## Build and Run (Docker)

```
docker build -f ./docker/Dockerfile . -t amuyu/driver-did-icon
docker run -p 8080:8080 amuyu/driver-did-icon
curl -X GET http://localhost:8080/1.0/identifiers/did:icon:02:6f7a00a29deb82cb36d501d687c18bad79a8f1c154ef0c78
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
  * Default value(Testnet): `cx8b19bdb4e1ad3e10b599d8887dd256e02995f340`

### `uniresolver_driver_did_icon_network_id`

  * The Network ID of a node.
  * Default value(Testnet): `2`

## Driver Metadata

The driver returns the following metadata in addition to a DID document:

(none)
