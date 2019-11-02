package com.example.lettuce

import io.lettuce.core.ClientOptions
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeUnit


@Testcontainers
class SimpleReadWriteTest {

    @Container
    val redis: GenericContainer<Nothing> =
        GenericContainer<Nothing>("redis:5.0.3-alpine")
            .withExposedPorts(6379)

    lateinit var client: RedisClient
    lateinit var connection: StatefulRedisConnection<String, String>

    @BeforeEach
    fun setUp() {
        redis.start()
        val redisUri = "redis://${redis.containerIpAddress}:${redis.getMappedPort(6379)}/0"
        client = RedisClient.create(redisUri).apply {
            options =
                ClientOptions
                    .builder()
                    .requestQueueSize(4)
                    .build()
        }
        connection = this.client.connect()
    }

    @AfterEach
    fun tearDown() {
        connection.sync().flushall()
        connection.close()
        client.shutdown()
        redis.stop()
    }

    @Test
    fun `sync set then get`() {
        val sync = connection.sync()
        sync.set("key1", "value1")
        sync.set("key2", "value2")

        assertEquals(sync.get("key1"), "value1")
        assertEquals(sync.get("key2"), "value2")
    }

    @Test
    fun `async set then get`() {
        val async = connection.async()

        val future = async.set("key1", "value1").thenRun { println(">>>>> complete async task") }

        future.toCompletableFuture().get(10, TimeUnit.SECONDS)
        assertEquals(async.get("key1").get(), "value1")
    }
}