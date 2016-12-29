package com.tap.project.escrivaghera.AccountantApp.mongo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;

public class MongoDatabaseWrapperTest {
	private MongoDatabaseWrapper mongoDatabase;
	private DBCollection accountingRecords;

	@Before
	public void setUp() throws Exception {
		Fongo fongo = new Fongo("Server");
		MongoClient mongoClient = fongo.getMongo();

		DB db = mongoClient.getDB("AccountingDB");
		db.getCollection("Accounting").drop();

		mongoDatabase = new MongoDatabaseWrapper(mongoClient);
		accountingRecords = db.getCollection("Accounting");
	}

	@Test
	public void testAddIsSaved() throws IllegalJournalEntryException {
		JournalEntry entry = createJournalEntry("1", 1200.0, 1200.0);

		mongoDatabase.add(entry);
		assertTrue(containRecord(entry));
	}

	private JournalEntry createJournalEntry(String id, double leftValue, double rightValue)
			throws IllegalJournalEntryException {
		Date date = new Date();
		JournalEntry entry = new JournalEntry(id, date);
		List<Count> myCounts = createTestList(leftValue, rightValue);
		entry.setListOfCount(myCounts);
		return entry;
	}

	@Test
	public void testModify() throws IllegalJournalEntryException {
		JournalEntry myEntry = addRecord("1", 1200.0, 1200.0);
		JournalEntry modify = createJournalEntry("1", 1300.0, 1300.0);
		mongoDatabase.modify("1", modify);
		assertFalse(containRecord(myEntry));
		assertTrue(containRecord(modify));
	}

	private JournalEntry addRecord(String id, double leftValue, double rightValue) throws IllegalJournalEntryException {
		JournalEntry myEntry = createJournalEntry(id, leftValue, rightValue);
		Iterator<BasicDBObject> records = myEntry.toListOfBasicDBObject().iterator();
		while (records.hasNext()) {
			accountingRecords.insert(records.next());
		}
		return myEntry;
	}

	@Test
	public void testDelete() throws IllegalJournalEntryException {
		addRecord("1", 1200.0, 1200.0);
		assertEquals(2, mongoDatabase.delete("1"));
	}

	@Test
	public void testGetAllRegistractionsEmpty() {
		Date[] dates = createDates();
		assertTrue(mongoDatabase.getAllRegistration(dates[0], dates[1]).isEmpty());
	}

	@Test
	public void testGetAllRegistractionNotEmpty() throws IllegalJournalEntryException {
		Date[] dates = createDates();
		addRecord("1", 1200.0, 1200.0);
		addRecord("2", 1300.0, 1300.0);
		assertEquals(2, mongoDatabase.getAllRegistration(dates[0], dates[1]).size());
	}

	@Test
	public void testGetAllRegistractionWithOneRegistractionNotIncluded() throws IllegalJournalEntryException {
		Date[] dates = createDates();
		addRecord("1", 1200.0, 1200.0);
		addRecord("2", 1300.0, 1300.0);
		List<Count> myCount = createTestList(1500.0, 1500.0);
		JournalEntry entry = new JournalEntry("3",
				new Date(new GregorianCalendar(1900 + 116, 10, 1).getTimeInMillis()));
		entry.setListOfCount(myCount);
		Iterator<BasicDBObject> records = entry.toListOfBasicDBObject().iterator();
		while (records.hasNext()) {
			accountingRecords.insert(records.next());
		}

		assertEquals(2, mongoDatabase.getAllRegistration(dates[0], dates[1]).size());

	}

	@Test
	public void testGetAllRegistractionWithLostRecord() throws IllegalJournalEntryException {
		Date[] dates = createDates();
		JournalEntry myEntry = createJournalEntry("1", 1200.0, 1200.0);
		List<BasicDBObject> myListOfRecord = myEntry.toListOfBasicDBObject();
		Iterator<BasicDBObject> records = myListOfRecord.iterator();
		while (records.hasNext()) {
			accountingRecords.insert(records.next());
		}
		accountingRecords.remove(myListOfRecord.get(0));
		assertNull(mongoDatabase.getAllRegistration(dates[0], dates[1]));
	}

	private Date[] createDates() {
		Date[] dates = new Date[2];
		dates[0] = new Date(new GregorianCalendar(1900 + 116, 11, 1).getTimeInMillis());
		dates[1] = new Date(new GregorianCalendar(1900 + 116, 11, 31).getTimeInMillis());
		return dates;
	}

	private boolean containRecord(JournalEntry entry) {
		boolean find = true;
		Iterator<BasicDBObject> records = entry.toListOfBasicDBObject().iterator();
		while (records.hasNext() && find) {
			find = accountingRecords.find(records.next()).hasNext();
		}
		return find;
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
