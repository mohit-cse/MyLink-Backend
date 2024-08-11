package com.mohit.docservice.Utils.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> noAttachment(MultipartException e) {
        e.printStackTrace();
        return new ResponseEntity<>("No attachment found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> noJwtToken(MissingRequestHeaderException e) {
        e.printStackTrace();
        if(e.getHeaderName().equals("Authorization"))
            return new ResponseEntity<>("Missing Auth Token", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>("Missing Headers", HttpStatus.BAD_REQUEST);
    }
}
