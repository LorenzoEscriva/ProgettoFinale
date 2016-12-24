package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SessionTest {
	private Session mysession;

	@Before
	public void setUp() throws Exception {
		User myuser=new User("1","test", "test");
		mysession=new Session(1, myuser);
	}

	@Test
	public void testAddADescriptionToActionList() {
		String action=new String("delete record with id 0001");
		mysession.addDescription(action);
		assertEquals(1, mysession.getList().size());
	}

}
