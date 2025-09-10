package com.upsxace.acehq.config.error;

public class BadRequestException extends ApplicationException {
    public BadRequestException() {
        super("Bad request.", 400);
    }

    public BadRequestException(String message) {
        super(message, 400);
    }
}
