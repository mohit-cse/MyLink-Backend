package com.mohit.userservice.Utils.Exceptions;

public class NoSuchTokenException extends RuntimeException{
    public NoSuchTokenException(String message){
        super(message);
    }
}
