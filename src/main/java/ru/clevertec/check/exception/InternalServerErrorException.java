package ru.clevertec.check.exception;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException() {
        super("INTERNAL SERVER ERROR");
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
