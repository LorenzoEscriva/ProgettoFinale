package com.tap.project.escrivaghera.AccountantApp.exception;

/**
 * This is a subclass of RuntimeException's class. The methods throws this exception when they are called
 * and the user not are authenticated
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class NotAuthenticationException extends RuntimeException {
	/**
	 * The constructor of this class
	 * @param error's description
	 */
	public NotAuthenticationException() {
		super("Authentication missing");
	}
}
