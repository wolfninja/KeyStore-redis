package com.wolfninja.keystore.redis;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.wolfninja.keystore.api.KeyValueStoreAdapter;
import com.wolfninja.keystore.api.Keyspace;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * KeyValueStoreAdapter instance for Redis
 * <p>
 * Implementation uses {@link Jedis} as Redis client
 * 
 * @since 0.1
 */
public class RedisAdapter implements KeyValueStoreAdapter {

	/**
	 * Create an adapter instance, with no keyspace prefix
	 * 
	 * @param jedisPool
	 *            Configured {@link JedisPool} instance, not null
	 * @return {@link RedisAdapter} instance backed by the provided {@link JedisPool}
	 * @since 0.1
	 */
	public static RedisAdapter create(@Nonnull final JedisPool jedisPool) {
		return new RedisAdapter(jedisPool);
	}

	/**
	 * Create an adapter instance with the provided keyspace prefix
	 * 
	 * @param jedisPool
	 *            Configured {@link JedisPool} instance, not null
	 * @param prefix
	 *            String prefix to use for the Redis keyspace name, nullable
	 * @return {@link RedisAdapter} instance backed by the provided {@link JedisPool}
	 * @since 0.1
	 */
	public static RedisAdapter create(@Nonnull final JedisPool jedisPool, @Nullable final String prefix) {
		return new RedisAdapter(jedisPool, prefix);
	}

	private final JedisPool jedisPool;
	private final String keyspacePrefix;

	/**
	 * Constructor
	 * 
	 * @param jedisPool
	 *            Configured {@link JedisPool} instance, not null
	 * @since 0.1
	 */
	protected RedisAdapter(@Nonnull final JedisPool jedisPool) {
		this(jedisPool, null);
	}

	/**
	 * Constructor
	 * 
	 * @param jedisPool
	 *            Configured {@link JedisPool} instance, not null
	 * @param keyspacePrefix
	 *            String prefix to use for the Redis key names, nullable
	 * @since 0.1
	 */
	protected RedisAdapter(@Nonnull final JedisPool jedisPool, @Nullable final String keyspacePrefix) {
		Objects.requireNonNull(jedisPool, "JedisPool must not be null");
		this.jedisPool = jedisPool;
		this.keyspacePrefix = keyspacePrefix;
	}

	protected String buildKeyspaceName(final String keyspaceName) {
		return keyspacePrefix != null ? keyspacePrefix + keyspaceName : keyspaceName;
	}

	@Override
	public Keyspace getKeyspace(final String keyspaceName) {
		Objects.requireNonNull(keyspaceName, "KeyspaceName must not be null");
		return new RedisKeyspace(buildKeyspaceName(keyspaceName), jedisPool);
	}

	/**
	 * Return the configured keyspace prefix used to constuct the redis keys
	 * 
	 * @return {@link Optional} String keyspace prefix
	 */
	@Nonnull
	public Optional<String> getKeyspacePrefix() {
		return Optional.ofNullable(keyspacePrefix);
	}
}
