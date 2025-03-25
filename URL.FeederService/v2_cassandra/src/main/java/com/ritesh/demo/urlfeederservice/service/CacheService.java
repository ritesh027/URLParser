package com.ritesh.demo.urlfeederservice.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ritesh.demo.urlfeederservice.codec.URLSerializationCodec;
import com.ritesh.demo.urlfeederservice.model.URL;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class CacheService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheService.class);

    @Value("${com.ritesh.demo.urlfeederservice.service.cache.ttl}")
    private Integer ttl;

    @Value("${com.ritesh.demo.urlfeederservice.service.cache.url}")
    private String url;

    private RedisClient redisClient = null;

    private StatefulRedisConnection<String, URL> statefulRedisConnection = null;

    public URL get(String key){
        URL url = statefulRedisConnection.sync().get(key);
        if (url != null) {
            LOG.info("served from cache, for key {}", key);
        } else {
            LOG.info("cache miss for the key, {}", key);
        }
        return url;
    }

    public void set(URL url){
        // Long ttlSeconds =  TimeUnit.DAYS.toSeconds(this.ttl);
        Long ttlSeconds = 15L;
        statefulRedisConnection.sync().setex(url.getUrl(), ttlSeconds, url);
        LOG.info("cache stored");
    }

    @PostConstruct
    private void init(){
        LOG.info("post construct");
        redisClient = RedisClient.create(url);
        statefulRedisConnection = redisClient.connect(new URLSerializationCodec());
    }

    @PreDestroy
    private void destroy(){
        LOG.info("predestroy");
        if(statefulRedisConnection != null){
            statefulRedisConnection.close();
        }
        if(redisClient != null){
            redisClient.shutdown();
        }
    }
}
