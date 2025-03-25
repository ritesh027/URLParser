package com.ritesh.demo.htmlfilewoker.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ritesh.demo.htmlfilewoker.client.URLFeederService;
import com.ritesh.demo.htmlfilewoker.model.URL;

import retrofit2.Response;
import retrofit2.Retrofit;

@Service
public class URLProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(URLProcessor.class);

    @Autowired
    private Retrofit retrofit;

    // aysnc bcz it is making a network call to another serivce and it is blocking in nature.
    @Async
    public void process(String url){
        try{
            Set<URL> urls = new HashSet<>();
            Document Doc = Jsoup.connect(url).get();
            Elements links = Doc.select("a[href]");
            for(Element link: links){
                String path = link.attr("href");
                LOG.info("URL: {}", path);
                urls.add(URL.builder().url(path).build());
            }

            Response<Void> response =  retrofit.create(URLFeederService.class).batchPublish(urls).execute();
            if(!response.isSuccessful()){
                LOG.error("Retrofit call failed, with response code {}", response.code());
            }
        } catch (IOException ex){
            LOG.error("Exception, {}", ex);
        }
    }
    
}
