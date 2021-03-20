package org.arcbr.remo.exception;

public class RemoRedisEntityDecodeException extends RemoRedisException{
    public RemoRedisEntityDecodeException(String message) {
        super(message, 1500880, "ENTITY_DECODE");
    }
}
