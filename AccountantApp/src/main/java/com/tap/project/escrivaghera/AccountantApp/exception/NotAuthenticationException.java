package com.tap.project.escrivaghera.AccountantApp.exception;

public class NotAuthenticationException extends RuntimeException {

	public NotAuthenticationException() {
		super("Authentication missing");
	}
}
