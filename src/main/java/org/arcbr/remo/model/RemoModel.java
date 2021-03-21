package org.arcbr.remo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class RemoModel implements Serializable {
    @Id
    private String id;


    public String getId() {
        return id;
    }

    public RemoModel setId(String id) {
        this.id = id;
        return this;
    }
}
