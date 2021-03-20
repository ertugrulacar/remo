package org.arcbr.remo.exception;

public class RemoException extends RuntimeException{
    private final int code;
    private final String type;

    public RemoException(String message, int code, String type) {
        super(message);
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
