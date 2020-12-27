package org.example.app.exceptions;

public class EmptyFileException extends Exception {

    private final static String MESSAGE = "select file to upload";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
