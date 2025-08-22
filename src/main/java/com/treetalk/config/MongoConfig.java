package com.treetalk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.treetalk.repository")
public class MongoConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public MongoTemplate mongoTemplate(com.mongodb.client.MongoClient mongoClient, 
                                     MongoMappingContext context) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "treetalk");
        
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        converter.setMapKeyDotReplacement("_");
        
        return mongoTemplate;
    }
}