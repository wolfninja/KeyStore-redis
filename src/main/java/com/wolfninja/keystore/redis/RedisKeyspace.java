package com.wolfninja.keystore.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.wolfninja.keystore.api.KeyValue;
import com.wolfninja.keystore.api.Keyspace;

/**
 * Keyspace implementation for Redis
 * 
 * @author nick
 * @since 1.0
 */
public class RedisKeyspace implements Keyspace {

	private final String keyspaceName;
	private final JedisPool jedisPool;

	/**
	 * Constructor
	 * 
	 * @param keyspaceName
	 *            Keyspace name
	 * @param jedisPool
	 */
	protected RedisKeyspace(String keyspaceName, JedisPool jedisPool) {
		this.keyspaceName = keyspaceName;
		this.jedisPool = jedisPool;
	}

	@Override
	public boolean add(final String key, final String value) {
		Preconditions.checkNotNull(key, "Key should be provided");
		Preconditions.checkNotNull(value, "Value should be provided");

		return jedisInstance().hsetnx(keyspaceName, key, value) == 1;
	}

	@Override
	public boolean checkAndSet(final String key, final String value, long version) {
		// FIXME needs transaction support
		Preconditions.checkNotNull(key, "Key should be provided");
		Preconditions.checkNotNull(value, "Value should be provided");

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
		Preconditions.checkNotNull(key, "Key should be provided");
		return jedisInstance().hdel(keyspaceName, key) == 1;
	}

	@Override
	public boolean exists(final String key) {
		Preconditions.checkNotNull(key, "Key should be provided");

		return jedisInstance().hexists(keyspaceName, key);
	}

	@Override
	public Optional<KeyValue> get(final String key) {
		Preconditions.checkNotNull(key, "Key should be provided");

		final String value = jedisInstance().hget(keyspaceName, key);
		final KeyValue keyValue = (value == null) ? null : KeyValue.create(key, value, value.hashCode());
		return Optional.fromNullable(keyValue);
	}

	private Jedis jedisInstance() {
		return jedisPool.getResource();
	}

	@Override
	public boolean replace(final String key, final String value) {
		// FIXME needs transaction support
		Preconditions.checkNotNull(key, "Key should be provided");
		Preconditions.checkNotNull(value, "Value should be provided");

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
		Preconditions.checkNotNull(key, "Key should be provided");
		Preconditions.checkNotNull(value, "Value should be provided");

		jedisInstance().hset(keyspaceName, key, value);
		return true;
	}

}
