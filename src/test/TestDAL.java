import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import database.DAL;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDAL {

	private static int userOneId;
	private static final String USER_ONE_FIRST_NAME = "John";
	private static final String USER_ONE_LAST_NAME = "Doe";
	private static final String USER_ONE_EMAIL = "john.doe@pomona.edu";
	private static final String USER_ONE_PASSWORD = "password";
	private static final String USER_ONE_CAMPUS = "POM";

	private static int userTwoId;
	private static final String USER_TWO_FIRST_NAME = "Sue";
	private static final String USER_TWO_LAST_NAME = "Storm";
	private static final String USER_TWO_EMAIL = "sue.storm@hmc.edu";
	private static final String USER_TWO_PASSWORD = "1234";
	private static final String USER_TWO_CAMPUS = "HMC";

	private static int eventOneId;
	private static final Timestamp EVENT_ONE_TIME = Timestamp.valueOf("2022-04-04 14:55:10.888");
	private static final String EVENT_ONE_POSTER_URL = "url1";
	private static final String EVENT_ONE_NAME = "Math Colloquium";
	private static final String EVENT_ONE_DESCRIPTION = "There is a thingy with math";
	private static final String EVENT_ONE_LOCATION = "Estella";
	private static final int EVENT_ONE_POPULARITY = 0;
	private static final String EVENT_ONE_STATUS = "active";
	private static final Set<String> EVENT_ONE_TAGS = new HashSet<String>(Arrays.asList("Mathematics", "Colloquium", "test"));

	private static int eventTwoId;
	private static final Timestamp EVENT_TWO_TIME = Timestamp.valueOf("2023-04-04 14:55:15.888");
	private static final String EVENT_TWO_POSTER_URL = "url2";
	private static final String EVENT_TWO_NAME = "CS Colloquium";
	private static final String EVENT_TWO_DESCRIPTION = "There is a thingy with cs";
	private static final String EVENT_TWO_LOCATION = "Edmunds";
	private static final int EVENT_TWO_POPULARITY = 0;
	private static final String EVENT_TWO_STATUS = "active";
	private static final Set<String> EVENT_TWO_TAGS = new HashSet<String>(
			Arrays.asList("Computer Science", "Colloquium"));

	private static JSONObject userOne;
	private static JSONObject userTwo;

	private static JSONObject eventOne;
	private static JSONObject eventTwo;

	private static JSONObject userSearchResult;
	private static JSONObject eventSearchResult;

	private static DAL dal;

	@SuppressWarnings("unchecked")
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		userOne = new JSONObject();
		userOne.put("firstName", USER_ONE_FIRST_NAME);
		userOne.put("lastName", USER_ONE_LAST_NAME);
		userOne.put("email", USER_ONE_EMAIL);
		userOne.put("campus", USER_ONE_CAMPUS);

		userTwo = new JSONObject();
		userTwo.put("uID", userTwoId);
		userTwo.put("firstName", USER_TWO_FIRST_NAME);
		userTwo.put("lastName", USER_TWO_LAST_NAME);
		userTwo.put("email", USER_TWO_EMAIL);
		userTwo.put("campus", USER_TWO_CAMPUS);

		dal = DAL.getInstance();
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Order(1)
	@Test
	public void createValidUser() {
		JSONObject userOneDb = dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL,
				USER_ONE_PASSWORD, USER_ONE_CAMPUS);
		JSONObject userTwoDb = dal.createUser(USER_TWO_FIRST_NAME, USER_TWO_LAST_NAME, USER_TWO_EMAIL,
				USER_TWO_PASSWORD, USER_TWO_CAMPUS);
		assertTrue(compareUsers(userOne, userOneDb));
		assertTrue(compareUsers(userTwo, userTwoDb));
		
		userOneId = (int) userOneDb.get("uID");
		userTwoId = (int) userTwoDb.get("uID");

		eventOne = new JSONObject();
		eventOne.put("owner", userOneId);
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
		eventTwo.put("owner", userTwoId);
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

		userSearchResult = new JSONObject();
		JSONArray userResults = new JSONArray();
		JSONObject user = new JSONObject();
		user.put("uID", userOneId);
		user.put("firstName", USER_ONE_FIRST_NAME);
		user.put("lastName", USER_ONE_LAST_NAME);
		userResults.add(user);
		userSearchResult.put("users", userResults);
	}

	@Test
	@Order(2)
	public void createNullNameUser() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createUser("", USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_PASSWORD, USER_ONE_CAMPUS);
		});
		assertEquals("First Name cannot be left empty", e.getMessage());

		e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createUser(USER_ONE_FIRST_NAME, "", USER_ONE_EMAIL, USER_ONE_PASSWORD, USER_ONE_CAMPUS);
		});
		assertEquals("Last Name cannot be left empty", e.getMessage());
	}

	@Test
	@Order(3)
	public void createNullLoginUser() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, "", USER_ONE_PASSWORD, USER_ONE_CAMPUS);
		});
		assertEquals("Email cannot be left empty", e.getMessage());

		e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_TWO_EMAIL, "", USER_ONE_CAMPUS);
		});
		assertEquals("Password cannot be left empty", e.getMessage());
	}

	@Test
	@Order(4)
	public void createExistingUser() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_TWO_PASSWORD, USER_ONE_CAMPUS);
		});

		assertTrue(e.getMessage().contains("Detail: Key (email)=(" + USER_ONE_EMAIL + ") already exists."));
	}

	@Test
	@Order(5)
	public void logInExistingUser() {
		assertTrue(compareUsers(userOne, dal.logInUser(USER_ONE_EMAIL, USER_ONE_PASSWORD)));
		assertTrue(compareUsers(userTwo, dal.logInUser(USER_TWO_EMAIL, USER_TWO_PASSWORD)));
	}

	@Test
	@Order(6)
	public void logInBadFields() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.logInUser("jonlol", USER_ONE_PASSWORD);
		});
		assertEquals("User was not found in the database", e.getMessage());

		e = assertThrows(NullPointerException.class, () -> {
			dal.logInUser(USER_ONE_EMAIL, "password1");
		});
		assertEquals("User was not found in the database", e.getMessage());
	}

	@Test
	@Order(7)
	public void retrieveExistingUser() {
		assertTrue(compareUsers(userOne, dal.retrieveUser(userOneId)));
	}

	@Test
	@Order(8)
	public void retrieveUnknownUser() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.retrieveUser(0);
		});
		assertEquals("User was not found in the database", e.getMessage());
	}

	@Test
	@Order(44)
	public void deleteExistingUser() {
		dal.deleteUser(userOneId);
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.retrieveUser(userOneId);
		});
		assertEquals("User was not found in the database", e.getMessage());
		
		dal.deleteUser(userTwoId);
		e = assertThrows(NullPointerException.class, () -> {
			dal.retrieveUser(userTwoId);
		});
		assertEquals("User was not found in the database", e.getMessage());
	}

	@Test
	@Order(45)
	public void deleteUnknownUser() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.deleteUser(0);
		});
		assertEquals("No user exists under the provided information", e.getMessage());
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(9)
	public void updateExistingUser() {
		JSONObject updatedUser = new JSONObject();
		updatedUser.put("uID", userOneId);
		updatedUser.put("firstName", "Jack");
		updatedUser.put("lastName", "Savage");
		updatedUser.put("email", "jack.savage@email.com");
		updatedUser.put("campus", "CMC");

		assertTrue(
				compareUsers(updatedUser, dal.updateUser(userOneId, "Jack", "Savage", "jack.savage@email.com", "CMC")));
		dal.updateUser(userOneId, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);
	}

	@Test
	@Order(10)
	public void updateUnknownUser() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.updateUser(0, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);
		});

		assertEquals("User was not found in the database", e.getMessage());
	}

	@Test
	@Order(11)
	public void updateUserNullName() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.updateUser(userOneId, "", USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);
		});
		assertEquals("First Name cannot be left empty", e.getMessage());

		e = assertThrows(IllegalArgumentException.class, () -> {
			dal.updateUser(userOneId, USER_ONE_FIRST_NAME, "", USER_ONE_EMAIL, USER_ONE_CAMPUS);
		});
		assertEquals("Last Name cannot be left empty", e.getMessage());
	}

	@Test
	@Order(12)
	public void updateUserNullEmail() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.updateUser(userOneId, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, "", USER_ONE_CAMPUS);
		});
		assertEquals("Email cannot be left empty", e.getMessage());
	}

	@Test
	@Order(13)
	public void updateUserExistingEmail() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.updateUser(userOneId, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_TWO_EMAIL, USER_ONE_CAMPUS);
		});
		assertTrue(e.getMessage().contains("Detail: Key (email)=(" + USER_TWO_EMAIL + ") already exists."));
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(14)
	public void createValidEvent() {
		JSONObject eventOneDb = dal.createEvent(userOneId, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL,
				EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION, EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		JSONObject eventTwoDb = dal.createEvent(userTwoId, EVENT_TWO_TIME, EVENT_TWO_POSTER_URL,
				EVENT_TWO_NAME, EVENT_TWO_DESCRIPTION, EVENT_TWO_LOCATION, EVENT_TWO_TAGS);
		eventOneId = (int) eventOneDb.get("eID");
		eventTwoId = (int) eventTwoDb.get("eID");
		
		eventSearchResult = new JSONObject();
		JSONArray eventResults = new JSONArray();
		JSONObject event = new JSONObject();
		event.put("eID", eventOneId);
		event.put("owner", userOneId);
		event.put("name", EVENT_ONE_NAME);
		event.put("posterUrl", EVENT_ONE_POSTER_URL);
		eventResults.add(event);
		eventSearchResult.put("events", eventResults);
		
		assertTrue(compareEvents(eventOne, eventOneDb));
		assertTrue(compareEvents(eventTwo, eventTwoDb));
	}

	@Test
	@Order(15)
	public void createNullParamsEvent() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createEvent(userOneId, null, EVENT_ONE_POSTER_URL, EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION,
					EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		});
		assertEquals("Event Time cannot be left empty", e.getMessage());

		e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createEvent(userOneId, EVENT_ONE_TIME, "", EVENT_ONE_NAME, EVENT_ONE_DESCRIPTION,
					EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		});
		assertEquals("Poster must be uploaded", e.getMessage());

		e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createEvent(userOneId, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL, "", EVENT_ONE_DESCRIPTION,
					EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		});
		assertEquals("Event Name cannot be left empty", e.getMessage());
	}

	@Test
	@Order(16)
	public void createExistingEvent() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.createEvent(userOneId, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL, EVENT_ONE_NAME,
					EVENT_ONE_DESCRIPTION, EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		});
		assertTrue(e.getMessage().contains("Detail: Key (posterurl)=(" + EVENT_ONE_POSTER_URL + ") already exists."));
	}

	@Test
	@Order(17)
	public void retrieveExistingEvent() {
		assertTrue(compareEvents(eventOne, dal.retrieveEvent(eventOneId)));
	}

	@Test
	@Order(18)
	public void retrieveUnknownEvent() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.retrieveEvent(0);
		});
		assertEquals(e.getMessage(), "Event was not found in the database");
	}

	@Test
	@Order(42)
	public void deleteEvents() {
		dal.deleteEvent(eventOneId, userOneId);
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.retrieveEvent(eventOneId);
		});
		assertEquals(e.getMessage(), "Event was not found in the database");

		
		dal.deleteEvent(eventTwoId, userTwoId);
		e = assertThrows(NullPointerException.class, () -> {
			dal.retrieveEvent(eventTwoId);
		});
		assertEquals(e.getMessage(), "Event was not found in the database");
	}

	@Test
	@Order(43)
	public void deleteUnknownEvent() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.deleteEvent(eventOneId, userOneId);
		});
		assertEquals("No event exists under the provided information", e.getMessage());

		e = assertThrows(NullPointerException.class, () -> {
			dal.deleteEvent(eventTwoId, userTwoId);
		});
		assertEquals("No event exists under the provided information", e.getMessage());
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(19)
	public void updateEvent() {
		JSONObject updatedEvent = new JSONObject();
		updatedEvent.put("eID", eventOneId);
		updatedEvent.put("owner", userOneId);
		updatedEvent.put("eventTime", Timestamp.valueOf("2022-04-05 14:55:15.888"));
		updatedEvent.put("posterUrl", EVENT_ONE_POSTER_URL);
		updatedEvent.put("name", "New Name");
		updatedEvent.put("description", "New Description");
		updatedEvent.put("location", "New Location");
		updatedEvent.put("popularity", EVENT_ONE_POPULARITY);
		updatedEvent.put("status", EVENT_ONE_STATUS);
		JSONArray tagArr = new JSONArray();
		tagArr.add("Online");
		tagArr.add("Sport");
		updatedEvent.put("tags", tagArr);
		
		Set<String> newTags = new HashSet<String>(Arrays.asList("Online", "Sport"));
		assertTrue(compareEvents(updatedEvent, dal.updateEvent(eventOneId, userOneId, "New Description",
				Timestamp.valueOf("2022-04-05 14:55:15.888"), "New Name", "New Location", newTags)));

		dal.updateEvent(eventOneId, userOneId, EVENT_ONE_DESCRIPTION, EVENT_ONE_TIME, EVENT_ONE_NAME,
				EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
	}

	@Test
	@Order(20)
	public void updateNullParamsEvent() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.updateEvent(eventOneId, userOneId, EVENT_ONE_DESCRIPTION, null, EVENT_ONE_NAME,
					EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		});
		assertEquals("Event Time cannot be left empty", e.getMessage());

		e = assertThrows(IllegalArgumentException.class, () -> {
			dal.updateEvent(eventOneId, userOneId, EVENT_ONE_DESCRIPTION, EVENT_ONE_TIME, "", EVENT_ONE_LOCATION,
					EVENT_ONE_TAGS);
		});
		assertEquals("Event Name cannot be left empty", e.getMessage());
	}

	@Test
	@Order(21)
	public void updateUnknownEvent() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.updateEvent(0, userOneId, EVENT_ONE_DESCRIPTION, EVENT_ONE_TIME, EVENT_ONE_NAME,
					EVENT_ONE_LOCATION, EVENT_ONE_TAGS);
		});
		assertTrue(e.getMessage().equals("Event was not found in the database"));
	}

	@Test
	@Order(22)
	public void testUpdateEventStatus() {
		//TODO: check status change
		dal.updateEventStatus("inactive", Timestamp.valueOf("2022-04-03 14:55:10.888"),
				Timestamp.valueOf("2022-04-05 14:55:10.888"));
		dal.updateEventStatus("active", Timestamp.valueOf("2022-04-03 14:55:10.888"),
				Timestamp.valueOf("2022-04-05 14:55:10.888"));
	}

	@Test
	@Order(23)
	public void testRetrieveAllTags() {
		Set<String> tagSet = new HashSet<String>();
		tagSet.add("Pomona");
		tagSet.add("CMC");
		tagSet.add("HMC");
		tagSet.add("Scripps");
		tagSet.add("Pitzer");
		tagSet.add("Africana Studies");
		tagSet.add("American Studies");
		tagSet.add("Anthropology");
		tagSet.add("Art");
		tagSet.add("Art History");
		tagSet.add("Asian American Studies");
		tagSet.add("Asian Languages & Literatures");
		tagSet.add("Asian Studies");
		tagSet.add("Biology");
		tagSet.add("Chemistry");
		tagSet.add("Chicana/o-Latina/o Studies");
		tagSet.add("Chinese");
		tagSet.add("Classics");
		tagSet.add("Computer Science");
		tagSet.add("Economics");
		tagSet.add("English");
		tagSet.add("Environmental Analysis");
		tagSet.add("French");
		tagSet.add("Gender & Women Studies");
		tagSet.add("Geology");
		tagSet.add("German Studies");
		tagSet.add("History");
		tagSet.add("International Relations");
		tagSet.add("Japanese");
		tagSet.add("Late Antique-Medieval Studies");
		tagSet.add("Latin American Studies");
		tagSet.add("Linguistics & Cognitive Science");
		tagSet.add("Mathematics");
		tagSet.add("Media Studies");
		tagSet.add("Middle Eastern Studies");
		tagSet.add("Molecular Biology");
		tagSet.add("Music");
		tagSet.add("Neuroscience");
		tagSet.add("Philosophy");
		tagSet.add("Philosophy, Politics & Economics");
		tagSet.add("Physical Education");
		tagSet.add("Physics & Astronomy");
		tagSet.add("Politics");
		tagSet.add("Psychological Science");
		tagSet.add("Public Policy Analysis");
		tagSet.add("Religious Studies");
		tagSet.add("Romance Languages & Literatures");
		tagSet.add("Russian");
		tagSet.add("Russian & Eastern European Studies");
		tagSet.add("Science, Technology, & Society");
		tagSet.add("Sociology");
		tagSet.add("Spanish");
		tagSet.add("Theatre");
		tagSet.add("Dance");
		tagSet.add("Colloquium");
		tagSet.add("Commencement");
		tagSet.add("Campus-Wide");
		tagSet.add("Mandatory");
		tagSet.add("Important");
		tagSet.add("Festival");
		tagSet.add("Concert");
		tagSet.add("Play");
		tagSet.add("Relaxation");
		tagSet.add("Lunch");
		tagSet.add("Breakfast");
		tagSet.add("Dinner");
		tagSet.add("Brunch");
		tagSet.add("Snack");
		tagSet.add("Seniors");
		tagSet.add("Juniors");
		tagSet.add("Freshmen");
		tagSet.add("Sophomores");
		tagSet.add("Outdoors");
		tagSet.add("Indoors");
		tagSet.add("OA");
		tagSet.add("On-Campus");
		tagSet.add("Off-Campus");
		tagSet.add("Online");
		tagSet.add("Talk");
		tagSet.add("Party");
		tagSet.add("Guest Speaker/s");
		tagSet.add("test");

		JSONObject tagsJson = dal.retrieveAllTags();
		JSONArray tagsArray = (JSONArray) tagsJson.get("tags");

		boolean fullyContained = true;
		for (int i = 0; i < tagsArray.size(); i++) {
			fullyContained = fullyContained && tagSet.contains((String) tagsArray.get(i));
		}
		assertTrue(fullyContained);
	}

	@Test
	@Order(24)
	public void retrieveEventsByKnownTags() {
		Set<String> mathTags = new HashSet<String>(Arrays.asList("test"));
		
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByTag(mathTags, "")));
	}

	@Test
	@Order(25)
	public void retrieveEventsByKnownOwner() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByOwner(userOneId, "")));
	}

	@Test
	@Order(26)
	public void retrieveEventByName() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByName("Mat", "")));
	}

	@Test
	@Order(27)
	public void retrieveEventByTime() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByTime(
				Timestamp.valueOf("2022-04-03 14:55:10.888"), Timestamp.valueOf("2022-04-05 14:55:10.888"))));
	}

	@Test
	@Order(28)
	public void subscribeToKnownUser() {
		dal.subscribeTo(userOneId, userTwoId);
		dal.subscribeTo(userTwoId, userOneId);
	}

	@Test
	@Order(29)
	public void subscribeToUnknownUser() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.subscribeTo(userOneId, 0);
		});
		assertTrue(e.getMessage().contains("Detail: Key (followedid)=(0) is not present in table \"users\"."));
	}

	@Test
	@Order(30)
	public void subscribeTwice() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.subscribeTo(userOneId, userTwoId);
		});
		String expectedError = "Detail: Key (followerid, followedid)=(" + userOneId + ", " + userTwoId + ") already exists.";
		assertTrue(e.getMessage().contains(expectedError));
	}

	@Test
	@Order(31)
	public void retrieveSubscriptionsFromUser() {
		assertTrue(compareUserLists(userSearchResult, dal.retrieveSubscriptions(userTwoId)));
	}

	@Test
	@Order(32)
	public void retrieveSubscribersFromUser() {
		assertTrue(compareUserLists(userSearchResult, dal.retrieveSubscribers(userTwoId)));
	}

	@Test
	@Order(33)
	public void unsubcribeFromExistingRelationship() {
		dal.unsubscribeFrom(userOneId, userTwoId);
		dal.unsubscribeFrom(userTwoId, userOneId);
	}

	@Test
	@Order(34)
	public void unsubcribeFromUnknownRelationship() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.unsubscribeFrom(userOneId, userTwoId);
		});
		assertEquals("Cannot unsubscribe from unknown relationship", e.getMessage());
	}

	@Test
	@Order(35)
	public void rsvpToExistingEvent() {
		dal.rsvpTo(USER_ONE_EMAIL, USER_ONE_FIRST_NAME, eventTwoId, EVENT_ONE_TIME);
		dal.rsvpTo(USER_TWO_EMAIL, USER_TWO_FIRST_NAME, eventOneId, EVENT_TWO_TIME);
	}

	@Test
	@Order(36)
	public void rsvpWithNullEmail() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			dal.rsvpTo("", USER_ONE_FIRST_NAME, eventTwoId, EVENT_ONE_TIME);
		});
		assertEquals("Email cannot be left empty", e.getMessage());
	}

	@Test
	@Order(37)
	public void rsvpTwice() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.rsvpTo(USER_ONE_EMAIL, USER_ONE_FIRST_NAME, eventTwoId, EVENT_ONE_TIME);
		});
		String expectedError = "Detail: Key (event, email)=(" + eventTwoId + ", " + USER_ONE_EMAIL + ") already exists.";
		assertTrue(e.getMessage().contains(expectedError));
	}

	@Test
	@Order(38)
	public void retrieveRsvpdEvents() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveRsvpdEvents(USER_TWO_EMAIL, "")));
	}

	@Test
	@Order(39)
	public void retrieveAttendeesList() {
		assertTrue(compareUserLists(userSearchResult, dal.retrieveAttendees(eventTwoId)));
	}

	@Test
	@Order(40)
	public void unRsvpFromKnownEvent() {
		dal.unRsvpFrom(USER_ONE_EMAIL, eventTwoId);
		dal.unRsvpFrom(USER_TWO_EMAIL, eventOneId);
	}

	@Test
	@Order(41)
	public void unRsvpFromUnknownEvent() {
		Exception e = assertThrows(NullPointerException.class, () -> {
			dal.unRsvpFrom(USER_ONE_EMAIL, eventTwoId);
		});
		assertEquals("User is not RSVPd to this event", e.getMessage());
	}

	private static boolean compareUsers(JSONObject uOne, JSONObject uTwo) {
		String uOneFirst = (String) uOne.get("firstName");
		String uOneLast = (String) uOne.get("lastName");
		String uOneEmail = (String) uOne.get("email");
		String uOneCampus = (String) uOne.get("campus");

		String uTwoFirst = (String) uTwo.get("firstName");
		String uTwoLast = (String) uTwo.get("lastName");
		String uTwoEmail = (String) uTwo.get("email");
		String uTwoCampus = (String) uTwo.get("campus");

		boolean firstComp = uOneFirst.equals(uTwoFirst);
		boolean lastComp = uOneLast.equals(uTwoLast);
		boolean emailComp = uOneEmail.equals(uTwoEmail);
		boolean campusComp = uOneCampus.equals(uTwoCampus);

		return firstComp && lastComp && emailComp && campusComp;
	}

	private static boolean compareEvents(JSONObject eOne, JSONObject eTwo) {
		int eOneOwner = (int) eOne.get("owner");
		Timestamp eOneTime = (Timestamp) eOne.get("eventTime");
		String eOneUrl = (String) eOne.get("posterUrl");
		String eOneName = (String) eOne.get("name");
		String eOneDescription = (String) eOne.get("description");
		String eOneLocation = (String) eOne.get("location");
		int eOnePopularity = (int) eOne.get("popularity");
		String eOneStatus = (String) eOne.get("status");

		int eTwoOwner = (int) eTwo.get("owner");
		Timestamp eTwoTime = (Timestamp) eTwo.get("eventTime");
		String eTwoUrl = (String) eTwo.get("posterUrl");
		String eTwoName = (String) eTwo.get("name");
		String eTwoDescription = (String) eTwo.get("description");
		String eTwoLocation = (String) eTwo.get("location");
		int eTwoPopularity = (int) eTwo.get("popularity");
		String eTwoStatus = (String) eTwo.get("status");

		JSONArray tagsOne = (JSONArray) eOne.get("tags");
		JSONArray tagsTwo = (JSONArray) eTwo.get("tags");

		if (tagsOne.size() != tagsTwo.size()) {
			return false;
		}

		Set<String> eOneTags = new HashSet<String>();
		Set<String> eTwoTags = new HashSet<String>();
		for (int i = 0; i < tagsOne.size(); i++) {
			String eOneTag = (String) tagsOne.get(i);
			eOneTags.add(eOneTag);
			String eTwoTag = (String) tagsTwo.get(i);
			eTwoTags.add(eTwoTag);
		}
		
		boolean ownerComp = eOneOwner == eTwoOwner;
		boolean timeComp = eOneTime.equals(eTwoTime);
		boolean urlComp = eOneUrl.equals(eTwoUrl);
		boolean nameComp = eOneName.equals(eTwoName);
		boolean descriptionComp = eOneDescription.equals(eTwoDescription);
		boolean locationComp = eOneLocation.equals(eTwoLocation);
		boolean popularityComp = eOnePopularity == eTwoPopularity;
		boolean statusComp = eOneStatus.equals(eTwoStatus);
		boolean tagsComp = eOneTags.equals(eTwoTags);

		return tagsComp && ownerComp && timeComp && urlComp && nameComp && descriptionComp && locationComp
				&& popularityComp && statusComp;
	}

	private static boolean compareEventLists(JSONObject listOne, JSONObject listTwo) {
		JSONArray arrOne = (JSONArray) listOne.get("events");
		JSONArray arrTwo = (JSONArray) listTwo.get("events");

		if (arrOne.size() != arrTwo.size()) {
			return false;
		}

		boolean equals = true;
		for (int i = 0; i < arrOne.size(); i++) {
			JSONObject eOne = (JSONObject) arrOne.get(i);
			int eOneId = (int) eOne.get("eID");
			int eOneOwner = (int) eOne.get("owner");
			String eOneUrl = (String) eOne.get("posterUrl");
			String eOneName = (String) eOne.get("name");

			JSONObject eTwo = (JSONObject) arrTwo.get(i);
			int eTwoId = (int) eTwo.get("eID");
			int eTwoOwner = (int) eTwo.get("owner");
			String eTwoUrl = (String) eTwo.get("posterUrl");
			String eTwoName = (String) eTwo.get("name");

			boolean idComp = eOneId == eTwoId;
			boolean ownerComp = eOneOwner == eTwoOwner;
			boolean urlComp = eOneUrl.equals(eTwoUrl);
			boolean nameComp = eOneName.equals(eTwoName);

			equals = equals & idComp & ownerComp & urlComp & nameComp;
		}
		return equals;
	}

	private static boolean compareUserLists(JSONObject listOne, JSONObject listTwo) {
		JSONArray arrOne = (JSONArray) listOne.get("users");
		JSONArray arrTwo = (JSONArray) listTwo.get("users");

		if (arrOne.size() != arrTwo.size()) {
			return false;
		}

		boolean equals = true;
		for (int i = 0; i < arrOne.size(); i++) {
			JSONObject uOne = (JSONObject) arrOne.get(i);
			int uOneId = (int) uOne.get("uID");
			String uOneFirst = (String) uOne.get("firstName");
			String uOneLast = (String) uOne.get("lastName");

			JSONObject uTwo = (JSONObject) arrTwo.get(i);
			int uTwoId = (int) uTwo.get("uID");
			String uTwoFirst = (String) uTwo.get("firstName");
			String uTwoLast = (String) uTwo.get("lastName");

			boolean idComp = uOneId == uTwoId;
			boolean firstComp = uOneFirst.equals(uTwoFirst);
			boolean lastComp = uOneLast.equals(uTwoLast);

			equals = equals & idComp & firstComp & lastComp;
		}
		return equals;
	}
}
