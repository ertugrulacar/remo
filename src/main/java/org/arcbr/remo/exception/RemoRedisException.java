package org.arcbr.remo.exception;

public class RemoRedisException extends RemoException{
    public RemoRedisException(String message, int code, String type) {
        super(message, code, type);
    }
}
