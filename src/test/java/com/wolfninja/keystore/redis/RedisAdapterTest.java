package com.wolfninja.keystore.redis;

import java.util.Optional;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wolfninja.keystore.api.Keyspace;

import redis.clients.jedis.JedisPool;

@SuppressWarnings("javadoc")
@Test
public class RedisAdapterTest {

	@Test(dataProvider = "buildKeyspaceNameData")
	public void buildKeyspaceName(final String inputPrefix, final String expected) {
		final JedisPool mockPool = EasyMock.createMock(JedisPool.class);

		final RedisAdapter adapter = RedisAdapter.create(mockPool, inputPrefix);
		final String actual = adapter.buildKeyspaceName("myKeyspace");
		Assert.assertEquals(actual, expected);
	}

	@DataProvider
	Object[][] buildKeyspaceNameData() {
		return new Object[][] { //
				{ null, "myKeyspace" }, //
				{ "nice_", "nice_myKeyspace" }, //
		};
	}

	@Test
	public void create() {
		final JedisPool mockPool = EasyMock.createMock(JedisPool.class);

		final RedisAdapter actual = RedisAdapter.create(mockPool);

		Assert.assertNotNull(actual);
	}

	@Test
	public void createWithPrefix() {
		final JedisPool mockPool = EasyMock.createMock(JedisPool.class);

		final RedisAdapter actual = RedisAdapter.create(mockPool, "asdf_");

		Assert.assertNotNull(actual);
		Assert.assertEquals(actual.getKeyspacePrefix(), Optional.of("asdf_"));
	}

	@Test
	public void getKeyspace() {
		final JedisPool mockPool = EasyMock.createMock(JedisPool.class);

		final RedisAdapter adapter = RedisAdapter.create(mockPool);
		final Keyspace actual = adapter.getKeyspace("my.keyspace");

		Assert.assertEquals(actual.getClass(), RedisKeyspace.class);
	}
}
