package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class registers the user's actions on database
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class Session {

	private int id;
	private User user;
	private List<String> userActions;
	private static final Logger LOGGER = Logger.getLogger(Session.class);
	
	/**
	 * Create a new Session class' instance with an associated user
	 * 
	 * @param id
	 * @param user
	 */
	public Session(int id, User user) {
		this.id = id;
		this.user = user;
		userActions = new ArrayList<>();
	}
	/**
	 * This method add a new action to the list
	 * 
	 * @param action's description
	 */
	public void addDescription(String action) {
		String actionDescription = new String("In the session " + id + ", the user " + user.toString() + " " + action);
		LOGGER.info(actionDescription);
		userActions.add(actionDescription);
	}
	
	
	/**
	 * Gets the list of actions' description
	 * 
	 * @return
	 */
	public List<String> getList() {
		return userActions;
	}

	/**
	 * The method creates a description of current object
	 * 
	 *  @return current object's description
	 */
	@Override
	public String toString() {
		String sessionDescription = "The user " + user.toString() + " makes this actions:\n";
		Iterator<String> actionit = userActions.iterator();
		while (actionit.hasNext()) {
			sessionDescription = sessionDescription + actionit.next() + "\n";
		}
		return sessionDescription;
	}
	/**
	 * Gets id of this Session
	 * 
	 * @return Session's id
	 */
	public int getSessionId() {
		return id;
	}

}
