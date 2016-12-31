package com.tap.project.escrivaghera.AccountantApp.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;

public class MongoTestHelper {

	public DBCollection accountingRecords;

	public MongoTestHelper(MongoClient mongoClient) {
		DB db = mongoClient.getDB("school");
		db.getCollection("student").drop();
		accountingRecords = db.getCollection("student");
	}

	public JournalEntry createJournalEntry(String id, double leftValue, double rightValue)
			throws IllegalJournalEntryException {
		Date[] date = createDates();
		JournalEntry entry = new JournalEntry(id, date[0]);
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

	public static ArrayList<Count> createTestList(double leftValue, double rightValue) {
		ArrayList<Count> myCounts = new ArrayList<Count>();
		Count count = new Count("count1", true);
		count.setValue(leftValue);
		myCounts.add(count);
		count = new Count("count2", false);
		count.setValue(rightValue);
		myCounts.add(count);
		return myCounts;
	}

	public static Date[] createDates() {
		Date[] dates = new Date[2];
		dates[0] = new Date(new GregorianCalendar(1900 + 116, 11, 1).getTimeInMillis());
		dates[1] = new Date(new GregorianCalendar(1900 + 116, 11, 31).getTimeInMillis());
		return dates;
	}
}
