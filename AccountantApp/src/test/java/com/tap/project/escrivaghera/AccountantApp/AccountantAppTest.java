package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AccountantAppTest {
	private AccountantApp myAccountantApp;
	private Database db;

	@Before
	public void setUp() throws Exception {
		db=mock(Database.class);
		Server serverTest=new Server(db);
		myAccountantApp=new AccountantApp(serverTest);
	}

	@Test
	public void testAuthenticatedMethod() {
		User user =new User("1", "test", "test");
		myAccountantApp.authenticate(user);
		assertNotNull(myAccountantApp.getMySession());
	}
	
	@Test(expected=NotAuthenticationException.class)
	public void testCreateJournalEntryWithoutAuthentication(){
		
	}
}
