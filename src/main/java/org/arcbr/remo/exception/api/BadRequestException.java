package org.arcbr.remo.exception.api;


import org.arcbr.remo.exception.RemoException;

public class BadRequestException extends RemoException {
    public BadRequestException(String message, String type) {
        super(message, 400, type);
    }
}
