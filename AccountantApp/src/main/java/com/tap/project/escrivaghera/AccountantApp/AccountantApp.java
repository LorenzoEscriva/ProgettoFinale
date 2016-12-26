package com.tap.project.escrivaghera.AccountantApp;

public class AccountantApp {
	private Session mySession;
	private Database myDB;
	private Server myServer;
	
	public AccountantApp(Server server){
		this.myServer=server;
		this.myDB=myServer.getDb();
		mySession=null;
	}

}
