package com.wolfninja.keystore.redis;

import org.testng.annotations.Test;

import redis.clients.jedis.JedisPoolConfig;

import com.fiftyonred.mock_jedis.MockJedisPool;
import com.wolfninja.keystore.api.BaseKeyspaceTest;

@Test
public class RedisKeyspaceContractTest extends BaseKeyspaceTest {

	public RedisKeyspaceContractTest() {
		super(new RedisAdapter(new MockJedisPool(new JedisPoolConfig(), "127.0.0.1")).getKeyspace("awesome.keyspace"));
	}
}
