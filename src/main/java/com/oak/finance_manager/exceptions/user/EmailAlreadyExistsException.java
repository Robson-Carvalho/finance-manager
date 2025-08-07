package com.oak.finance_manager.exceptions.user;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("E-mail already exists");
    }

    public EmailAlreadyExistsException(String message) {super(message);}
}
