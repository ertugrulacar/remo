package org.arcbr.remo.app;

import org.arcbr.remo.db.mongo.RemoChangeStreamEvent;
import org.arcbr.remo.ex.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class UserListener {

    @Autowired
    private ApplicationContext applicationContext;


    @PostConstruct
    public void eto(){
        RemoChangeStreamEvent event = applicationContext.getBean(RemoChangeStreamEvent.class);
        event.run("user", User.class);
    }

}
