package com.oak.finance_manager.exceptions.auth;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("This user is not authorized");
    }

    public UnauthorizedException(String message) {super(message);}
}

