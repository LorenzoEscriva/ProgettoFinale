package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class AccountantAppTest {
	private AccountantApp myAccountantApp;
	private Database db;
	private User user;

	@Before
	public void setUp() throws Exception {
		db=mock(Database.class);
		Server serverTest=new Server(db);
		myAccountantApp=new AccountantApp(serverTest);
		user = new User("1", "test", "test");
	}

	@Test
	public void testAuthenticatedMethod() {
		myAccountantApp.authenticate(user);
		assertNotNull(myAccountantApp.getMySession());
	}
	
	@Test(expected=NotAuthenticationException.class)
	public void testCreateJournalEntryWithoutAuthentication(){
		myAccountantApp.createJournalEntry("1", new Date(), new ArrayList<Count>());
	}
	
	@Test
	public void testCreateJournalEntryWithBadListOfCounts(){
		myAccountantApp.authenticate(user);
		ArrayList<Count> myCounts = createTestList(1200.0, 1100.0);
		assertNull(myAccountantApp.createJournalEntry("1", new Date(), myCounts));
	}
	
	@Test
	public void testCreateJournalEntryWithGoodListOfCounts(){
		myAccountantApp.authenticate(user);
		ArrayList<Count> myCounts=createTestList(1200.0, 1200.0);
		assertNotNull(myAccountantApp.createJournalEntry("1", new Date(), myCounts));
	}

	public ArrayList<Count> createTestList(double leftValue, double rightValue) {
		ArrayList<Count> myCounts=new ArrayList<Count>();
		Count count=new Count("count1", true);
		count.setValue(leftValue);
		myCounts.add(count);
		count=new Count("count2", false);
		count.setValue(rightValue);
		myCounts.add(count);
		return myCounts;
	}
	
	
}
