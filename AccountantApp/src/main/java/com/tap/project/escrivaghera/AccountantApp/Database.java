package com.tap.project.escrivaghera.AccountantApp;

import java.util.Date;
import java.util.List;

/**
 * This interface must be implemented by database wrapper
 * 
 * @author Matteo Ghera
 * @author Lorenzo Escriva
 *
 */
public interface Database {
	/**
	 * Adds a new journal entry description. This description is made by a number of records as the length
	 * of count's list associated at this JournalEntry's object
	 * 
	 * @param the new Journal Entry to add
	 */
	public void add(JournalEntry newEntry);

	/**
	 * Changes the records matching at the id with records of JournalEntry changeEntry
	 * @param the id of record to change
	 * @param the JournalEntry substitute
	 */
	public void modify(String id, JournalEntry changeEntry);

	/**
	 * Deletes the record with id
	 * @param id
	 */
	public void delete(String id);

	/**
	 * Takes the list of all journal entries between two dates
	 * 
	 * @param fist date
	 * @param second date
	 * @return the list of journal entries
	 */
	public List<JournalEntry> getAllRegistration(Date date1, Date date2);
}
