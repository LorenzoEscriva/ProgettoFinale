package com.tap.project.escrivaghera.AccountantApp.exception;

/**
 * This is an subclass of Exception's class. The methods throws this exception when receive a wrong 
 * list of Count's objects
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class IllegalJournalEntryException extends Exception {
	/**
	 * The constructor of this class
	 * @param error's description
	 */
	public IllegalJournalEntryException(String error) {
		super(error);
	}

}
