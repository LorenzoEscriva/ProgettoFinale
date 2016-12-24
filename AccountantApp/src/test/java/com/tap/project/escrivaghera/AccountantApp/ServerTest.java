package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {
	private Server myServer;

	@Before
	public void setUp() throws Exception {
		myServer=new Server();
	}

	@Test
	public void testGetSessionWithFirstSession() {
		Session session = createSession("1");
		assertEquals(1, session.getSessionId());
	}

	public Session createSession(String userId) {
		User user = new User(userId, "test", "test");
		Session session =myServer.getSession(user);
		return session;
	}
	
	@Test
	public void testGetSessionWithMoreSessions(){
		createSession("1");
		Session session=createSession("2");
		assertEquals(2, session.getSessionId());
	}
	
	@Test
	public void testGetSessionAddElementsInList(){
		createSession("1");
		createSession("2");
		assertEquals(2, myServer.getListSize());
	}
}
