package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;
import com.tap.project.escrivaghera.AccountantApp.exception.NotAuthenticationException;
import com.tap.project.escrivaghera.AccountantApp.helper.GenericHelper;

public class AccountantAppTest {

	private AccountantApp myAccountantApp;
	private Database db;
	private User user;
	private int lengthOfUserActions;
	private GenericHelper myGenericHelper;

	@Before
	public void setUp() throws Exception {
		db = mock(Database.class);
		Server serverTest = new Server(db);
		myAccountantApp = new AccountantApp(serverTest);
		user = new User("1", "test", "test");
		myGenericHelper = new GenericHelper();
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
		ArrayList<Count> myCounts = myGenericHelper.createTestList(1200.0, 1100.0);
		assertNull(myAccountantApp.createJournalEntry("1", new Date(), myCounts));
	}

	@Test
	public void testCreateJournalEntryWithGoodListOfCounts() {
		myAccountantApp.authenticate(user);
		ArrayList<Count> myCounts = myGenericHelper.createTestList(1200.0, 1200.0);
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
		autheticationAndSettingLength();
		Date date1 = new Date(), date2 = new Date();
		myAccountantApp.getAllRegistration(date1, date2);
		assertEquals(lengthOfUserActions + 1, myAccountantApp.getMySession().getList().size());
		verify(db).getAllRegistration(date1, date2);
	}

	@Test
	public void testGetAllRegistractionWithOneElement() throws IllegalJournalEntryException {
		Date[] dates = authenticateAndCreateDates();
		List<JournalEntry> returnList = new ArrayList<JournalEntry>();
		createJournalEntry(returnList);
		when(db.getAllRegistration(dates[0], dates[1])).thenReturn(returnList);
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistractionWithMoreElement() throws IllegalJournalEntryException {
		Date[] dates = authenticateAndCreateDates();
		List<JournalEntry> returnList = new ArrayList<JournalEntry>();
		createJournalEntry(returnList);
		createJournalEntry(returnList);
		when(db.getAllRegistration(dates[0], dates[1])).thenReturn(returnList);
		assertEquals(2, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testAddInteractionWithDB() {
		autheticationAndSettingLength();
		JournalEntry test = new JournalEntry("1",
				new Date(new GregorianCalendar(1910 + 100, 11, 10).getTimeInMillis()));
		myAccountantApp.add(test);
		assertEquals(lengthOfUserActions + 1, myAccountantApp.getMySession().getList().size());
		verify(db).add(test);
	}

	@Test
	public void testModifyInteractionWithDB() {
		autheticationAndSettingLength();
		JournalEntry test = new JournalEntry("1",
				new Date(new GregorianCalendar(1910 + 100, 11, 10).getTimeInMillis()));
		myAccountantApp.modify("2", test);
		assertEquals(lengthOfUserActions + 1, myAccountantApp.getMySession().getList().size());
		verify(db).modify("2", test);
	}

	@Test
	public void testDeleteInteractionWithDB() {
		autheticationAndSettingLength();
		myAccountantApp.delete("1");
		assertEquals(lengthOfUserActions + 1, myAccountantApp.getMySession().getList().size());
		verify(db).delete("1");
	}

	private void autheticationAndSettingLength() {
		myAccountantApp.authenticate(user);
		lengthOfUserActions = myAccountantApp.getMySession().getList().size();
	}

	private Date[] authenticateAndCreateDates() {
		myAccountantApp.authenticate(user);
		Date[] dates = myGenericHelper.createDates();
		return dates;
	}

	private void createJournalEntry(List<JournalEntry> returnList) throws IllegalJournalEntryException {
		JournalEntry entry = new JournalEntry("1",
				new Date(new GregorianCalendar(1900 + 116, 11, 10).getTimeInMillis()));
		entry.setListOfCount(myGenericHelper.createTestList(1200.0, 1200.0));
		returnList.add(entry);
	}

}
