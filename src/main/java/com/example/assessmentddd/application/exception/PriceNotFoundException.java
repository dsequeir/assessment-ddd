package com.example.assessmentddd.application.exception;

public class PriceNotFoundException extends RuntimeException{
    public PriceNotFoundException(String message) {
        super(message);
    }

}
