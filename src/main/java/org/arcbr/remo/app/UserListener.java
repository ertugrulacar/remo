package org.arcbr.remo.app;

import org.arcbr.remo.db.mongo.RemoChangeStreamEvent;
import org.arcbr.remo.ex.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class UserListener {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @PostConstruct
    public void eto(){
        RemoChangeStreamEvent event = applicationContext.getBean(RemoChangeStreamEvent.class);
        event.run("user", User.class);

        User user = new User().setName("Eto").setLastName("Acar").setList(Arrays.asList(21,424,4));
        User user1 = new User().setName("Berkay").setLastName("Cetinlaya").setList(Arrays.asList(2, 4));
        User user2 = new User().setName("Kado").setLastName("Aydemir").setList(Arrays.asList(223,56, 24));

//        reactiveMongoTemplate.insert(user).subscribe();
//        reactiveMongoTemplate.insert(user1).subscribe();
//        reactiveMongoTemplate.insert(user2).subscribe();
//        user.setId("60563b85e777d951475c1577");
//
//        Query query = new Query(Criteria.where("_id").is("60563b85e777d951475c1577"));
//        Update update = new Update().set("lastName", "Acarkekekek");
//        reactiveMongoTemplate.upsert(query, update, "user").subscribe();


    }

}