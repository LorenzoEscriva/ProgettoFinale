package com.tap.project.escrivaghera.AccountantApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.tap.project.escrivaghera.AccountantApp.exception.*;

/**
 * This class creates a new journal entry with all accountant movements at it associated
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public class JournalEntry {

	private String id;
	private Date date;
	private List<Count> listOfCount;

	/**
	 * Create a new JournalEntry's object
	 * 
	 * @param object's id
	 * @param date associated
	 */
	public JournalEntry(String id, Date date) {
		super();
		this.id = id;
		this.date = date;
		listOfCount = new ArrayList<>();
	}
	
	/**
	 * Gets id of current object
	 * 
	 * @return object's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets all accountant movements
	 * @return the list of movements
	 */
	public List<Count> getListOfCount() {
		return listOfCount;
	}

	/**
	 * Adds a new list of movements, the sum of count's value which has got the isLeft field true must be equal
	 * to the sum of count's value which has got the isLeft field false
	 * 
	 * @param the list of count to add
	 * @throws IllegalJournalEntryException when the totals of count's value is several 
	 */
	public void setListOfCount(List<Count> list) throws IllegalJournalEntryException {
		double countleft = 0;
		double countright = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isLeft()) {
				countleft = countleft + list.get(i).getValue();
			} else {
				countright = countright + list.get(i).getValue();
			}
		}
		if (!new Double(countleft).equals(new Double(countright)))
			throw new IllegalJournalEntryException(
					"The total of counts is several, the difference between the left count and the right count is: "
							+ (countleft - countright));
		this.listOfCount = list;
	}

	/**
	 * Gets the date associated to the current object
	 * @return the current date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Create a list of BasicDBObject's objects of the current JournalEntry
	 * @return the correspondent list
	 */
	public List<BasicDBObject> toListOfBasicDBObject() {
		List<BasicDBObject> listBasicDBObject = new ArrayList<>();
		String entryDate = convertDateIntoString(date);
		Iterator<Count> countit = listOfCount.iterator();
		while (countit.hasNext()) {
			BasicDBObject record = new BasicDBObject();
			Count count = countit.next();
			record.put("id", id);
			record.put("date", entryDate);
			record.put("description", count.getDescrizione());
			record.put("value", count.getValue());
			record.put("isLeft", Boolean.toString(count.isLeft()));
			listBasicDBObject.add(record);
		}
		return listBasicDBObject;
	}

	private String convertDateIntoString(Date date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(date);
	}
}
