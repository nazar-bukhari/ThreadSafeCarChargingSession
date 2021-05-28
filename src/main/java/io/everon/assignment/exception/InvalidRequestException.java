package io.everon.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid id provided or stopped already")
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String errorMessage){
        super(errorMessage);
    }
}
