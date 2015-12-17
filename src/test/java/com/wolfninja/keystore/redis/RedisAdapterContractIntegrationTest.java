package com.wolfninja.keystore.redis;

import org.testng.annotations.Test;

import redis.clients.jedis.JedisPoolConfig;

import com.fiftyonred.mock_jedis.MockJedisPool;
import com.wolfninja.keystore.api.BaseKeyValueStoreAdapterTest;

@Test
public class RedisAdapterContractIntegrationTest extends BaseKeyValueStoreAdapterTest {

	public RedisAdapterContractIntegrationTest() {
		super(new RedisAdapter(new MockJedisPool(new JedisPoolConfig(), "127.0.0.1")));
	}
}
