package com.tap.project.escrivaghera.AccountantApp;

public class NotAuthenticationException extends RuntimeException {

	public NotAuthenticationException() {
		super("Authentication missing");
	}
}
