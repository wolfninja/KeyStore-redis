package com.wolfninja.keystore.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.common.base.Preconditions;
import com.wolfninja.keystore.api.KeyValueStoreAdapter;
import com.wolfninja.keystore.api.Keyspace;

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
		Preconditions.checkNotNull(jedisPool, "JedisPool must not be null");
		this.jedisPool = jedisPool;
	}

	@Override
	public Keyspace getKeyspace(final String keyspaceName) {
		Preconditions.checkNotNull(keyspaceName, "KeyspaceName must not be null");
		return new RedisKeyspace(keyspaceName, jedisPool);
	}

}
