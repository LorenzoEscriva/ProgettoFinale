package com.tap.project.escrivaghera.AccountantApp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {
	private Server myServer;

	@Before
	public void setUp() throws Exception {
		myServer = new Server(null);
	}

	@Test
	public void testGetSessionWithFirstSession() {
		Session session = createSession("1");
		assertEquals(1, session.getSessionId());
	}

	public Session createSession(String userId) {
		User user = new User(userId, "test", "test");
		Session session = myServer.getSession(user);
		return session;
	}

	@Test
	public void testGetSessionWithMoreSessions() {
		createSession("1");
		Session session = createSession("2");
		assertEquals(2, session.getSessionId());
	}

	@Test
	public void testGetSessionAddElementsInList() {
		createSession("1");
		createSession("2");
		assertEquals(2, myServer.getList().size());
	}

	@Test
	public void testInterationWithSession() {
		Session mySession = createSession("1");
		mySession.addDescription("delete record with id 0001");
		mySession = myServer.getList().get(0);
		String description = mySession.toString();
		String expected = "The user 1 test test makes this actions:\nIn the session 1, the user 1 test test "
				+ "delete record with id 0001\n";
		assertEquals(expected, description);
	}
}
