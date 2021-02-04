package com.cg.healthcaresystemrest.exception;
/*
 * Author: 		Jayesh Gaur
 * Description: This exception is thrown when user enters a value like email	
 * 					or phone number which already exists in the database and it cannot
 * 					be unique
 * Created on:	 October 11, 2019
 */
public class ExistingCredentialException extends Exception {

	private static final long serialVersionUID = 1L;

	//Default constructor
	public ExistingCredentialException() {
	}
	
	public ExistingCredentialException(String exception) {
		super(exception);
	}
	
}
