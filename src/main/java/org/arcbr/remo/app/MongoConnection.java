package org.arcbr.remo.app;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConnection  {

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(@Value("${remo.mongodb.uri}") String uri){
        ConnectionString connectionString = new ConnectionString(uri);
        return new ReactiveMongoTemplate(MongoClients.create(connectionString), connectionString.getDatabase());
    }

}
