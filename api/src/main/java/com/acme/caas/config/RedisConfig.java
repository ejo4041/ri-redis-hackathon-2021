package com.acme.caas.config;

import com.redislabs.modules.rejson.JReJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.Pool;

@Configuration
public class RedisConfig {

    /**
     * <pre>
     * try (Jedis jedis = pool.getResource()) {
     *    jedis.set("foo", "bar");
     *    String foobar = jedis.get("foo");
     *    jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike");
     *    Set<String> sose = jedis.zrange("sose", 0, -1);
     * }
     * // ... when closing your application:
     * pool.close();
     * </pre>
     *
     * The JedisPool service allows you to grab a Jedis instance, interact with Redis, then
     * release it back into the pool. The pool of connections is helpful when you need to use a
     * jedis client in a blocking manner (pub/sub) and still need other clients for
     * redis interaction in the mean time. Also, the JReJSON library needs a JedisPool instance
     * in order to run commands on.
     * @param host the Host of the redis db
     * @param port the port of the redis db
     * @return a JedisPool Object
     */
    @Bean
    public JedisPool jedisPool(@Value("${spring.redis.host}") String host,
                               @Value("${spring.redis.port}") Integer port){
        JedisPoolConfig config = new JedisPoolConfig();
        var pool = new JedisPool(config,host,port);
        return pool;
    }


    @Bean
    public JReJSON redisJsonClient(JedisPool pool){
        JReJSON client = new JReJSON(pool);
        return client;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisTemplate template) {
        return template;
    }
}
