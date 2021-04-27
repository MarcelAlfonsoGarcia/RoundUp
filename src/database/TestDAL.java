package database;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDAL {

	private static int usrOneId;
	private static final String USR_ONE_FIRST_NAME = "John";
	private static final String USR_ONE_SECOND_NAME = "Doe";
	private static final String USR_ONE_EMAIL = "john.doe@pomona.edu";
	private static final String USR_ONE_CAMPUS = "POM";
	
	private static int usrTwoId;
	private static final String USR_TWO_FIRST_NAME = "Sue";
	private static final String USR_TWO_SECOND_NAME = "Storm";
	private static final String USR_TWO_EMAIL = "sue.storm@hmc.edu";
	private static final String USR_TWO_CAMPUS = "HMC";
	
	private static int eventOneId;
	private static final int EVENT_ONE_ONWER = 1;
	private static final Timestamp EVENT_ONE_TIME = Timestamp.valueOf("2022-04-04 14:55:10.888");;
	private static final String EVENT_ONE_POSTER_URL = "url1";
	private static final String EVENT_ONE_NAME = "Math Colloquium";
	private static final String EVENT_ONE_DESCRIPTION = "There is a thingy with math";
	private static final String EVENT_ONE_LOCATION = "Estella";
	private static final int EVENT_ONE_POPULARITY = 23;
	private static final String EVENT_ONE_STATUS = "active";
	private static final List<String> EVENT_ONE_TAGS = Arrays.asList("Math","Colloquium");

	private static int eventTwoId = 2;
	private static final int EVENT_TWO_ONWER = 2;
	private static final Timestamp EVENT_TWO_TIME = Timestamp.valueOf("2022-04-04 14:55:15.888");
	private static final String EVENT_TWO_POSTER_URL = "url2";
	private static final String EVENT_TWO_NAME = "CS Colloquium";
	private static final String EVENT_TWO_DESCRIPTION = "There is a thingy with cs";
	private static final String EVENT_TWO_LOCATION = "Edmunds";
	private static final int EVENT_TWO_POPULARITY = 54;
	private static final String EVENT_TWO_STATUS = "active";
	private static final List<String> EVENT_TWO_TAGS = Arrays.asList("Computer Science","Colloquium");
	
	private static JSONObject userOne;
	private static JSONObject userTwo;

	private static JSONObject eventOne;
	private static JSONObject eventTwo;

	private static DAL dal;
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userOne = new JSONObject();
		userOne.put("uID", usrOneId);
		userOne.put("firstName", USR_ONE_FIRST_NAME);
		userOne.put("lastName", USR_ONE_SECOND_NAME);
		userOne.put("email", USR_ONE_EMAIL);
		userOne.put("campus", USR_ONE_CAMPUS);
		
		userTwo = new JSONObject();
		userTwo.put("uID", usrTwoId);
		userTwo.put("firstName", USR_TWO_FIRST_NAME);
		userTwo.put("lastName", USR_TWO_SECOND_NAME);
		userTwo.put("email", USR_TWO_EMAIL);
		userTwo.put("campus", USR_TWO_CAMPUS);
		
		eventOne = new JSONObject();
		eventOne.put("eID", eventOneId);
		eventOne.put("owner", EVENT_ONE_ONWER);
		eventOne.put("eventTime", EVENT_ONE_TIME);
		eventOne.put("posterUrl", EVENT_ONE_POSTER_URL);
		eventOne.put("name", EVENT_ONE_NAME);
		eventOne.put("description", EVENT_ONE_DESCRIPTION);
		eventOne.put("location", EVENT_ONE_LOCATION);
		eventOne.put("popularity", EVENT_ONE_POPULARITY);
		eventOne.put("status", EVENT_ONE_STATUS);
		
		eventTwo = new JSONObject();
		eventTwo.put("eID", eventTwoId);
		eventTwo.put("owner", EVENT_TWO_ONWER);
		eventTwo.put("eventTime", EVENT_TWO_TIME);
		eventTwo.put("posterUrl", EVENT_TWO_POSTER_URL);
		eventTwo.put("name", EVENT_TWO_NAME);
		eventTwo.put("description", EVENT_TWO_DESCRIPTION);
		eventTwo.put("location", EVENT_TWO_LOCATION);
		eventTwo.put("popularity", EVENT_TWO_POPULARITY);
		eventTwo.put("status", EVENT_TWO_STATUS);
		
		dal = DAL.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void createValidUser() {
		assertEquals(userOne,dal.createUser(USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS));
		assertEquals(userTwo,dal.createUser(USR_TWO_FIRST_NAME, USR_TWO_SECOND_NAME, USR_TWO_EMAIL, USR_TWO_CAMPUS));
	}
	
	@Test
	public void createNullNameUser() {
		dal.createUser("", USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
		dal.createUser(USR_ONE_FIRST_NAME, "", USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}
	
	@Test
	public void createNullEmailUser() {
		dal.createUser(USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, "", USR_ONE_CAMPUS);
	}
	
	@Test
	public void createExistingUser() {
		dal.createUser(USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void retrieveExistingUser() {
		assertEquals(userOne,dal.retrieveUser(usrOneId));
	}

	@Test
	public void retrieveUnknownUser() {
		dal.retrieveUser(0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateExistingUser() {
		JSONObject updatedUser = new JSONObject();
		updatedUser.put("uID", usrOneId);
		updatedUser.put("firstName", "Jack");
		updatedUser.put("lastName", "Savage");
		updatedUser.put("email", "jack.savage@email.com");
		updatedUser.put("campus", "CMC");
		
		assertEquals(updatedUser,dal.updateUser(usrOneId, "Jack", "Savage", "jack.savage@email.com", "CMC"));
		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}
	
	@Test
	public void updateUserNullName() {
		dal.updateUser(usrOneId, "", USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, "", USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}
	
	@Test
	public void updateUserNullEmail() {
		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, "", USR_ONE_CAMPUS);
	}
	
	@Test
	public void updateUserExistingEmail() {
		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void testCreateEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateEventStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveAllTags() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEventsByTag() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEventsByOwner() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEventsByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEventsByTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubscribeTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnsubscribeTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveSubscriptions() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveSubscribers() {
		fail("Not yet implemented");
	}

	@Test
	public void testRsvpTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnRsvpFrom() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveRsvpdEvents() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveAttendees() {
		fail("Not yet implemented");
	}

}
