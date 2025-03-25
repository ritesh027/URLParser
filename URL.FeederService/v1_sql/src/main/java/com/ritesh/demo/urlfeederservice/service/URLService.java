package com.ritesh.demo.urlfeederservice.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ritesh.demo.urlfeederservice.dao.URLRepository;
import com.ritesh.demo.urlfeederservice.model.URL;
import com.ritesh.demo.urlfeederservice.resources.URLResource;

@Service
public class URLService {

    private static final Logger LOG = LoggerFactory.getLogger(URLService.class);

    @Value("#{${com.ritesh.demo.urlfeederservice.topics}}")
    Map<String, String> kafkaTopics;

    @Value("${com.ritesh.demo.urlfeederservice.cooldown}")
    private int cooldown;


    @Autowired
    private URLRepository urlRepository;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private CacheService cacheService;

    @Async
    public void save(Set<URL> urls){
        for(URL url: urls){
            try {
                LOG.info("----------- {}", Thread.currentThread().getName());
                // checking into the cache
                if(cacheService.get(url.getUrl()) != null)
                {
                    LOG.info("url {} is processed within 1 days", url.getUrl());
                    return;
                }
                URL existingurl = urlRepository.findByUrl(url.getUrl());
                Optional<String> optContentType = Optional.empty();
                if(existingurl != null)
                {
                    if(existingurl.getLastProcessed().getTime() + TimeUnit.DAYS.toMillis(cooldown) > System.currentTimeMillis()){
                        LOG.info("URL is {} Already processed within 7 days.");
                        cacheService.set(existingurl);
                        return;
                    }

                    url = existingurl;
                    optContentType = Optional.of(existingurl.getContentType());
                }

                url.setLastProcessed(new Timestamp(System.currentTimeMillis()));
                url.setTimesProcessed(url.getTimesProcessed() + 1);
                if(optContentType.isEmpty()){
                    optContentType = getContentType(url.getUrl());
                }
                if (optContentType.isEmpty()){
                    LOG.warn("Content type not found for url: {}", url.getUrl());
                    return;
                }
                Optional<String> optTopic = getTopicsbyContentType(optContentType.get());
                if(optTopic.isEmpty()){
                    LOG.warn("Content type {} not mapped", optContentType.get());
                }
                String topic = optTopic.get();
                LOG.info("url: {}, Topic: {}", url.getUrl(), topic);
                urlRepository.save(url);
                cacheService.set(url);
                // kafkaService.send(topic, url.getUrl());
                LOG.info("*****************************SUCCESS IN DB for {} ***********************************************", url.getUrl());
                

            } catch (IOException ex) {
                // TODO: handle exception
                LOG.error("Exception: {}", ex);
            }
        }
        
    }

    private Optional<String> getContentType(String path) throws IOException{
        java.net.URI uri = java.net.URI.create(path);
        java.net.URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        return Optional.of(connection.getContentType());
    }

    private Optional<String> getTopicsbyContentType(String rawContentType) throws IOException{
        String contentType = rawContentType.split(";")[0].trim();
        LOG.info("contentType {}", contentType);

        if (kafkaTopics.containsKey(contentType)) {
            return Optional.of(kafkaTopics.get(contentType));
        }
        return Optional.empty();

    }
    
}
