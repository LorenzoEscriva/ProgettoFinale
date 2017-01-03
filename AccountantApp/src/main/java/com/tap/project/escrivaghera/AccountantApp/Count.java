package com.tap.project.escrivaghera.AccountantApp;

/**
 * An instance of this class contains the information associated at one count
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class Count {
	private String description;
	private boolean isLeft;
	private double value;
	
	/**
	 * Creates a new Count's object
	 * @param description of count
	 * @param nature of this count
	 */
	public Count(String description, boolean isLeft) {
		this.description = description;
		this.isLeft = isLeft;
	}
	
	/**
	 * Changes the value associated
	 * @param value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * Gets the count's nature
	 * @return the type of current count
	 */
	public boolean isLeft() {
		return isLeft;
	}
	
	/**
	 * Get's the count's value
	 * @return the current value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Get's the count's description
	 * @return the current description
	 */
	public String getDescrizione() {
		return description;
	}
}
