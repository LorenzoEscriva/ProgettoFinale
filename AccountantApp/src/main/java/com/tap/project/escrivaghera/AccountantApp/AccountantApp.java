package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountantApp {
	private Session mySession;
	private Database myDB;
	private Server myServer;
	
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

	public JournalEntry createJournalEntry(String string, Date date, List<Count> arrayList) {
		if(mySession==null)
			throw new NotAuthenticationException();
		return null;
	}

}
