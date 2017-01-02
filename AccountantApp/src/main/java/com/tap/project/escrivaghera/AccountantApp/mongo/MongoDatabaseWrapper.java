package com.tap.project.escrivaghera.AccountantApp.mongo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.AccountantApp;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.Database;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;

public class MongoDatabaseWrapper implements Database {

	private DBCollection accountingRecords;
	private static final Logger LOGGER = Logger.getLogger(AccountantApp.class);

	public MongoDatabaseWrapper(MongoClient mongoClient) {
		DB db = mongoClient.getDB("AccountingDB");
		accountingRecords = db.getCollection("Accounting");
	}

	@Override
	public void add(JournalEntry newEntry) {
		Iterator<BasicDBObject> records = newEntry.toListOfBasicDBObject().iterator();
		while (records.hasNext()) {
			accountingRecords.save(records.next());
		}
	}

	@Override
	public void modify(String id, JournalEntry changeEntry) {
		delete(id);
		add(changeEntry);
	}

	@Override
	public void delete(String id) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("id", id);
		DBObject find;
		do {
			find = accountingRecords.findAndRemove(searchQuery);
		} while (find != null);
	}

	@Override
	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		List<JournalEntry> listOfJournalEntry = getAllRegistration();
		if (!listOfJournalEntry.isEmpty()) {
			int i = 0;
			while (i < listOfJournalEntry.size()) {
				JournalEntry current = listOfJournalEntry.get(i);
				Date currentDate = current.getDate();
				if (currentDate.compareTo(date1) < 0 || currentDate.compareTo(date2) > 0)
					listOfJournalEntry.remove(i);
				i = i + 1;
			}
		}
		return listOfJournalEntry;
	}

	private List<JournalEntry> getAllRegistration() {
		List<JournalEntry> listOfJournalEntry = new ArrayList<>();
		DBCursor cursor = accountingRecords.find();
		List<DBObject> myCursorList = cursor.toArray();
		int i = 0;
		while (i < myCursorList.size()) {
			DBObject newJournalEntryCursor = myCursorList.get(i);
			String idJournalEntry = (String) newJournalEntryCursor.get("id");
			String dateJournalEntry = (String) newJournalEntryCursor.get("date");
			try {
				Date date = convertStringToDate(dateJournalEntry);
				JournalEntry newEntry = new JournalEntry(idJournalEntry, date);
				List<Count> counts = new ArrayList<>();
				Count count = extractCount(newJournalEntryCursor);
				counts.add(count);
				i = createCount(myCursorList, i, idJournalEntry, counts);
				newEntry.setListOfCount(counts);
				listOfJournalEntry.add(newEntry);
			} catch (ParseException | IllegalJournalEntryException e) {
				LOGGER.error("EXECPTION: ", e);
				return new ArrayList<>();
			}
			i = i + 1;
		}
		return listOfJournalEntry;
	}

	private int createCount(List<DBObject> myCursorList, int k, String idJournalEntry, List<Count> counts) {
		Count count;
		boolean exit = false;
		int j = k;
		while (j + 1 < myCursorList.size() && !exit) {
			j = j + 1;
			DBObject newCountCursor = myCursorList.get(j);
			String idNextRecord = (String) newCountCursor.get("id");
			if (idNextRecord.equals(idJournalEntry)) {
				count = extractCount(newCountCursor);
				counts.add(count);
			} else {
				exit = true;
				j = j - 1;
			}
		}
		return j;
	}

	public Count extractCount(DBObject countCursor) {
		String elementDescription = (String) countCursor.get("description");
		boolean elementIsLeft = Boolean.parseBoolean((String) countCursor.get("isLeft"));
		Count count = new Count(elementDescription, elementIsLeft);
		double elementValue = (Double) countCursor.get("value");
		count.setValue(elementValue);
		return count;
	}

	private Date convertStringToDate(String dateJournalEntry) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.parse(dateJournalEntry);
	}

}
