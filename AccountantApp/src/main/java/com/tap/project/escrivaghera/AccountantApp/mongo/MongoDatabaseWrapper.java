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
	private final static Logger LOGGER = Logger.getLogger(AccountantApp.class);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tap.project.escrivaghera.AccountantApp.Database#getAllRegistration(
	 * java.util.Date, java.util.Date)
	 * 
	 * For implement the getAllRegistraction between two date we take the list
	 * of all registrations (with the getAllRegistraction method without
	 * parameters) and then we delete from that list the intruders element, that
	 * is the records that have got the date out of range.
	 */
	@Override
	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		List<JournalEntry> listOfJournalEntry = getAllRegistration();
		if (listOfJournalEntry != null) {
			int i = 0;
			while (i < listOfJournalEntry.size()) {
				JournalEntry current = listOfJournalEntry.get(i);
				Date currentDate = current.getDate();
				if (currentDate.compareTo(date1) < 0 || currentDate.compareTo(date2)> 0)
					listOfJournalEntry.remove(i);
				i = i + 1;
			}
		}
		return listOfJournalEntry;
	}

	/*
	 * For create the list of JounrnalEntry's object we need to create the
	 * JournalEntries's object from the database info. We note that the record
	 * format is: | id| date| description| value| isLeft| First of all, we take
	 * the info for create a JounalEntry object using the class constructor In
	 * the second part, we take the info from the record for create the first
	 * Count and we add it to the list of Count object Finally, we create all
	 * other Count object analyzing the subsequent record that have got equal
	 * id. We put the objects create into the corresponding list
	 */
	private List<JournalEntry> getAllRegistration() {
		List<JournalEntry> listOfJournalEntry = new ArrayList<JournalEntry>();
		DBCursor cursor = accountingRecords.find();
		List<DBObject> myCursorList = cursor.toArray();
		int i = 0;
		while (i < myCursorList.size()) {
			// we create the JournalEntry object
			DBObject newJournalEntryCursor = myCursorList.get(i);
			String idJournalEntry = (String) newJournalEntryCursor.get("id");
			String dateJournalEntry = (String) newJournalEntryCursor.get("date");
			try {
				// we create the first Count's object (the current record's
				// Count object)
				Date date = convertStringToDate(dateJournalEntry);
				JournalEntry newEntry = new JournalEntry(idJournalEntry, date);

				List<Count> counts = new ArrayList<Count>();
				Count count = extractCount(newJournalEntryCursor);
				counts.add(count);

				// we create the other Count object from the subsequent records
				// info
				boolean exit = false;
				while (i + 1 < myCursorList.size() && !exit) {
					i = i + 1;
					DBObject newCountCursor = myCursorList.get(i);
					String idNextRecord = (String) newCountCursor.get("id");
					if (idNextRecord.equals(idJournalEntry)) {
						count = extractCount(newCountCursor);
						counts.add(count);
					} else {
						exit = true;
						i = i - 1;
					}
				}
				newEntry.setListOfCount(counts);
				listOfJournalEntry.add(newEntry);
			} catch (ParseException | IllegalJournalEntryException e) {
				LOGGER.error("EXECPTION: ", e);
				return null;
			}
			i = i + 1;
		}
		return listOfJournalEntry;
	}

	public Count extractCount(DBObject countCursor) {
		String elementDescription = (String) countCursor.get("description");
		boolean elementIsLeft = new Boolean((String) countCursor.get("isLeft"));
		Count count = new Count(elementDescription, elementIsLeft);
		double elementValue = (Double) countCursor.get("value");
		count.setValue(elementValue);
		return count;
	}

	private Date convertStringToDate(String dateJournalEntry) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = formatter.parse(dateJournalEntry);
		return date;
	}

}
