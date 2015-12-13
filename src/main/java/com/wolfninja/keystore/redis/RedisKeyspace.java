package com.wolfninja.keystore.redis;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.wolfninja.keystore.api.KeyValue;
import com.wolfninja.keystore.api.Keyspace;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Keyspace implementation for Redis
 * 
 * @since 0.1
 */
public class RedisKeyspace implements Keyspace {

	private final JedisPool jedisPool;

	private final String keyspaceName;

	/**
	 * Constructor
	 * 
	 * @param keyspaceName
	 *            Keyspace name, not null
	 * @param jedisPool
	 *            Jedis Connection pool, not null
	 * @since 0.1
	 */
	protected RedisKeyspace(@Nonnull final String keyspaceName, @Nonnull final JedisPool jedisPool) {
		Objects.requireNonNull(keyspaceName, "Keyspace name should be provided");
		Objects.requireNonNull(jedisPool, "Jedis Pool should be provided");
		this.keyspaceName = keyspaceName;
		this.jedisPool = jedisPool;
	}

	@Override
	public boolean add(final String key, final String value) {
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		return jedisInstance().hsetnx(keyspaceName, key, value) == 1;
	}

	@Override
	public boolean checkAndSet(final String key, final String value, long version) {
		// FIXME needs transaction support
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		final String existingValue = jedisInstance().hget(keyspaceName, key);

		if (existingValue != null) {
			final int existingVersion = existingValue.hashCode();
			if (existingVersion == version) {
				jedisInstance().hset(keyspaceName, key, value);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean delete(final String key) {
		Objects.requireNonNull(key, "Key should be provided");
		return jedisInstance().hdel(keyspaceName, key) == 1;
	}

	@Override
	public boolean deletes(final String key, final long version) {
		// FIXME needs transaction support
		Objects.requireNonNull(key, "Key should be provided");

		final String existingValue = jedisInstance().hget(keyspaceName, key);

		if (existingValue != null) {
			final int existingVersion = existingValue.hashCode();
			if (existingVersion == version) {
				jedisInstance().hdel(keyspaceName, key);
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean exists(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		return jedisInstance().hexists(keyspaceName, key);
	}

	@Override
	public Optional<String> get(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		return Optional.ofNullable(jedisInstance().hget(keyspaceName, key));
	}

	@Override
	public Optional<KeyValue> gets(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		final String value = jedisInstance().hget(keyspaceName, key);
		final KeyValue keyValue = (value == null) ? null : KeyValue.create(key, value, value.hashCode());
		return Optional.ofNullable(keyValue);
	}

	private Jedis jedisInstance() {
		return jedisPool.getResource();
	}

	@Override
	public boolean replace(final String key, final String value) {
		// FIXME needs transaction support
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		final Jedis resource = jedisInstance();

		final String existing = resource.hget(keyspaceName, key);
		if (existing != null) {
			if (value.hashCode() != existing.hashCode()) {
				resource.hset(keyspaceName, key, value);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean set(final String key, final String value) {
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		jedisInstance().hset(keyspaceName, key, value);
		return true;
	}

}
