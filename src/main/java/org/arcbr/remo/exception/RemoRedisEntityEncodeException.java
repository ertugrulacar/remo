package org.arcbr.remo.exception;

public class RemoRedisEntityEncodeException extends RemoRedisException{
    public RemoRedisEntityEncodeException(String message) {
        super(message, 1500980, "ENTITY_ENCODE");
    }
}
