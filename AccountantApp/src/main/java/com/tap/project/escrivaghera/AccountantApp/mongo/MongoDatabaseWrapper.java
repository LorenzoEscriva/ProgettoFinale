package com.tap.project.escrivaghera.AccountantApp.mongo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.Database;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<JournalEntry> getAllRegistration(Date date1, Date date2) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
