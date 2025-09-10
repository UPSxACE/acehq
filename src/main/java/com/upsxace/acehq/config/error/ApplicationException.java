package com.upsxace.acehq.config.error;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final int status;

    public ApplicationException(String message, int status) {
        super(message);
        this.status = status;
    }
}
