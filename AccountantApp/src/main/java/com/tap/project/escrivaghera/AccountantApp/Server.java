package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Server {
	private List<Session> sessionList;
	private int currentSession;
	private Database db;
	
	public Server(Database database){
		sessionList=new ArrayList<Session>();
		currentSession=0;
		db=database;
	}
	
	public Session getSession(User user) {
		currentSession++;
		Session session =new Session(currentSession, user);
		sessionList.add(session);
		return session;
	}

	public List<Session> getList() {
		return sessionList;
	}

	public Database getDb() {
		return db;
	}

	public String getSessionsDescription() {
		String description="They have made this anctions on the database:\n";
		Iterator<Session> sessionit=sessionList.iterator();
		while(sessionit.hasNext()){
			description=description+sessionit.next().toString()+"\n";
		}
		return description;
	}
}