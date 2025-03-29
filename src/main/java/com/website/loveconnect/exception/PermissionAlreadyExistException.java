package com.website.loveconnect.exception;

public class PermissionAlreadyExistException extends RuntimeException{
    public PermissionAlreadyExistException(String message){
        super(message);
    }
}
