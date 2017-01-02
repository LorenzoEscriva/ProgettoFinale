package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tap.project.escrivaghera.AccountantApp.exception.*;

public class AccountantApp {

	private Session mySession;
	private Database myDB;
	private Server myServer;
	private static final Logger LOGGER = Logger.getLogger(AccountantApp.class);

	public AccountantApp(Server server) {
		this.myServer = server;
		this.myDB = myServer.getDb();
		mySession = null;
	}

	public Session getMySession() {
		return mySession;
	}

	public void authenticate(User anUser) {
		mySession = myServer.getSession(anUser);
	}

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

	public void add(JournalEntry newEntry) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.add(newEntry);
		mySession.addDescription("adds the journal entry with id " + newEntry.getId());
	}

	public void modify(String id, JournalEntry changeEntry) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.modify(id, changeEntry);
		mySession.addDescription("changes the journal entry with id " + id);
	}

	public void delete(String id) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.delete(id);
		mySession.addDescription("deletes the journal entry with id " + id);
	}

	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		if (mySession == null)
			throw new NotAuthenticationException();
		mySession.addDescription("takes all journal entry between " + date1.toString() + " and " + date2.toString());
		return myDB.getAllRegistration(date1, date2);
	}

}
