package com.trifler.trifler_chat_application.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
