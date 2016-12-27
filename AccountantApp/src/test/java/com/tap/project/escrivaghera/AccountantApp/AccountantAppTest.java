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
		db = mock(Database.class);
		Server serverTest = new Server(db);
		myAccountantApp = new AccountantApp(serverTest);
		user = new User("1", "test", "test");
	}

	@Test
	public void testAuthenticatedMethod() {
		myAccountantApp.authenticate(user);
		assertNotNull(myAccountantApp.getMySession());
	}

	@Test(expected = NotAuthenticationException.class)
	public void testCreateJournalEntryWithoutAuthentication() {
		myAccountantApp.createJournalEntry("1", new Date(), new ArrayList<Count>());
	}

	@Test
	public void testCreateJournalEntryWithBadListOfCounts() {
		myAccountantApp.authenticate(user);
		ArrayList<Count> myCounts = createTestList(1200.0, 1100.0);
		assertNull(myAccountantApp.createJournalEntry("1", new Date(), myCounts));
	}

	@Test
	public void testCreateJournalEntryWithGoodListOfCounts() {
		myAccountantApp.authenticate(user);
		ArrayList<Count> myCounts = createTestList(1200.0, 1200.0);
		assertNotNull(myAccountantApp.createJournalEntry("1", new Date(), myCounts));
	}

	@Test(expected = NotAuthenticationException.class)
	public void testAddJournalEntryWithoutAuthentication() {
		myAccountantApp.add(new JournalEntry("1", new Date()));
	}

	@Test(expected = NotAuthenticationException.class)
	public void testModifyJournalEntryWithoutAuthentication() {
		myAccountantApp.modify("1", new JournalEntry("2", new Date()));
	}

	@Test(expected = NotAuthenticationException.class)
	public void testDeleteJournalEntryWithoutAuthentication() {
		myAccountantApp.delete(null);
	}

	@Test(expected = NotAuthenticationException.class)
	public void testGetAllRegistrationJournalEntryWithoutAuthentication() {
		myAccountantApp.getAllRegistration(new Date(), new Date());
	}

	@Test
	public void testGetAllRegistrationInteractionWithDB() {
		myAccountantApp.authenticate(user);
		Date date1=new Date(), date2=new Date();
		myAccountantApp.getAllRegistration(date1, date2);
		verify(db).getAllRegistration(date1, date2);
	}
	
	

	public ArrayList<Count> createTestList(double leftValue, double rightValue) {
		ArrayList<Count> myCounts = new ArrayList<Count>();
		Count count = new Count("count1", true);
		count.setValue(leftValue);
		myCounts.add(count);
		count = new Count("count2", false);
		count.setValue(rightValue);
		myCounts.add(count);
		return myCounts;
	}

}
