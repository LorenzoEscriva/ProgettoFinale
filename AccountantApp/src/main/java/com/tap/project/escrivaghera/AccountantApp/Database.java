package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;
import java.util.List;

public interface Database {

	public void add(JournalEntry newEntry);

	public void modify(String id, JournalEntry changeEntry);

	public void delete(String id);

	public List<JournalEntry> getAllRegistration(Date date1, Date date2);
}
