package com.wolfninja.keystore.redis;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.wolfninja.keystore.api.KeyValue;
import com.wolfninja.keystore.api.Keyspace;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

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

		try (final Jedis jedis = jedisInstance()) {
			return jedis.hsetnx(keyspaceName, key, value) == 1;
		}
	}

	@Override
	public boolean checkAndSet(final String key, final String value, long version) {
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		try (final Jedis jedis = jedisInstance()) {
			// FIXME right now we are locking on the entire keyspace/hash (issue #1)
			jedis.watch(keyspaceName);
			final String existingValue = jedis.hget(keyspaceName, key);
			final Transaction trans = jedis.multi();
			if (existingValue != null) {
				final int existingVersion = existingValue.hashCode();
				if (existingVersion == version) {
					trans.hset(keyspaceName, key, value);
					final List<Object> response = trans.exec();
					return response != null && response.size() == 1 && response.get(0).equals(0L);
				}
			}
			trans.discard();
		}
		return false;
	}

	@Override
	public boolean delete(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		try (final Jedis jedis = jedisInstance()) {
			return jedis.hdel(keyspaceName, key) == 1;
		}
	}

	@Override
	public boolean deletes(final String key, final long version) {
		Objects.requireNonNull(key, "Key should be provided");

		try (final Jedis jedis = jedisInstance()) {
			// FIXME right now we are locking on the entire keyspace/hash (issue #1)
			jedis.watch(keyspaceName);
			final String existingValue = jedis.hget(keyspaceName, key);
			final Transaction trans = jedis.multi();
			if (existingValue != null) {
				final int existingVersion = existingValue.hashCode();
				if (existingVersion == version) {
					trans.hdel(keyspaceName, key);
					final List<Object> result = trans.exec();
					return result != null && result.size() == 1 && result.get(0).equals(1L);
				}
			}
			trans.discard();
		}
		return false;

	}

	@Override
	public boolean exists(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		try (final Jedis jedis = jedisInstance()) {
			return jedis.hexists(keyspaceName, key);
		}
	}

	@Override
	public Optional<String> get(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		try (final Jedis jedis = jedisInstance()) {
			return Optional.ofNullable(jedis.hget(keyspaceName, key));
		}
	}

	@Override
	public Optional<KeyValue> gets(final String key) {
		Objects.requireNonNull(key, "Key should be provided");

		try (final Jedis jedis = jedisInstance()) {
			final String value = jedis.hget(keyspaceName, key);
			final KeyValue keyValue = (value == null) ? null : KeyValue.create(key, value, value.hashCode());
			return Optional.ofNullable(keyValue);
		}
	}

	private Jedis jedisInstance() {
		return jedisPool.getResource();
	}

	@Override
	public boolean replace(final String key, final String value) {
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		try (final Jedis jedis = jedisInstance()) {
			// FIXME right now we are locking on the entire keyspace/hash (issue #1)
			jedis.watch(keyspaceName);
			final String existing = jedis.hget(keyspaceName, key);
			final Transaction trans = jedis.multi();
			if (existing != null) {
				if (value.hashCode() != existing.hashCode()) {
					trans.hset(keyspaceName, key, value);
					final List<Object> response = trans.exec();
					return response != null && response.size() == 1 && response.get(0).equals(0L);
				}
			}
			trans.discard();
		}
		return false;
	}

	@Override
	public boolean set(final String key, final String value) {
		Objects.requireNonNull(key, "Key should be provided");
		Objects.requireNonNull(value, "Value should be provided");

		try (final Jedis jedis = jedisInstance()) {
			jedis.hset(keyspaceName, key, value);
			return true;
		}
	}

}
