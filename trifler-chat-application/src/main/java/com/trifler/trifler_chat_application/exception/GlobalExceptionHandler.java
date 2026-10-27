package com.trifler.trifler_chat_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String,String>> handleDuplicateResource(DuplicateResourceException e){
        Map<String,String> map=new HashMap<>();

        map.put("Message: ",e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentException(MethodArgumentNotValidException e){
        Map<String,String> map=new HashMap<>();

        List<FieldError> fieldErrors=e.getFieldErrors();

        for(FieldError error:fieldErrors) {
            map.put(error.getField(),error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String,String>> invalidCredentialException(InvalidCredentialException e){
        Map<String,String> map=new HashMap<>();
        map.put("message",e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}
