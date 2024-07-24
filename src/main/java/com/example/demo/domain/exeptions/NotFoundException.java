package com.example.demo.domain.exeptions;

public class NotFoundException extends RuntimeException{

    //Constructor creado con mensaje
    public NotFoundException(String message) {
        super(message);
    }
}
