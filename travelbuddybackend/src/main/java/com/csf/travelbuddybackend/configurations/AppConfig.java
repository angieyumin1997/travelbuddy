package com.csf.travelbuddybackend.configurations;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {

    private Logger logger = Logger.getLogger(AppConfig.class.getName());

    @Value("${cors.pathMapping}")
    String pathMapping;

    @Value("${cors.origins}")
    String origins;

    @Value("${mongo.url}")
    private String connectionString;

    private MongoClient client = null;

    @Bean
    public MongoClient mongoClient(){
        if(null == client)
            client = MongoClients.create(connectionString);
        return client;
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(),"test");
    }
    
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        logger.info("pathMapping = %s , origins = %s".formatted(pathMapping,origins));
        return new CORSConfigurations(pathMapping,origins);
    }
}
