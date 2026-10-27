package com.trifler.trifler_chat_application.exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String msg){
        super(msg);
    }
}
