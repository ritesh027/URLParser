package com.ritesh.demo.urlfeederservice.resources;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ritesh.demo.urlfeederservice.common.Constants;
import com.ritesh.demo.urlfeederservice.model.URL;
import com.ritesh.demo.urlfeederservice.service.URLService;

@RestController
public class URLResource {


    private static final Logger LOG = LoggerFactory.getLogger(URLResource.class);

    @Autowired
    private URLService urlService;

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> batchSubmitURL(@RequestBody Set<URL> urls){

        urls.forEach(url ->{
            url.setId(UUID.randomUUID().toString());
            url.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            url.setTimesProcessed(0);
        });
        LOG.info("######################################req START###################################################");
        urlService.save(urls);
        LOG.info("######################################req END###################################################");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/url")
    public ResponseEntity<Void> submitURL(@RequestBody URL url){
        
        long startTime = System.currentTimeMillis();
        url.setId(UUID.randomUUID().toString());
        url.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        url.setTimesProcessed(0);
        LOG.info("message: {}", url);
        LOG.info("######################################req START###################################################");
        urlService.save(new HashSet<>() {{
            add(url);
        }});
        LOG.info("######################################req END###################################################");
        LOG.info("Req Processed in {} ms", (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok().build();
    }
}


  

