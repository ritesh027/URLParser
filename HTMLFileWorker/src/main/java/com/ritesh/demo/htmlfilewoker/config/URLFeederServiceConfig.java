package com.ritesh.demo.htmlfilewoker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class URLFeederServiceConfig {

    @Value("${com.ritesh.demo.html-worker.urlfeederservice.baseurl}")
    String baseUrl;
    
    @Bean
    public Retrofit getClient(){

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();
    }
}
