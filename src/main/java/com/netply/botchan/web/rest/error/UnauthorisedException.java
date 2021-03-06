package com.netply.botchan.web.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorisedException extends RuntimeException {
    public UnauthorisedException() {
    }

    public UnauthorisedException(String message) {
        super(message);
    }
}
