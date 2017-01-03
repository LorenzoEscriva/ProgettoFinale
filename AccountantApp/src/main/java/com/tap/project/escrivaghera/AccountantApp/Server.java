package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class allows to the user the database access and assigns he a new Session's object
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class Server {

	private List<Session> sessionList;
	private int currentSession;
	private Database db;

	/**
	 * Create a new Server's instance with a particular database instance associated
	 * 
	 * @param database
	 */
	public Server(Database database) {
		sessionList = new ArrayList<>();
		currentSession = 0;
		db = database;
	}

	/**
	 * Gets a Session's instance associated to the user represents by the user
	 * 
	 * @param a particular instance of user, the Session instance creates will be associated to it
	 * @return a new Session's object created
	 */
	public Session getSession(User user) {
		currentSession++;
		Session session = new Session(currentSession, user);
		sessionList.add(session);
		return session;
	}
	
	/**
	 * Gets the list of Session
	 * 
	 * @return the Session's list of current object
	 */
	public List<Session> getList() {
		return sessionList;
	}

	/**
	 * Get the database instance
	 * 
	 * @return the database associated at this object 
	 */
	public Database getDb() {
		return db;
	}

	/**
	 * Gets the description of all actions 
	 * 
	 * @return description of all actions
	 */
	public String getSessionsDescription() {
		String description = "They have made this anctions on the database:\n";
		Iterator<Session> sessionit = sessionList.iterator();
		while (sessionit.hasNext()) {
			description = description + sessionit.next().toString() + "\n";
		}
		return description;
	}
}
