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
		JournalEntry entry = createJournalEntry(1200.0, 1200.0);
		
		mongoDatabase.add(entry);
		assertTrue(containRecord(entry));
	}

	private JournalEntry createJournalEntry(double leftValue, double rigthValue) throws IllegalJournalEntryException {
		Date date = new Date();
		JournalEntry entry =new JournalEntry("1",date);
		List<Count> myCounts=createTestList(leftValue, rigthValue);
		entry.setListOfCount(myCounts);
		return entry;
	}
	
	@Test
	public void testModify() throws IllegalJournalEntryException{
		JournalEntry myEntry = addRecord();
		JournalEntry modify =createJournalEntry(1300.0, 1300.0);
		mongoDatabase.modify("1", modify);
		assertFalse(containRecord(myEntry));
		assertTrue(containRecord(modify));		
	}

	public JournalEntry addRecord() throws IllegalJournalEntryException {
		JournalEntry myEntry=createJournalEntry(1200.0, 1200.0);
		Iterator<BasicDBObject> records=myEntry.toListOfBasicDBObject().iterator();
		while(records.hasNext()){
			accountingRecords.insert(records.next());
		}
		return myEntry;
	}
	
	@Test
	public void testDelete() throws IllegalJournalEntryException{
		addRecord();
		assertEquals(2, mongoDatabase.delete("1"));
	}

	private boolean containRecord(JournalEntry entry) {
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
