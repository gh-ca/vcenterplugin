package com.dmeplugin.dmestore.exception;


public class VcenterException extends DMEException {
    private String code;
    private String message;

    public VcenterException() {
        super();
    }

    public VcenterException(String message) {
        super(message);
        this.message = message;
    }

    public VcenterException(String code, String message) {
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
