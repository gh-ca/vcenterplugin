package com.dmeplugin.dmestore.exception;


public class IgnorableException extends VcenterException {
    public IgnorableException() {
        super();
    }

    public IgnorableException(String message) {
        super(message);
    }

    public IgnorableException(String code, String message) {
        super(code, message);
    }
}
