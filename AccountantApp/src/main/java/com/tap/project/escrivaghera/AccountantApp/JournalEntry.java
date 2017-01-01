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

	private String Id;
	private Date date;
	private List<Count> listOfCount;

	public JournalEntry(String id, Date date) {
		super();
		this.Id = id;
		this.date = date;
		listOfCount = new ArrayList<Count>();
	}

	public String getId() {
		return Id;
	}

	public List<Count> getListOfCount() {
		return listOfCount;
	}

	public void setListOfCount(List<Count> list) throws IllegalJournalEntryException {
		double countleft = 0, countright = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isLeft() == true) {
				countleft = countleft + list.get(i).getValue();
			} else {
				countright = countright + list.get(i).getValue();
			}
		}
		if (countleft != countright)
			throw new IllegalJournalEntryException(
					"The total of counts is several, the difference between the left count and the right count is: "
							+ (countleft - countright));
		this.listOfCount = list;
	}

	public Date getDate() {
		return date;
	}

	public List<BasicDBObject> toListOfBasicDBObject() {
		List<BasicDBObject> listBasicDBObject = new ArrayList<BasicDBObject>();
		String entryDate = convertDateIntoString(date);
		Iterator<Count> countit = listOfCount.iterator();
		while (countit.hasNext()) {
			BasicDBObject record = new BasicDBObject();
			Count count = countit.next();
			record.put("id", Id);
			record.put("date", entryDate);
			record.put("description", count.getDescrizione());
			record.put("value", count.getValue());
			record.put("isLeft", new String(count.isLeft() + ""));
			listBasicDBObject.add(record);
		}
		return listBasicDBObject;
	}

	private String convertDateIntoString(Date date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String entryDate = df.format(date);
		return entryDate;
	}
}
