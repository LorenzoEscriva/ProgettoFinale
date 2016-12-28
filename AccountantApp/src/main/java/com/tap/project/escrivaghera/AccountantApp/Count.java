package com.tap.project.escrivaghera.AccountantApp;

public class Count {

	private String description;
	private boolean isLeft;
	private double value;

	public Count(String description, boolean isLeft) {
		this.description = description;
		this.isLeft = isLeft;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public double getValue() {
		return value;
	}
}
