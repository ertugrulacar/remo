package org.arcbr.remo.exception;

public class RemoRedisEntityNotFoundException extends RemoRedisException{

    public RemoRedisEntityNotFoundException(String message) {
        super(message, 1500780, "NO_DATA");
    }
}
