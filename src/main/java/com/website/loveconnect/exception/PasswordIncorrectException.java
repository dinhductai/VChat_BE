package com.website.loveconnect.exception;

public class PasswordIncorrectException extends RuntimeException{
    public PasswordIncorrectException(String message){
        super(message);
    }
}
