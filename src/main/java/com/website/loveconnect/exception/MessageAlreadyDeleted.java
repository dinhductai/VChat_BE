package com.website.loveconnect.exception;

public class MessageAlreadyDeleted extends RuntimeException{
    public MessageAlreadyDeleted(String message) {
        super(message);
    }
}
