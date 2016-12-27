package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class AccountantApp {

	private Session mySession;
	private Database myDB;
	private Server myServer;
	private final static Logger LOGGER = Logger.getLogger(AccountantApp.class);

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
			LOGGER.error("Exception happen!", e);
			return null;
		}
		return newJournalEntry;
	}

	public void add(JournalEntry newEntry) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.add(newEntry);
		String addJournalEntry = new String("Adds the journal entry with id " + newEntry.getId());
		LOGGER.info(addJournalEntry);
	}

	public void modify(String id, JournalEntry changeEntry) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.modify(id, changeEntry);
		String modifyJournalEntry = new String("Changes the journal entry with id " + id);
		LOGGER.info(modifyJournalEntry);
	}

	public void delete(String id) {
		if (mySession == null)
			throw new NotAuthenticationException();
		myDB.delete(id);
		String deleteJournalEntry = new String("Deletes the journal entry with id " + id);
		LOGGER.info(deleteJournalEntry);
	}

	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		if (mySession == null)
			throw new NotAuthenticationException();
		String getListJournalEntry = new String(
				"Takes all journal entry between " + date1.toString() + " and " + date2.toString());
		LOGGER.info(getListJournalEntry);
		return myDB.getAllRegistration(date1, date2);
	}

}
