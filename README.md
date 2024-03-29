[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)  
![DIF Logo](https://raw.githubusercontent.com/decentralized-identity/decentralized-identity.github.io/master/images/logo-small.png)

# Universal Resolver Driver: did:icon for zzeung service

This is a [Universal Resolver](https://github.com/decentralized-identity/universal-resolver/) driver for **did:icon** identifiers.

This driver is for identifiers using zzeung service.


## Specifications

* [Decentralized Identifiers](https://w3c-ccg.github.io/did-spec/)
* [DID Method Spec](https://github.com/icon-project/icon-DID/blob/master/docs/ICON-DID-method.md)

## Example DIDs

```
did:icon:01:64aa0a2a479cb47afbf2d18d6f9f216bcdcbecdda27ccba3
did:icon:53:85b0b264ecae5cc82ba517145f9f2d444ab954e8028ce155
```

## Build and Run (Docker)

```
docker build -f ./docker/Dockerfile . -t amuyu/driver-did-icon
docker run -p 8080:8080 amuyu/driver-did-icon
curl -X GET http://localhost:8080/1.0/identifiers/did:icon:01:64aa0a2a479cb47afbf2d18d6f9f216bcdcbecdda27ccba3
```

## Build (native Java)
```
./gradlew clean build
```

## Driver Environment Variables

The driver recognizes the following environment variables:

### `uniresolver_driver_did_icon_node_url`

  * The JSON-RPC URL of a node.
  * Default value(Sejong testnet): `https://sejong.net.solidwallet.io/api/v3`

### `uniresolver_driver_did_icon_score_addr`

  * The SCORE Address for did.
  * Default value(Sejong testnet): `cxc7c8b0bb85eca64aecc8cc38628c4bc3c449f1fd`

### `uniresolver_driver_did_icon_network_id`

  * The Network ID of a node.
  * Default value(Sejong testnet): `83 (0x53)`

## Driver Metadata

The driver returns the following metadata in addition to a DID document:

(none)
