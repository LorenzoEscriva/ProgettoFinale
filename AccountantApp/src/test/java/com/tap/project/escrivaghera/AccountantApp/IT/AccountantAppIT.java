package com.tap.project.escrivaghera.AccountantApp.IT;

import static org.junit.Assert.*;

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
	public void testAddJournalEntryIT() {
		myAccountantApp.authenticate(myUser);
		JournalEntry testEntry = new JournalEntry("1",
				new Date(new GregorianCalendar(1910 + 100, 11, 10).getTimeInMillis()));
		myAccountantApp.add(testEntry);
		assertNotNull(myDB.getAllRegistration());
	}

}
