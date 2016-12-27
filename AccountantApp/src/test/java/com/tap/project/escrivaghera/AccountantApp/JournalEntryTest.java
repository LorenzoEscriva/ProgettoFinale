package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tap.project.escrivaghera.AccountantApp.exception.*;

import org.junit.Before;
import org.junit.Test;

public class JournalEntryTest {

	private static final Date date = Calendar.getInstance().getTime();
	private JournalEntry myJournalEntry;

	@Before
	public void setUp() throws Exception {
		myJournalEntry = new JournalEntry("id1", date);
	}

	@Test
	public void testThatGetIdDoNotReturnAnObject(){
		assertTrue(myJournalEntry.getId().equals("id1"));
	}
	
	@Test
	public void setListOfCountTestGoodCase() throws IllegalJournalEntryException {
		List<Count> list = new ArrayList<Count>();
		Count count1 = new Count("count1", false);
		Count count2 = new Count("count2", true);
		count1.setValue(1200);
		count2.setValue(1200);
		list.add(count1);
		list.add(count2);
		myJournalEntry.setListOfCount(list);
		assertEquals(list, myJournalEntry.getListOfCount());
	}

	@Test
	public void setListOfCountTestExceptionCase() {
		List<Count> list = new ArrayList<Count>();
		Count count1 = new Count("count1", true);
		Count count2 = new Count("count2", false);
		count1.setValue(1200);
		count2.setValue(1100);
		list.add(count1);
		list.add(count2);
		try {
			myJournalEntry.setListOfCount(list);
			fail("should not go here");
		} catch (IllegalJournalEntryException e) {
			assertTrue(e.getLocalizedMessage().equals("The total of counts is several, the difference between the left count and the right count is: 100.0"));
		}
	}
}
