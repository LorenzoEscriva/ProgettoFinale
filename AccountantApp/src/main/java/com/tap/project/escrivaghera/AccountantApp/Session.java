package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Session {
	private int id;
	private User user;
	private List<String> userActions; 
	private final static Logger LOGGER =Logger.getLogger(Session.class);

	public Session(int id, User user) {
		this.id=id;
		this.user=user;
		userActions=new ArrayList<String>();
	}

	public void addDescription(String action) {
		String actionDescription=new String("In the session "+ id+ ", the user "+ user.toString() +" "+ action);
		LOGGER.info(actionDescription);
		userActions.add(actionDescription);
	}

	public List<String> getList() {
		return userActions;
	}

}
