package com.dmeplugin.dmestore.exception;

/**
 * Created by hyuan on 2017/6/9.
 */
public class NoDMEException extends IgnorableException {
    public NoDMEException() {
        super("-90002", "no esight in DB");
    }
}
