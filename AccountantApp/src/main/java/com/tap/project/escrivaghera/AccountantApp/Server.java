package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.List;

public class Server {
	private List<Session> sessionList;
	private int currentSession;
	
	public Server(){
		sessionList=new ArrayList<Session>();
		currentSession=0;
	}
	
	public Session getSession(User user) {
		currentSession++;
		Session session =new Session(currentSession, user);
		sessionList.add(session);
		return session;
	}

	public int getListSize() {
		// TODO Auto-generated method stub
		return sessionList.size();
	}

}
