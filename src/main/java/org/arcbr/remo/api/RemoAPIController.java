package org.arcbr.remo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemoAPIController {

    @Autowired
    private RemoAPIService service;

    @GetMapping(value = "{collectionName}/{objectId}", produces = "application/json")
    public ResponseEntity<?> get(@PathVariable("collectionName") String collectionName, @PathVariable("objectId") String objectId){
        return service.get(collectionName, objectId);
    }

}
