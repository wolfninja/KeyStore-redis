package com.wolfninja.keystore.redis;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wolfninja.keystore.api.BaseKeyspaceTest;

import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;
import redis.embedded.ports.EphemeralPortProvider;

@SuppressWarnings("javadoc")
@Test
public class RedisKeyspaceContractIntegrationTest extends BaseKeyspaceTest {

	private static RedisServer server;
	private static int port;

	private static int genPort() {
		final int port = new EphemeralPortProvider().next();
		RedisKeyspaceContractIntegrationTest.port = port;
		System.out.println("Using port: " + port);
		return port;
	}

	public RedisKeyspaceContractIntegrationTest() {
		super(new RedisAdapter(new JedisPool("localhost", genPort())).getKeyspace("awesome.keyspace"));
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
