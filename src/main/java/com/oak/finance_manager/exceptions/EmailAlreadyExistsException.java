package com.oak.finance_manager.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado");
    }

    public EmailAlreadyExistsException(String message) {super(message);}
}
