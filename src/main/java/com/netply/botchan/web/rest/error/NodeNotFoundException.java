package com.netply.botchan.web.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NodeNotFoundException extends Exception {
    public NodeNotFoundException() {
        this("No such node found");
    }

    public NodeNotFoundException(String s) {
        super(s);
    }
}
