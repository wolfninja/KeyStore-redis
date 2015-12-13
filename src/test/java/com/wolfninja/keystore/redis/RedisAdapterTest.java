package com.wolfninja.keystore.redis;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import redis.clients.jedis.JedisPool;

import com.wolfninja.keystore.api.Keyspace;

@SuppressWarnings("javadoc")
@Test
public class RedisAdapterTest {

	@Test
	public void create() {
		final JedisPool mockPool = EasyMock.createMock(JedisPool.class);

		final RedisAdapter actual = RedisAdapter.create(mockPool);

		Assert.assertNotNull(actual);
	}

	@Test
	public void getKeyspace() {
		final JedisPool mockPool = EasyMock.createMock(JedisPool.class);

		final RedisAdapter adapter = RedisAdapter.create(mockPool);
		final Keyspace actual = adapter.getKeyspace("my.keyspace");

		Assert.assertEquals(actual.getClass(), RedisKeyspace.class);
	}
}
