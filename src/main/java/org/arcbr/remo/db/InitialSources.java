package org.arcbr.remo.db;

import org.arcbr.remo.db.mongo.RemoChangeStreamEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InitialSources {

    public Map<String, RemoChangeStreamEvent>  events;

    public InitialSources() {
        events = new HashMap<>();
    }

    public void setOutputClass(String collection, RemoChangeStreamEvent event){
        events.put(collection, event);
    }
    public RemoChangeStreamEvent getOutputClass(String collection){
        return events.get(collection);
    }
}
