package ru.clevertec.check.exception;

public enum CustomExceptionType {
    BAD_REQUEST("BAD REQUEST"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR"),
    NOT_ENOUGH_MONEY("NOT ENOUGH MONEY");

    private final String errorMessage;

    CustomExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
