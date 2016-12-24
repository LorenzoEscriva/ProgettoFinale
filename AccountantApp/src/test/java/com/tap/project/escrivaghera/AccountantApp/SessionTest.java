package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SessionTest {
	private Session mysession;
	private User myuser;

	@Before
	public void setUp() throws Exception {
		myuser=new User("1","test", "test");
		mysession=new Session(1, myuser);
	}

	@Test
	public void testAddDescriptionToActionList() {
		String action=new String("delete record with id 0001");
		mysession.addDescription(action);
		assertEquals(1, mysession.getList().size());
	}

	@Test
	public void testAddDescriptionCorrectDescriptionFormat(){
		String action=new String("delete record with id 0001");
		String descriptionAction=("In the session 1, the user "+ myuser.toString() +" "+ action);
		mysession.addDescription(action);
		assertEquals(descriptionAction, mysession.getList().get(0));
	}
}
