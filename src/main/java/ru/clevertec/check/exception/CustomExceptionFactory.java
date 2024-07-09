package ru.clevertec.check.exception;

public class CustomExceptionFactory {
    public static RuntimeException createException(CustomExceptionType type) {
        return switch (type) {
            case BAD_REQUEST -> new BadRequestException(type.getErrorMessage());
            case INTERNAL_SERVER_ERROR -> new InternalServerErrorException(type.getErrorMessage());
            case NOT_ENOUGH_MONEY -> new NotEnoughMoneyException(type.getErrorMessage());
            default -> throw new IllegalArgumentException("Unknown exception type: " + type);
        };
    }
    private static class InternalServerErrorException extends RuntimeException {
        public InternalServerErrorException(String message) {
            super(message);
        }
    }

    private static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    private static class NotEnoughMoneyException extends RuntimeException {
        public NotEnoughMoneyException(String message) {
            super(message);
        }
    }

}

