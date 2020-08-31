package com.dmeplugin.dmestore.exception;

public class DataBaseException extends VcenterException {

	public DataBaseException() {
		super();
	}

	public DataBaseException(String message) {

		super(message);

		setCode("-70001");
	}

	public DataBaseException(String code, String message) {

		super(message);

		setCode(code);
	}

}
