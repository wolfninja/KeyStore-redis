# KeyStore-Redis
Redis client implementation of the [WolfNinja KeyStore abstraction API](http://github.com/wolfninja/KeyStore)

*Master branch:*
![Travis CI Build](https://img.shields.io/travis/wolfninja/KeyStore-redis.svg)
[![Code Coverage](https://img.shields.io/codecov/c/github/wolfninja/KeyStore-redis.svg)](https://codecov.io/github/wolfninja/KeyStore-redis)

*Develop branch:*
![Travis CI Build](https://img.shields.io/travis/wolfninja/KeyStore-redis/develop.svg)
[![Code Coverage](https://img.shields.io/codecov/c/github/wolfninja/KeyStore-redis/develop.svg)](https://codecov.io/github/wolfninja/KeyStore-redis?branch=develop)

## Description
Redis implementation of the KeyStore API

## Latest version
The most recent release is KeyStore-redis 0.1.0, released December 16, 2015. Targets [KeyStore API 0.1.0](https://github.com/wolfninja/KeyStore/tree/v0.1.0)

### Maven Central
Releases are available via Maven Central: [com.wolfninja.keystore:keystore-redis:0.1.0](http://search.maven.org/#artifactdetails|com.wolfninja.keystore|keystore-redis|0.1.0|bundle)

* Group ID: com.wolfninja.keystore
* Artifact ID: keystore-redis

### Gradle

```
dependencies {
  compile 'com.wolfninja.keystore:keystore-redis:0.1.0'
}
```

### Maven
```xml
    <dependency>
      <groupId>com.wolfninja.keystore</groupId>
      <artifactId>keystore-redis</artifactId>
      <version>0.1.0</version>
    </dependency>
```

## Usage Example
```java
		// Create MemoryAdapter instance
		final KeyValueStoreAdapter adapter = MemoryAdapter.create();
		// Create new KeyValueStore instance
		final KeyValueStore store = KeyValueStore.create(adapter);
		// Get key space "myKeyspace"
		final Keyspace keyspace = store.getKeyspace("myKeyspace");
```

## Configuration and limitations
- No configuration required
- This implementation supports the full API featureset

## Versioning
- This project uses [Semantic Versioning](http://semver.org/) to make release versions predictable
- Versions consist of MAJOR.MINOR.PATCH
  - Different MAJOR versions are not guaranteed to be API compatible
  - Incrementing MINOR versions within the same MAJOR version contain additional functionality, with existing calls being compatible
  - Different PATCH versions withing the same MAJOR.MINOR version are completely API compatible
- MAJOR and MINOR versions are compatible with the same API version

## Branches
- *master* is the "stable" branch from which releases are built
- *develop* branch is used for active development, and merged into *master* at release time


## Changelog
See CHANGELOG.md for full history of release changes

## License
Licenced under the MIT License (see LICENSE.txt)
