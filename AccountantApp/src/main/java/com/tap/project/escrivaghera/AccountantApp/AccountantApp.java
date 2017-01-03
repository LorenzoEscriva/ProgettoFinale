package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tap.project.escrivaghera.AccountantApp.exception.*;

/**
 * This the main class. This class permits to interacts with other components, the 
 * authenticated user can create a new JournalEntry's object, add it to the database, 
 * modify or delete one already exists and get the list of all registrations. 
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class AccountantApp {

	private Session mySession;
	private Database myDB;
	private Server myServer;
	private static final Logger LOGGER = Logger.getLogger(AccountantApp.class);

	/**
	 * Creates a new object of AccountantApp
	 * @param the server associated at this object
	 */
	public AccountantApp(Server server) {
		this.myServer = server;
		this.myDB = myServer.getDb();
		mySession = null;
	}

	/**
	 * Gets the current session
	 * @return the current session
	 */
	public Session getMySession() {
		return mySession;
	}
	
	/**
	 * Permits an user to do the log in
	 * @param anUser
	 */
	public void authenticate(User anUser) {
		mySession = myServer.getSession(anUser);
	}
	/**
	 * Creates a new JournalEntry's object
	 * @param id of new  JournalEntry's object
	 * @param date of new JournalEntry's object
	 * @param list of counts associated at the new JournalEntry's object
	 * @return the JournalEntry created, null if the list throws an IllegalJournalEntryException
	 */
	public JournalEntry createJournalEntry(String id, Date date, List<Count> listOfCounts) {
		if (mySession == null)
			throw new NotAuthenticationException();
		JournalEntry newJournalEntry = new JournalEntry(id, date);
		try {
			newJournalEntry.setListOfCount(listOfCounts);
		} catch (IllegalJournalEntryException e) {
			LOGGER.error("EXCEPTION ", e);
			return null;
		}
		return newJournalEntry;
	}
	
	/**
	 * Adds a new JournalEntry to the database
	 * @param new JournalEntry object
	 */
	public void add(JournalEntry newEntry) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.add(newEntry);
		mySession.addDescription("adds the journal entry with id " + newEntry.getId());
	}
	/**
	 * Modifies the journal entries specified by id with the information contained in JournalEntry object
	 * @param id of the records to modify
	 * @param the JournalEntry object contains the information
	 */
	public void modify(String id, JournalEntry changeEntry) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.modify(id, changeEntry);
		mySession.addDescription("changes the journal entry with id " + id);
	}

	/**
	 * Deletes some records with id
	 * @param id of record to delete
	 */
	public void delete(String id) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.delete(id);
		mySession.addDescription("deletes the journal entry with id " + id);
	}
	/**
	 * Gets the list of registrations between two dates
	 * @param first date
	 * @param second date
	 * @return the list of all registrations
	 */
	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		if (mySession == null)
			throw new NotAuthenticationException();
		mySession.addDescription("takes all journal entry between " + date1.toString() + " and " + date2.toString());
		return myDB.getAllRegistration(date1, date2);
	}

}
