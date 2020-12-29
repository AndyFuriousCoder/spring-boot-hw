package org.example.app.exceptions;

public class IncorrectFilterException extends Exception{

    private final static String MESSAGE = "Filter value is empty. Type desired value to appropriate field.";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
