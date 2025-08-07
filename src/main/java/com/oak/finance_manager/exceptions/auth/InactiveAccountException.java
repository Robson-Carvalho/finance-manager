package com.oak.finance_manager.exceptions.auth;

public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException() {
        super("Account inactive.");
    }

    public InactiveAccountException(String message) {super(message);}
}
