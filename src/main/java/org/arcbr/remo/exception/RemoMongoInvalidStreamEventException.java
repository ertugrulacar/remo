package org.arcbr.remo.exception;

public class RemoMongoInvalidStreamEventException extends RemoMongoException{
    public RemoMongoInvalidStreamEventException(String message) {
        super(message, 1200500, "INVALID_STREAM_EVENT");
    }
    public RemoMongoInvalidStreamEventException(String message, String type) {
        super(message, 1200500, type);
    }
}
