package com.tap.project.escrivaghera.AccountantApp;

public class User {

	private String id;
	private String name;
	private String surname;

	public User(String id, String name, String surname) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public String toString() {
		return id + " " + name + " " + surname;
	}
}
