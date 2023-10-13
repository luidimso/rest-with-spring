package com.luidimso.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMathOperationExperation extends RuntimeException {
	public UnsupportedMathOperationExperation(String ex) {
		super(ex);
	}
	
	private static final long serialVersionUID = 1L;

}
