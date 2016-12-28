package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.tap.project.escrivaghera.AccountantApp.exception.*;

import org.junit.Before;
import org.junit.Test;

public class AccountantAppTest {

	private AccountantApp myAccountantApp;
	private Database db;
	private User user;
	private int lengthOfUserActions;

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
		autheticationAndSettingLength();
		Date date1 = new Date(), date2 = new Date();
		myAccountantApp.getAllRegistration(date1, date2);
		assertEquals(lengthOfUserActions+1, myAccountantApp.getMySession().getList().size());
		verify(db).getAllRegistration(date1, date2);
	}

	private void autheticationAndSettingLength() {
		myAccountantApp.authenticate(user);
		lengthOfUserActions = myAccountantApp.getMySession().getList().size();
	}

	@Test
	public void testGetAllRegistractionWithOneElement() throws IllegalJournalEntryException {
		myAccountantApp.authenticate(user);
		Date[] dates = createDates();
		List<JournalEntry> returnList = new ArrayList<JournalEntry>();
		createJournalEntry(returnList);
		when(db.getAllRegistration(dates[0], dates[1])).thenReturn(returnList);
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistractionWithMoreElement() throws IllegalJournalEntryException {
		myAccountantApp.authenticate(user);
		Date[] dates = createDates();
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
		assertEquals(lengthOfUserActions+1, myAccountantApp.getMySession().getList().size());
		verify(db).add(test);
	}

	@Test
	public void testModifyInteractionWithDB() {
		autheticationAndSettingLength();
		JournalEntry test = new JournalEntry("1",
				new Date(new GregorianCalendar(1910 + 100, 11, 10).getTimeInMillis()));
		myAccountantApp.modify("2", test);
		assertEquals(lengthOfUserActions+1, myAccountantApp.getMySession().getList().size());
		verify(db).modify("2", test);
	}

	@Test
	public void testDeleteInteractionWithDB() {
		autheticationAndSettingLength();
		myAccountantApp.delete("1");
		assertEquals(lengthOfUserActions+1, myAccountantApp.getMySession().getList().size());
		verify(db).delete("1");
	}

	private Date[] createDates() {
		Date[] dates = new Date[2];
		dates[0] = new Date(new GregorianCalendar(1900 + 116, 10, 1).getTimeInMillis());
		dates[1] = new Date(new GregorianCalendar(1900 + 116, 11, 1).getTimeInMillis());
		return dates;
	}

	private void createJournalEntry(List<JournalEntry> returnList) throws IllegalJournalEntryException {
		JournalEntry entry = new JournalEntry("1",
				new Date(new GregorianCalendar(1900 + 116, 11, 10).getTimeInMillis()));
		entry.setListOfCount(createTestList(1200.0, 1200.0));
		returnList.add(entry);
	}

	private ArrayList<Count> createTestList(double leftValue, double rightValue) {
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
