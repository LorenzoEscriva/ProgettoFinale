package com.tap.project.escrivaghera.AccountantApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public List<Count> getListOfCount() {
		return listOfCount;
	}

	public void setListOfCount(List<Count> list) throws IllegalJournalEntryException {
		int countleft = 0, countright = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isLeft == true) {
				countleft++;
			} else {
				countright++;
			}
		}
		if (countleft != countright)
			throw new IllegalJournalEntryException("The total of counts is several");
		this.listOfCount = list;
	}

}
