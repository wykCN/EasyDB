package com.wyk.esaydb.exception;

public class UniqueNotFoundException extends Exception {

	private static final long serialVersionUID = 6694784467470294981L;

	public UniqueNotFoundException() {
		super("未找到实体的唯一索引");
	}

	public UniqueNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UniqueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniqueNotFoundException(String message) {
		super(message);
	}

	public UniqueNotFoundException(Throwable cause) {
		super(cause);
	}

	
}
