package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class AccountantApp {
	private Session mySession;
	private Database myDB;
	private Server myServer;
	private final static Logger LOGGER = Logger.getLogger(AccountantApp.class);
	
	public AccountantApp(Server server){
		this.myServer=server;
		this.myDB=myServer.getDb();
		mySession=null;
	}

	public Session getMySession() {
		return mySession;
	}

	public void authenticate(User anUser) {
		mySession=myServer.getSession(anUser);
	}

	public JournalEntry createJournalEntry(String id, Date date, List<Count> listOfCounts) {
		if(mySession==null)
			throw new NotAuthenticationException();
		
		JournalEntry newJournalEntry=new JournalEntry(id, date);
		try {
			newJournalEntry.setListOfCount(listOfCounts);
		} catch (IllegalJournalEntryException e) {
			LOGGER.error("Exception happen!", e);
			return null;
		}
		return newJournalEntry;
	}

}
