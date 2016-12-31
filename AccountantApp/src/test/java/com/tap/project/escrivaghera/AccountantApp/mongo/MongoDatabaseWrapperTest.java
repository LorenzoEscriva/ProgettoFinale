package com.tap.project.escrivaghera.AccountantApp.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;
import com.tap.project.escrivaghera.AccountantApp.helper.MongoTestHelper;

public class MongoDatabaseWrapperTest {

	private MongoDatabaseWrapper mongoDatabase;
	private MongoTestHelper mongoTestHelper;

	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("Server");
		MongoClient mongoClient = fongo.getMongo();
		mongoTestHelper = new MongoTestHelper(mongoClient);
		mongoDatabase = new MongoDatabaseWrapper(mongoClient);
	}

	@Test
	public void testAddIsSaved() throws IllegalJournalEntryException {
		JournalEntry entry = mongoTestHelper.createJournalEntry("1", 1300.0, 1300.0);
		mongoDatabase.add(entry);
		assertTrue(mongoTestHelper.containRecord(entry));
	}

	@Test
	public void testModify() throws IllegalJournalEntryException {
		JournalEntry removed = mongoTestHelper.createJournalEntry("1", 1200.0, 1200.0);
		JournalEntry modify = mongoTestHelper.createJournalEntry("2", 1300.0, 1300.0);
		mongoTestHelper.addRecord(removed);
		mongoDatabase.modify("1", modify);
		assertFalse(mongoTestHelper.containRecord(removed));
		assertTrue(mongoTestHelper.containRecord(modify));
	}

	@Test
	public void testDelete() throws IllegalJournalEntryException {
		JournalEntry added = mongoTestHelper.createJournalEntry("1", 1200.0, 1200.0);
		mongoTestHelper.addRecord(added);
		mongoDatabase.delete("1");
		assertFalse(mongoTestHelper.containRecord(added));
	}

	@Test
	public void testGetAllRegistractionsEmpty() {
		Date[] dates = mongoTestHelper.createDates();
		assertTrue(mongoDatabase.getAllRegistration(dates[0], dates[1]).isEmpty());
	}

	@Test
	public void testGetAllRegistractionNotEmpty() throws IllegalJournalEntryException {
		Date[] dates = mongoTestHelper.createDates();
		JournalEntry added = mongoTestHelper.createJournalEntry("1", 1200.0, 1200.0);
		JournalEntry added2 = mongoTestHelper.createJournalEntry("2", 1300.0, 1300.0);
		mongoTestHelper.addRecord(added);
		mongoTestHelper.addRecord(added2);
		assertEquals(2, mongoDatabase.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistractionWithOneRegistractionNotIncluded() throws IllegalJournalEntryException {
		Date[] dates = mongoTestHelper.createDates();
		JournalEntry added = mongoTestHelper.createJournalEntry("1", 1200.0, 1200.0);
		JournalEntry added2 = mongoTestHelper.createJournalEntry("2", 1300.0, 1300.0);
		mongoTestHelper.addRecord(added);
		mongoTestHelper.addRecord(added2);
		List<Count> myCount = mongoTestHelper.createTestList(1500.0, 1500.0);
		JournalEntry entry = new JournalEntry("3",
				new Date(new GregorianCalendar(1900 + 116, 10, 1).getTimeInMillis()));
		entry.setListOfCount(myCount);
		Iterator<BasicDBObject> records = entry.toListOfBasicDBObject().iterator();
		while (records.hasNext()) {
			mongoTestHelper.accountingRecords.insert(records.next());
		}
		assertEquals(2, mongoDatabase.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistractionWithLostRecord() throws IllegalJournalEntryException {
		Date[] dates = mongoTestHelper.createDates();
		JournalEntry myEntry = mongoTestHelper.createJournalEntry("1", 1200.0, 1200.0);
		List<BasicDBObject> myListOfRecord = myEntry.toListOfBasicDBObject();
		Iterator<BasicDBObject> records = myListOfRecord.iterator();
		while (records.hasNext()) {
			mongoTestHelper.accountingRecords.insert(records.next());
		}
		mongoTestHelper.accountingRecords.remove(myListOfRecord.get(0));
		assertNull(mongoDatabase.getAllRegistration(dates[0], dates[1]));
	}

}
