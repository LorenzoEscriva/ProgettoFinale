package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;

public interface Database {
	public void add(JournalEntry newEntry);
	public void modify(String id, JournalEntry changeEntry);
	public void delete(String id);
	public void getAllRegistration(Date date1, Date date2);
}
