package com.acme.caas;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

public class RedisTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    private static AtomicBoolean started = new AtomicBoolean(false);

    private static GenericContainer redis = new GenericContainer("redislabs/redismod:latest").withExposedPorts(6379);

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        // Commenting-out the if statement fixes the unit test on my Ubuntu machine.  Without the following
        // if statement commented-out, the tests only succeed exactly 50% of the time. (JWC)
        //if (!started.get()) {
        redis.start();
        System.setProperty("test.redis.server.port", redis.getMappedPort(6379).toString());
        System.setProperty("jhipster.cache.redis.server", "redis://" + redis.getContainerIpAddress() + ":" + redis.getMappedPort(6379));
        started.set(true);
        //}
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (started.get()) {
            redis.stop();
        }
    }
}
