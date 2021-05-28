package io.everon.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SessionNotFoundException extends RuntimeException{

    public SessionNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
