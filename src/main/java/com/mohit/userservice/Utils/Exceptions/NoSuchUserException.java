package com.mohit.userservice.Utils.Exceptions;

public class NoSuchUserException extends RuntimeException{
    public NoSuchUserException(String message){
        super(message);
    }
}
