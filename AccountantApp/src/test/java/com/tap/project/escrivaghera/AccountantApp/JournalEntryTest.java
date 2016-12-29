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
	public void testThatGetIdDoNotReturnAnObject() {
		assertTrue(myJournalEntry.getId().equals("id1"));
	}

	@Test
	public void setListOfCountTestGoodCase() throws IllegalJournalEntryException {
		List<Count> list = createTestList(1200.0, 1200.0);
		myJournalEntry.setListOfCount(list);
		assertEquals(list, myJournalEntry.getListOfCount());
	}

	@Test
	public void setListOfCountTestExceptionCase() {
		List<Count> list = createTestList(1200.0, 1100.0);
		try {
			myJournalEntry.setListOfCount(list);
			fail("should not go here");
		} catch (IllegalJournalEntryException e) {
			assertTrue(e.getLocalizedMessage().equals(
					"The total of counts is several, the difference between the left count and the right count is: 100.0"));
		}
	}

	@Test
	public void testToListOfBasicDBObject() throws IllegalJournalEntryException {
		List<Count> list = createTestList(1200.0, 1200.0);
		myJournalEntry.setListOfCount(list);

		assertEquals(2, myJournalEntry.toListOfBasicDBObject().size());
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
