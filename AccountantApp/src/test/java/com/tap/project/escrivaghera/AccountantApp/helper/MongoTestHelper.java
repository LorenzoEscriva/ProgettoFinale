package com.tap.project.escrivaghera.AccountantApp.helper;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;

public class MongoTestHelper extends GenericHelper {

	public DBCollection accountingRecords;

	public MongoTestHelper(MongoClient mongoClient) {
		DB db = mongoClient.getDB("AccountingDB");
		db.getCollection("Accounting").drop();
		accountingRecords = db.getCollection("Accounting");
	}

	public JournalEntry createJournalEntry(String id, double leftValue, double rightValue, int dateIntervall)
			throws IllegalJournalEntryException {
		Date[] date = createDates();
		JournalEntry entry = new JournalEntry(id, date[dateIntervall]);
		List<Count> myCounts = createTestList(leftValue, rightValue);
		entry.setListOfCount(myCounts);
		return entry;
	}

	public boolean containRecord(JournalEntry entry) {
		boolean find = true;
		Iterator<BasicDBObject> records = entry.toListOfBasicDBObject().iterator();
		while (records.hasNext() && find) {
			find = accountingRecords.find(records.next()).hasNext();
		}
		return find;
	}

	public void addRecord(JournalEntry myEntry) throws IllegalJournalEntryException {
		Iterator<BasicDBObject> records = myEntry.toListOfBasicDBObject().iterator();
		while (records.hasNext()) {
			accountingRecords.insert(records.next());
		}
	}
}
