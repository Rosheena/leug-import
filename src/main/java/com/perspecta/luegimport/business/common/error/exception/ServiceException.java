package com.perspecta.luegimport.business.common.error.exception;

public class ServiceException extends RuntimeException {
	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
