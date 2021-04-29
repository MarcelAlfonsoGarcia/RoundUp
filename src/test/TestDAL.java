package test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import database.DAL;

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
	private static final int EVENT_ONE_OWNER = 1;
	private static final Timestamp EVENT_ONE_TIME = Timestamp.valueOf("2022-04-04 14:55:10.888");;
	private static final String EVENT_ONE_POSTER_URL = "url1";
	private static final String EVENT_ONE_NAME = "Math Colloquium";
	private static final String EVENT_ONE_DESCRIPTION = "There is a thingy with math";
	private static final String EVENT_ONE_LOCATION = "Estella";
	private static final int EVENT_ONE_POPULARITY = 23;
	private static final String EVENT_ONE_STATUS = "active";
	private static final Set<String> EVENT_ONE_TAGS = new HashSet<String>(Arrays.asList("Mathematics", "Colloquium"));

	private static int eventTwoId = 2;
	private static final int EVENT_TWO_OWNER = 2;
	private static final Timestamp EVENT_TWO_TIME = Timestamp.valueOf("2022-04-04 14:55:15.888");
	private static final String EVENT_TWO_POSTER_URL = "url2";
	private static final String EVENT_TWO_NAME = "CS Colloquium";
	private static final String EVENT_TWO_DESCRIPTION = "There is a thingy with cs";
	private static final String EVENT_TWO_LOCATION = "Edmunds";
	private static final int EVENT_TWO_POPULARITY = 54;
	private static final String EVENT_TWO_STATUS = "active";
	private static final Set<String> EVENT_TWO_TAGS = new HashSet<String>(
			Arrays.asList("Computer Science", "Colloquium"));

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
		eventOne.put("owner", EVENT_ONE_OWNER);
		eventOne.put("eventTime", EVENT_ONE_TIME);
		eventOne.put("posterUrl", EVENT_ONE_POSTER_URL);
		eventOne.put("name", EVENT_ONE_NAME);
		eventOne.put("description", EVENT_ONE_DESCRIPTION);
		eventOne.put("location", EVENT_ONE_LOCATION);
		eventOne.put("popularity", EVENT_ONE_POPULARITY);
		eventOne.put("status", EVENT_ONE_STATUS);

		JSONArray eventOneTags = new JSONArray();
		for (String tag : EVENT_ONE_TAGS) {
			eventOneTags.add(tag);
		}
		eventOne.put("tags", eventOneTags);
		
		eventTwo = new JSONObject();
		eventTwo.put("eID", eventTwoId);
		eventTwo.put("owner", EVENT_TWO_OWNER);
		eventTwo.put("eventTime", EVENT_TWO_TIME);
		eventTwo.put("posterUrl", EVENT_TWO_POSTER_URL);
		eventTwo.put("name", EVENT_TWO_NAME);
		eventTwo.put("description", EVENT_TWO_DESCRIPTION);
		eventTwo.put("location", EVENT_TWO_LOCATION);
		eventTwo.put("popularity", EVENT_TWO_POPULARITY);
		eventTwo.put("status", EVENT_TWO_STATUS);

		JSONArray eventTwoTags = new JSONArray();
		for (String tag : EVENT_TWO_TAGS) {
			eventTwoTags.add(tag);
		}
		eventTwo.put("tags", eventTwoTags);
		
		dal = DAL.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void createValidUser() {
		assertEquals(userOne, dal.createUser(USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS));
		assertEquals(userTwo, dal.createUser(USR_TWO_FIRST_NAME, USR_TWO_SECOND_NAME, USR_TWO_EMAIL, USR_TWO_CAMPUS));
	}

	@Test
	public void createNullNameUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("First Name cannot be left empty");
		dal.createUser("", USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Last Name cannot be left empty");
		dal.createUser(USR_ONE_FIRST_NAME, "", USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void createNullEmailUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email cannot be left empty");

		dal.createUser(USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, "", USR_ONE_CAMPUS);
	}

	@Test
	public void createExistingUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("The email provided is already in use");

		dal.createUser(USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void retrieveExistingUser() {
		assertEquals(userOne, dal.retrieveUser(usrOneId));
	}

	@Test
	public void retrieveUnknownUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No user exists under the provided information");

		dal.retrieveUser(0);
	}

	@Test
	public void deleteExistingUser() {
		// TODO: verify deletion
		dal.deleteUser(usrOneId);
		dal.deleteUser(usrTwoId);
	}

	@Test
	public void deleteUnknownUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No user exists under the provided information");

		dal.deleteUser(0);
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

		assertEquals(updatedUser, dal.updateUser(usrOneId, "Jack", "Savage", "jack.savage@email.com", "CMC"));
		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void updateUnknownUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No user exists under the provided information");

		dal.updateUser(0, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, "", USR_ONE_CAMPUS);
	}

	@Test
	public void updateUserNullName() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("First Name cannot be left empty");

		dal.updateUser(usrOneId, "", USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Last Name cannot be left empty");

		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, "", USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void updateUserNullEmail() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email cannot be left empty");

		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, "", USR_ONE_CAMPUS);
	}

	@Test
	public void updateUserExistingEmail() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("The email provided is already in use");

		dal.updateUser(usrOneId, USR_ONE_FIRST_NAME, USR_ONE_SECOND_NAME, USR_ONE_EMAIL, USR_ONE_CAMPUS);
	}

	@Test
	public void createValidEvent() {
		assertEquals(eventOne, dal.createEvent(EVENT_ONE_OWNER, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL,
				EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION, EVENT_ONE_LOCATION, EVENT_ONE_TAGS));
		assertEquals(eventOne, dal.createEvent(EVENT_TWO_OWNER, EVENT_TWO_TIME, EVENT_TWO_POSTER_URL,
				EVENT_TWO_NAME, EVENT_TWO_DESCRIPTION, EVENT_TWO_LOCATION, EVENT_TWO_TAGS));
	}

	@Test
	public void createNullParamsEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Event Time cannot be left empty");

		dal.createEvent(EVENT_ONE_OWNER, null, EVENT_ONE_POSTER_URL, EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION,
				EVENT_ONE_LOCATION, EVENT_ONE_TAGS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Poster must be uploaded");

		dal.createEvent(EVENT_ONE_OWNER, EVENT_ONE_TIME, "", EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION, EVENT_ONE_LOCATION,
				EVENT_ONE_TAGS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Event Name cannot be left empty");

		dal.createEvent(EVENT_ONE_OWNER, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL, "", EVENT_ONE_DESCRIPTION,
				EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
	}

	@Test
	public void createExistingEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot create duplicate event");

		dal.createEvent(EVENT_ONE_OWNER, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL, EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION,
				EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
	}

	@Test
	public void retrieveExistingEvent() {
		assertEquals(eventOne, dal.retrieveEvent(eventOneId));
	}

	@Test
	public void retrieveUnknownEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No event exists under the provided information");

		dal.retrieveEvent(0);
	}

	@Test
	public void deleteEvents() {
		// TODO: verify deletion
		dal.deleteEvent(eventOneId, usrOneId);
		dal.deleteEvent(eventTwoId, usrTwoId);
	}

	@Test
	public void deleteUnknownEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No event exists under the provided information");

		dal.deleteEvent(eventOneId, usrOneId);
		dal.deleteEvent(eventTwoId, eventTwoId);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateEvent() {
		JSONObject updatedEvent = new JSONObject();
		updatedEvent.put("eID", eventOneId);
		updatedEvent.put("owner", EVENT_ONE_OWNER);
		updatedEvent.put("eventTime", Timestamp.valueOf("2022-04-05 14:55:15.888"));
		updatedEvent.put("posterUrl", EVENT_ONE_POSTER_URL);
		updatedEvent.put("name", "New Name");
		updatedEvent.put("description", "New Description");
		updatedEvent.put("location", "New Location");
		updatedEvent.put("popularity", EVENT_ONE_POPULARITY);
		updatedEvent.put("status", EVENT_ONE_STATUS);

		assertEquals(updatedEvent, dal.updateEvent(eventOneId, EVENT_ONE_OWNER, "New Description",
				Timestamp.valueOf("2022-04-05 14:55:15.888"), "New Name", "New Location", null));

		dal.updateEvent(eventOneId, EVENT_ONE_OWNER, EVENT_ONE_DESCRIPTION, EVENT_ONE_TIME, EVENT_ONE_NAME,
				EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
	}

	@Test
	public void updateNullParamsEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Event Time cannot be left empty");

		dal.updateEvent(eventOneId, EVENT_ONE_OWNER, EVENT_ONE_DESCRIPTION, null, EVENT_ONE_NAME, EVENT_ONE_LOCATION,
				EVENT_ONE_TAGS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Event Name cannot be left empty");

		dal.updateEvent(eventOneId, EVENT_ONE_OWNER, EVENT_ONE_DESCRIPTION, EVENT_ONE_TIME, "", EVENT_ONE_LOCATION,
				EVENT_ONE_TAGS);
	}

	@Test
	public void updateUnknownEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot find target event");

		dal.updateEvent(0, EVENT_ONE_OWNER, EVENT_ONE_DESCRIPTION, EVENT_ONE_TIME, EVENT_ONE_NAME, EVENT_ONE_LOCATION,
				EVENT_ONE_TAGS);
	}

	@Test
	public void testUpdateEventStatus() {
		fail("Not yet implemented");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveAllTags() {
		JSONObject tags = new JSONObject();
		JSONArray tagList = new JSONArray();

		tagList.add("Pomona");
		tagList.add("CMC");
		tagList.add("HMC");
		tagList.add("Scripps");
		tagList.add("Pitzer");
		tagList.add("Africana Studies");
		tagList.add("American Studies");
		tagList.add("Anthropology");
		tagList.add("Art");
		tagList.add("Art History");
		tagList.add("Asian American Studies");
		tagList.add("Asian Languages & Literatures");
		tagList.add("Asian Studies");
		tagList.add("Biology");
		tagList.add("Chemistry");
		tagList.add("Chicana/o-Latina/o Studies");
		tagList.add("Chinese");
		tagList.add("Classics");
		tagList.add("Computer Science");
		tagList.add("Economics");
		tagList.add("English");
		tagList.add("Environmental Analysis");
		tagList.add("French");
		tagList.add("Gender & Women Studies");
		tagList.add("Geology");
		tagList.add("German Studies");
		tagList.add("History");
		tagList.add("International Relations");
		tagList.add("Japanese");
		tagList.add("Late Antique-Medieval Studies");
		tagList.add("Latin American Studies");
		tagList.add("Linguistics & Cognitive Science");
		tagList.add("Mathematics");
		tagList.add("Media Studies");
		tagList.add("Middle Eastern Studies");
		tagList.add("Molecular Biology");
		tagList.add("Music");
		tagList.add("Neuroscience");
		tagList.add("Philosophy");
		tagList.add("Philosophy, Politics & Economics");
		tagList.add("Physical Education");
		tagList.add("Physics & Astronomy");
		tagList.add("Politics");
		tagList.add("Psychological Science");
		tagList.add("Public Policy Analysis");
		tagList.add("Religious Studies");
		tagList.add("Romance Languages & Literatures");
		tagList.add("Russian");
		tagList.add("Russian & Eastern European Studies");
		tagList.add("Science, Technology, & Society");
		tagList.add("Sociology");
		tagList.add("Spanish");
		tagList.add("Theatre");
		tagList.add("Dance");
		tagList.add("Colloquium");
		tagList.add("Commencement");
		tagList.add("Campus-Wide");
		tagList.add("Mandatory");
		tagList.add("Important");
		tagList.add("Festival");
		tagList.add("Concert");
		tagList.add("Play");
		tagList.add("Relaxation");
		tagList.add("Lunch");
		tagList.add("Breakfast");
		tagList.add("Dinner");
		tagList.add("Brunch");
		tagList.add("Snack");
		tagList.add("Seniors");
		tagList.add("Juniors");
		tagList.add("Freshmen");
		tagList.add("Sophomores");
		tagList.add("Outdoors");
		tagList.add("Indoors");
		tagList.add("OA");
		tagList.add("On-Campus");
		tagList.add("Off-Campus");
		tagList.add("Online");
		tagList.add("Talk");
		tagList.add("Party");
		tagList.add("Guest Speaker/s");
		tags.put("tags", tagList);
		
		assertEquals(tags, dal.retrieveAllTags());
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
