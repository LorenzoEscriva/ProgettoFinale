package com.tap.project.escrivaghera.AccountantApp.IT;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.AccountantApp;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.Server;
import com.tap.project.escrivaghera.AccountantApp.User;
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
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry = myAccountantApp.createJournalEntry("1", dates[0], createTestList(1100, 1100));
		myDB.add(testEntry);
		assertEquals(1, myDB.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testModifyIT() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0], createTestList(1100, 1100));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2", dates[0], createTestList(1200, 1200));
		myDB.add(testEntry1);
		myDB.modify("1", testEntry2);
		assertEquals(1, myDB.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testDeleteIT() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry = myAccountantApp.createJournalEntry("4", dates[0], createTestList(1300, 1300));
		myDB.add(testEntry);
		myDB.delete("4");
		assertEquals(0, myDB.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITOneElement() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry = myAccountantApp.createJournalEntry("1", dates[0], createTestList(1300, 1300));
		myDB.add(testEntry);
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITMoreElements() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0], createTestList(1300, 1300));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2", dates[0], createTestList(1300, 1300));
		JournalEntry testEntry3 = myAccountantApp.createJournalEntry("3", dates[0], createTestList(1200, 1200));
		myDB.add(testEntry1);
		myDB.add(testEntry2);
		myDB.add(testEntry3);
		assertEquals(3, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITOneBadElement() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0], createTestList(1300, 1300));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2",
				new Date(new GregorianCalendar(1910 + 100, 12, 11).getTimeInMillis()), createTestList(1300, 1300));
		JournalEntry testEntry3 = myAccountantApp.createJournalEntry("3", dates[0], createTestList(1200, 1200));
		myDB.add(testEntry1);
		myDB.add(testEntry2);
		myDB.add(testEntry3);
		assertEquals(2, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistrationITLostElement() {
		myAccountantApp.authenticate(myUser);
		Date[] dates = createDates();
		JournalEntry testEntry1 = myAccountantApp.createJournalEntry("1", dates[0], createTestList(1300, 1300));
		JournalEntry testEntry2 = myAccountantApp.createJournalEntry("2", dates[0], createTestList(1100, 1100));
		myDB.add(testEntry1);
		myDB.add(testEntry2);
		myDB.delete("2");
		assertEquals(1, myAccountantApp.getAllRegistration(dates[0], dates[1]).size());
	}

	private Date[] createDates() {
		Date[] dates = new Date[2];
		dates[0] = new Date(new GregorianCalendar(1910 + 100, 11, 10).getTimeInMillis());
		dates[1] = new Date(new GregorianCalendar(1910 + 100, 11, 11).getTimeInMillis());
		return dates;
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
