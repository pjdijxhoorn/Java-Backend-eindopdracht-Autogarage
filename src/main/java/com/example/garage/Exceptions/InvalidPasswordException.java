package com.example.garage.Exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super();
    }
    public InvalidPasswordException(String message) {
        super(message);
}
}
