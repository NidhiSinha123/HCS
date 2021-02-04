package com.cg.healthcaresystemrest.exception;
/*
 * Author: 		Jayesh Gaur
 * Description: This exception is thrown when a validation criteria is failed.
 * Created on:	 October 11, 2019
 */
public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {

	}

	public ValidationException(String string) {
		super(string);
	}

}