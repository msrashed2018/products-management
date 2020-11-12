package com.kiwe.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public ResourceAlreadyExistException(String message) {
		super(message);
		this.message = message;
	}

}
