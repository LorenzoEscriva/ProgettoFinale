package com.tap.project.escrivaghera.AccountantApp.IT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.AccountantApp;
import com.tap.project.escrivaghera.AccountantApp.Database;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.Server;
import com.tap.project.escrivaghera.AccountantApp.User;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;
import com.tap.project.escrivaghera.AccountantApp.helper.MongoTestHelper;
import com.tap.project.escrivaghera.AccountantApp.mongo.MongoDatabaseWrapper;

public class AccountantAppIT {

	private MongoTestHelper mongoTestHelper;
	private AccountantApp myAccountantApp;
	private User myUser;

	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("Server");
		MongoClient mongoClient = fongo.getMongo();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		Database myDB = new MongoDatabaseWrapper(mongoClient);
		Server serverTest = new Server(myDB);
		myAccountantApp = new AccountantApp(serverTest);
		myUser = new User("1", "testIT", "testIT");
	}

	@Test
	public void testAddIT() throws IllegalJournalEntryException {
		myAccountantApp.authenticate(myUser);
		JournalEntry testEntry = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		myAccountantApp.add(testEntry);
		assertTrue(mongoTestHelper.containRecord(testEntry));
	}

	@Test
	public void testModifyIT() throws IllegalJournalEntryException {
		myAccountantApp.authenticate(myUser);
		JournalEntry testEntry1 = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		JournalEntry testEntry2 = mongoTestHelper.createJournalEntry("1", 1200, 1200);
		mongoTestHelper.addRecord(testEntry1);
		myAccountantApp.modify("1", testEntry2);
		assertFalse(mongoTestHelper.containRecord(testEntry1));
		assertTrue(mongoTestHelper.containRecord(testEntry2));
	}

	@Test
	public void testDeleteIT() throws IllegalJournalEntryException {
		myAccountantApp.authenticate(myUser);
		JournalEntry removed = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		mongoTestHelper.addRecord(removed);
		myAccountantApp.delete("1");
		assertFalse(mongoTestHelper.containRecord(removed));
	}

	@Test
	public void testGetAllRegistrationITOneElement() throws IllegalJournalEntryException {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		mongoTestHelper.addRecord(testEntry);
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITMoreElements() throws IllegalJournalEntryException {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		JournalEntry testEntry2 = mongoTestHelper.createJournalEntry("2", 1200, 1200);
		JournalEntry testEntry3 = mongoTestHelper.createJournalEntry("3", 1300, 1300);
		mongoTestHelper.addRecord(testEntry1);
		mongoTestHelper.addRecord(testEntry2);
		mongoTestHelper.addRecord(testEntry3);
		assertEquals(3, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITOneBadElement() throws IllegalJournalEntryException {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		JournalEntry testEntry2 = mongoTestHelper.createJournalEntry("2", 1200, 1200);
		JournalEntry testEntry3 = myAccountantApp.createJournalEntry("3",
				new Date(new GregorianCalendar(1910 + 100, 12, 11).getTimeInMillis()),
				mongoTestHelper.createTestList(1300, 1300));
		mongoTestHelper.addRecord(testEntry1);
		mongoTestHelper.addRecord(testEntry2);
		mongoTestHelper.addRecord(testEntry3);
		assertEquals(2, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITLostElement() throws IllegalJournalEntryException {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = mongoTestHelper.createJournalEntry("1", 1100, 1100);
		JournalEntry testEntry2 = mongoTestHelper.createJournalEntry("2", 1200, 1200);
		mongoTestHelper.addRecord(testEntry1);
		mongoTestHelper.addRecord(testEntry2);
		myAccountantApp.delete("2");
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	private Date[] authenticateAndCreateDates() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = mongoTestHelper.createDates();
		return dates;
	}
}