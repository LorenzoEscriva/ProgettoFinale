package com.tap.project.escrivaghera.AccountantApp;

/**
 * An instance of this class contains the information of user that can use this app
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class User {

	private String id;
	private String name;
	private String surname;
	
	/**
	 * Create a new instance of this class
	 * 
	 * @param associated user's id
	 * @param associated user's name
	 * @param associated user's surname
	 */

	public User(String id, String name, String surname) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	/**
	 * This method creates a description of the current object
	 * 
	 * @return the object's description
	 */
	@Override
	public String toString() {
		return id + " " + name + " " + surname;
	}
}
