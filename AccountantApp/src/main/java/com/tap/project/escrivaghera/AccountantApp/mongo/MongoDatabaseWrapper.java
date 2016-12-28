package com.tap.project.escrivaghera.AccountantApp.mongo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.Database;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.exception.IllegalJournalEntryException;

public class MongoDatabaseWrapper implements Database {
	private DBCollection accountingRecords;

	public MongoDatabaseWrapper(MongoClient mongoClient) {
		DB db =mongoClient.getDB("AccountingDB");
		accountingRecords=db.getCollection("Accounting");
	}

	@Override
	public void add(JournalEntry newEntry) {
		Iterator<BasicDBObject> records=newEntry.toListOfBasicDBObject().iterator();
		while(records.hasNext()){
			accountingRecords.save(records.next());
		}
	}

	@Override
	public void modify(String id, JournalEntry changeEntry) {
		delete(id);
		add(changeEntry);
	}

	@Override
	public int delete(String id) {
		BasicDBObject searchQuery=new BasicDBObject();
		searchQuery.put("id", id);
		DBObject find;
		int numberOfRecordsDelete=0;
		do{
			find=accountingRecords.findAndRemove(searchQuery);
			numberOfRecordsDelete++;
		} while(find!=null);
		return numberOfRecordsDelete-1;
	}

	@Override
	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		List<JournalEntry> listOfJournalEntry=getAllRegistration();
		int i=0;
		while(i<listOfJournalEntry.size()){
			JournalEntry current =listOfJournalEntry.get(i);
			Date currentDate=current.getDate();
			if(currentDate.compareTo(date1)<0 || currentDate.compareTo(date2)>0)
				listOfJournalEntry.remove(i);
			i=i+1;
		}
		return listOfJournalEntry;
	}

	private List<JournalEntry> getAllRegistration() {
		List<JournalEntry> listOfJournalEntry=new ArrayList<JournalEntry>();
		DBCursor cursor=accountingRecords.find();
		List<DBObject> myCursorIterator=cursor.toArray();
		int i=0;
		while(i<myCursorIterator.size()){
			DBObject newJournalEntryCursor=myCursorIterator.get(i);
			String idJournalEntry=(String)newJournalEntryCursor.get("id");
			String dateJournalEntry=(String)newJournalEntryCursor.get("date");
			try {
				Date date = convertStringToDate(dateJournalEntry);
				JournalEntry newEntry=new JournalEntry(idJournalEntry, date);
				
				List<Count> counts =new ArrayList<Count>();
				Count count = extractCount(newJournalEntryCursor);
				counts.add(count);

				boolean exit=false;
				while(i+1<myCursorIterator.size() && !exit){
					i=i+1;
					DBObject newCountCursor=myCursorIterator.get(i);
					String idNextRecord=(String)newCountCursor.get("id");
					if(idNextRecord.equals(idJournalEntry)){
						count=extractCount(newCountCursor);
						counts.add(count);
					} else{ 
						exit=true;
						i=i-1;
					}
				}
				newEntry.setListOfCount(counts);
				listOfJournalEntry.add(newEntry);
			} catch (ParseException | IllegalJournalEntryException e) {
				return null;
			}
			i=i+1;
		}
		return listOfJournalEntry;
	}

	public Count extractCount(DBObject countCursor) {
		String elementDescription =(String) countCursor.get("description");
		boolean elementIsLeft=new Boolean((String)countCursor.get("isLeft"));
		Count count=new Count(elementDescription, elementIsLeft);
		double elementValue=(Double)countCursor.get("value");
		count.setValue(elementValue);
		return count;
	}

	private Date convertStringToDate(String dateJournalEntry) throws ParseException {
		SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
		Date date=formatter.parse(dateJournalEntry);
		return date;
	}
	

}
