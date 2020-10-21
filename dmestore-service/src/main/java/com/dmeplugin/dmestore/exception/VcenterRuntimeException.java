package com.dmeplugin.dmestore.exception;


public class VcenterRuntimeException extends RuntimeException {
    private String code;
    private String message;

    public VcenterRuntimeException() {
        super();
    }

    public VcenterRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    public VcenterRuntimeException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
