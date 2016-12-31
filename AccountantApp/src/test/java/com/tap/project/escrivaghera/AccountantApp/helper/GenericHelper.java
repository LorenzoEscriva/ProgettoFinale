package com.tap.project.escrivaghera.AccountantApp.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tap.project.escrivaghera.AccountantApp.Count;

public class GenericHelper {

	public static ArrayList<Count> createTestList(double leftValue, double rightValue) {
		ArrayList<Count> myCounts = new ArrayList<Count>();
		Count count = new Count("count1", true);
		count.setValue(leftValue);
		myCounts.add(count);
		count = new Count("count2", false);
		count.setValue(rightValue);
		myCounts.add(count);
		return myCounts;
	}

	public static Date[] createDates() {
		Date[] dates = new Date[2];
		dates[0] = new Date(new GregorianCalendar(1900 + 116, 11, 1).getTimeInMillis());
		dates[1] = new Date(new GregorianCalendar(1900 + 116, 11, 31).getTimeInMillis());
		return dates;
	}

	public GenericHelper() {
		super();
	}

}