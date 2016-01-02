# KeyStore-Redis
Redis client implementation of the [WolfNinja KeyStore abstraction API](http://github.com/wolfninja/KeyStore)

*Master branch:*
![Travis CI Build](https://img.shields.io/travis/wolfninja/KeyStore-redis.svg)
[![Code Coverage](https://img.shields.io/codecov/c/github/wolfninja/KeyStore-redis.svg)](https://codecov.io/github/wolfninja/KeyStore-redis)

*Develop branch:*
![Travis CI Build](https://img.shields.io/travis/wolfninja/KeyStore-redis/develop.svg)
[![Code Coverage](https://img.shields.io/codecov/c/github/wolfninja/KeyStore-redis/develop.svg)](https://codecov.io/github/wolfninja/KeyStore-redis?branch=develop)

## Description
Redis implementation of the KeyStore API. This implementation uses the [Jedis client library](https://github.com/xetorthio/jedis).

## Latest version
The most recent release is KeyStore-redis 0.1.0, released January 1, 2016. Targets [KeyStore API 0.1.0](https://github.com/wolfninja/KeyStore/tree/v0.1.0)

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
		// Set up Jedis pool with connection info
		final JedisPool pool = new JedisPool("localhost", 6379);

		// Optional keyspace prefix (all keyspaces key names will be prefixed with this)
		// This allows the same keyspace name to be used across different backends (keeping API compatible),
		// but prevent key conflict in Redis
		final String keyspacePrefix = "kv_";

		// Create adaptor, passing in pool (and optional keyspace prefix)
		final RedisAdapter adapter = RedisAdapter.create(pool, keyspacePrefix);
		final Keyspace keyspace = adapter.getKeyspace("mySpace");

		
		// Add a key+value pair to the keyspace
		assert keyspace.set("test", "Hiya buddy");
		assert keyspace.set("other", "This is a different key");
		
		//Check what we stored in redis
		{
			// Connect to redis, grab the keyspace we just created
			// each keyspace is a separate hash (prefix + keyspace name)
			final Map<String, String> hash = pool.getResource().hgetAll("kv_mySpace");
			assert hash.size() == 2;
			assert hash.get("test").equals("Hiya buddy");
			assert hash.get("other").equals("This is a different key");
		}
```

## Configuration
- Requires passing in a configured JedisPool instance to the RedisAdapter (see Usage example)

## Limitations
- API: This implementation supports the full API featureset
- Note: Because Redis doesn't allow locking on individual hash keys, we currently lock on the entire Keyspace when doing transactional/non-atomic operations. If the Keyspace is otherwise modified before the transaction commits, the transaction will be rolled back. Be sure to especially check the return values on these and retry if necessary. This is tracked in [issue #1](https://github.com/wolfninja/KeyStore-redis/issues/1)
    - Affected methods:
        - checkAndSet
        - deletes
        - replace

## Implementation Details
- This backend uses a Jedis pool to connect to Redis
- Each Keyspace is internally represented in Redis as a hash
    - The hash name is the keyspace name (prefixed with the Keyspace prefix if provided)

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
