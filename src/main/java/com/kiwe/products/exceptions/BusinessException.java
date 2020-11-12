package com.kiwe.products.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String details;
	private String message;

	public BusinessException(String message) {
		super(message);
		this.message = message;
	}

	public BusinessException(String message, String details) {
		super(message);
		this.message = message;
		this.details = details;
	}

}
