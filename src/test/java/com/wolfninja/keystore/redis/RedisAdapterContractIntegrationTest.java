package com.wolfninja.keystore.redis;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wolfninja.keystore.api.BaseKeyValueStoreAdapterTest;

import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;
import redis.embedded.ports.EphemeralPortProvider;

@SuppressWarnings("javadoc")
@Test
public class RedisAdapterContractIntegrationTest extends BaseKeyValueStoreAdapterTest {

	private static RedisServer server;
	private static int port;

	private static int genPort() {
		final int port = new EphemeralPortProvider().next();
		RedisAdapterContractIntegrationTest.port = port;
		System.out.println("Using port: " + port);
		return port;
	}

	public RedisAdapterContractIntegrationTest() {
		super(new RedisAdapter(new JedisPool("localhost", genPort())));
	}

	@BeforeClass
	public void setUp() {
		server = RedisServer.builder().port(port).build();
		server.start();
	}

	@AfterClass
	public void tearDown() {
		if (server != null && server.isActive()) {
			server.stop();
		}
	}
}
