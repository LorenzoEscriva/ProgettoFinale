package com.tap.project.escrivaghera.AccountantApp.IT;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.AccountantApp;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.Server;
import com.tap.project.escrivaghera.AccountantApp.User;
import com.tap.project.escrivaghera.AccountantApp.helper.GenericHelper;
import com.tap.project.escrivaghera.AccountantApp.mongo.MongoDatabaseWrapper;

public class AccountantAppIT {

	private AccountantApp myAccountantApp;
	private User myUser;
	private MongoDatabaseWrapper myDB;

	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("mongo server");
		MongoClient mongoClient = fongo.getMongo();
		myDB = new MongoDatabaseWrapper(mongoClient);
		Server serverTest = new Server(myDB);
		myAccountantApp = new AccountantApp(serverTest);
		myUser = new User("1", "testIT", "testIT");
	}

	@Test
	public void testAddIT() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry = myAccountantApp.createJournalEntry("1", dates[0],
				GenericHelper.createTestList(1100, 1100));
		myDB.add(testEntry);
		assertEquals(1, myDB.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testModifyIT() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0],
				GenericHelper.createTestList(1100, 1100));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2", dates[0],
				GenericHelper.createTestList(1200, 1200));
		myDB.add(testEntry1);
		myDB.modify("1", testEntry2);
		assertEquals(1, myDB.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testDeleteIT() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry = myAccountantApp.createJournalEntry("4", dates[0],
				GenericHelper.createTestList(1300, 1300));
		myDB.add(testEntry);
		myDB.delete("4");
		assertEquals(0, myDB.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITOneElement() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry = myAccountantApp.createJournalEntry("1", dates[0],
				GenericHelper.createTestList(1300, 1300));
		myDB.add(testEntry);
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITMoreElements() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0],
				GenericHelper.createTestList(1300, 1300));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2", dates[0],
				GenericHelper.createTestList(1300, 1300));
		JournalEntry testEntry3 = myAccountantApp.createJournalEntry("3", dates[0],
				GenericHelper.createTestList(1200, 1200));
		myDB.add(testEntry1);
		myDB.add(testEntry2);
		myDB.add(testEntry3);
		assertEquals(3, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITOneBadElement() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0],
				GenericHelper.createTestList(1300, 1300));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2",
				new Date(new GregorianCalendar(1910 + 100, 12, 11).getTimeInMillis()),
				GenericHelper.createTestList(1300, 1300));
		JournalEntry testEntry3 = myAccountantApp.createJournalEntry("3", dates[0],
				GenericHelper.createTestList(1200, 1200));
		myDB.add(testEntry1);
		myDB.add(testEntry2);
		myDB.add(testEntry3);
		assertEquals(2, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITLostElement() {
		Date[] dates = authenticateAndCreateDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0],
				GenericHelper.createTestList(1300, 1300));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2", dates[0],
				GenericHelper.createTestList(1100, 1100));
		myDB.add(testEntry1);
		myDB.add(testEntry2);
		myDB.delete("2");
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	private Date[] authenticateAndCreateDates() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = GenericHelper.createDates();
		return dates;
	}
}