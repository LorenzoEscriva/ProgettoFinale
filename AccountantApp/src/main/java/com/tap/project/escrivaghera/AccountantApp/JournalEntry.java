package com.tap.project.escrivaghera.AccountantApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.tap.project.escrivaghera.AccountantApp.exception.*;

public class JournalEntry {

	private String id;
	private Date date;
	private List<Count> listOfCount;

	public JournalEntry(String id, Date date) {
		super();
		this.id = id;
		this.date = date;
		listOfCount = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public List<Count> getListOfCount() {
		return listOfCount;
	}

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

	public Date getDate() {
		return date;
	}

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
