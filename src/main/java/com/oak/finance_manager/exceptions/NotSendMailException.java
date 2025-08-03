package com.oak.finance_manager.exceptions;

public class NotSendMailException extends RuntimeException {

    public NotSendMailException() {super("Mail not sent");}

    public NotSendMailException(String message) {
        super(message);
    }
}
