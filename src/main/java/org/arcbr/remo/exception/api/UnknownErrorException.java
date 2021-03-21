package org.arcbr.remo.exception.api;

import org.arcbr.remo.exception.RemoException;

public class UnknownErrorException extends RemoException {
    public UnknownErrorException(String message) {
        super(message, 500, "UNKNOWN_ERROR");
    }
}
