package com.tap.project.escrivaghera.AccountantApp;

public class Count {

	String description;
	boolean isLeft;
	double value;

	public Count(String description, boolean isLeft) {
		this.description = description;
		this.isLeft = isLeft;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
