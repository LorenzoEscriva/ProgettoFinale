package com.tap.project.escrivaghera.AccountantApp.mongo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
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
		Fongo fongo=new Fongo("Server");
		MongoClient mongoClient =fongo.getMongo();
		
		DB db= mongoClient.getDB("AccountingDB");
		db.getCollection("Accounting").drop();
		
		mongoDatabase=new MongoDatabaseWrapper(mongoClient);
		accountingRecords=db.getCollection("Accounting");
	}

	@Test
	public void testAddIsSaved() throws IllegalJournalEntryException {
		Date date = new Date();
		JournalEntry entry =new JournalEntry("1",date);
		List<Count> myCounts=createTestList(1200.0, 1200.0);
		entry.setListOfCount(myCounts);
		
		mongoDatabase.add(entry);
		assertTrue(containsRecord(entry));
	}
	


	private boolean containsRecord(JournalEntry entry) {
		boolean find=true;
		Iterator<BasicDBObject> records=entry.toListOfBasicDBObject().iterator();
		while(records.hasNext() && find){
			find=accountingRecords.find(records.next()).hasNext();
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
