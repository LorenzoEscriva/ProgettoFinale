package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			if (list.get(i).isLeft == true) {
				countleft = countleft + list.get(i).value;
			} else {
				countright = countright + list.get(i).value;
			}
		}
		if (countleft != countright)
			throw new IllegalJournalEntryException(
					"The total of counts is several, the difference between the left count and the right count is: "
							+ (countleft - countright));
		this.listOfCount = list;
	}
}
