package com.wolfninja.keystore.redis;

import java.util.Objects;

import com.wolfninja.keystore.api.KeyValueStoreAdapter;
import com.wolfninja.keystore.api.Keyspace;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * KeyValueStoreAdapter instance for Redis
 * <p>
 * Implementation uses {@link Jedis}
 * 
 * @author nick
 * @since 1.0
 */
public class RedisAdapter implements KeyValueStoreAdapter {

	/**
	 * Create an adapter instance
	 * 
	 * @param jedisPool
	 *            Configured {@link JedisPool} instance, not null
	 * @return {@link RedisAdapter} instance backed by the provided {@link JedisPool}
	 */
	public static RedisAdapter create(final JedisPool jedisPool) {
		return new RedisAdapter(jedisPool);
	}

	private final JedisPool jedisPool;

	/**
	 * Constructor
	 * 
	 * @param jedisPool
	 *            Configured {@link JedisPool} instance, not null
	 */
	protected RedisAdapter(final JedisPool jedisPool) {
		Objects.requireNonNull(jedisPool, "JedisPool must not be null");
		this.jedisPool = jedisPool;
	}

	@Override
	public Keyspace getKeyspace(final String keyspaceName) {
		Objects.requireNonNull(keyspaceName, "KeyspaceName must not be null");
		return new RedisKeyspace(keyspaceName, jedisPool);
	}

}
