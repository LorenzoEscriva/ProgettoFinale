package com.tap.project.escrivaghera.AccountantApp.app;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.AccountantApp;
import com.tap.project.escrivaghera.AccountantApp.Count;
import com.tap.project.escrivaghera.AccountantApp.Database;
import com.tap.project.escrivaghera.AccountantApp.JournalEntry;
import com.tap.project.escrivaghera.AccountantApp.Server;
import com.tap.project.escrivaghera.AccountantApp.User;
import com.tap.project.escrivaghera.AccountantApp.mongo.MongoDatabaseWrapper;

public class Main {

	public static void main(String[] args) throws UnknownHostException {
		String mongoHost = "localhost";
		if (args.length > 0)
			mongoHost = args[0];
		Database db = new MongoDatabaseWrapper(new MongoClient(mongoHost));
		Server server = new Server(db);
		AccountantApp app = new AccountantApp(server);
		User admin = new User("1", "admin", "main");
		app.authenticate(admin);
		ArrayList<Count> myCounts = new ArrayList<Count>();
		Count count = new Count("Purchase", true);
		count.setValue(12000);
		myCounts.add(count);
		count = new Count("Sale", false);
		count.setValue(12000);
		myCounts.add(count);
		JournalEntry newEntry = app.createJournalEntry("5",
				new Date(new GregorianCalendar(1900 + 117, 1, 1).getTimeInMillis()), myCounts);
		app.add(newEntry);
		System.out.println("Accountant app terminates.");
	}
}
