package ru.clevertec.check.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException() {
        super("BAD REQUEST");
    }
    public BadRequestException(String message) {
        super(message);
    }
}
