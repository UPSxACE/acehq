package com.upsxace.acehq.config.error;

public class ForbiddenException extends ApplicationException{
    public ForbiddenException() {
        super("Forbidden.", 403);
    }

    public ForbiddenException(String message) {
        super(message, 403);
    }
}
