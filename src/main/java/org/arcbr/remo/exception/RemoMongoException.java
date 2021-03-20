package org.arcbr.remo.exception;

public class RemoMongoException extends RemoException{
    public RemoMongoException(String message, int code, String type) {
        super(message, code, type);
    }
}
