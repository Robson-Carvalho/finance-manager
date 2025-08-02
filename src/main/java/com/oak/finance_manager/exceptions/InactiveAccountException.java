package com.oak.finance_manager.exceptions;

public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException() {
        super("Account inactive.");
    }

    public InactiveAccountException(String message) {super(message);}
}
