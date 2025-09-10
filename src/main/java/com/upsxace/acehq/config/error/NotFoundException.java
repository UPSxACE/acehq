package com.upsxace.acehq.config.error;

public class NotFoundException extends ApplicationException{
    public NotFoundException() {
        super("Not found.", 404);
    }

    public NotFoundException(String message) {
        super(message, 404);
    }
}
