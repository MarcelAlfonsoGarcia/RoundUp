package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
import org.junit.rules.ExpectedException;

import database.DAL;

//@TestMethodOrder(OrderAnnotation.class)
public class TestDAL {

	private static int userOneId;
	private static final String USER_ONE_FIRST_NAME = "John";
	private static final String USER_ONE_LAST_NAME = "Doe";
	private static final String USER_ONE_EMAIL = "john.doe@pomona.edu";
	private static final String USER_ONE_CAMPUS = "POM";

	private static int userTwoId;
	private static final String USER_TWO_FIRST_NAME = "Sue";
	private static final String USER_TWO_LAST_NAME = "Storm";
	private static final String USER_TWO_EMAIL = "sue.storm@hmc.edu";
	private static final String USER_TWO_CAMPUS = "HMC";

	private static int eventOneId;
	private static final int EVENT_ONE_OWNER = 1;
	private static final Timestamp EVENT_ONE_TIME = Timestamp.valueOf("2022-04-04 14:55:10.888");
	private static final String EVENT_ONE_POSTER_URL = "url1";
	private static final String EVENT_ONE_NAME = "Math Colloquium";
	private static final String EVENT_ONE_DESCRIPTION = "There is a thingy with math";
	private static final String EVENT_ONE_LOCATION = "Estella";
	private static final int EVENT_ONE_POPULARITY = 23;
	private static final String EVENT_ONE_STATUS = "active";
	private static final Set<String> EVENT_ONE_TAGS = new HashSet<String>(Arrays.asList("Mathematics", "Colloquium"));

	private static int eventTwoId;
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

	private static JSONObject userSearchResult;
	private static JSONObject eventSearchResult;

	private static DAL dal;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userOne = new JSONObject();
		userOne.put("uID", userOneId);
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

		eventSearchResult = new JSONObject();
		JSONArray userResults = new JSONArray();
		JSONObject user = new JSONObject();
		user.put("uID", userOneId);
		user.put("firstName", USER_ONE_FIRST_NAME);
		user.put("name", USER_ONE_LAST_NAME);
		userResults.add(user);
		eventSearchResult.put("events", userResults);

		eventSearchResult = new JSONObject();
		JSONArray eventResults = new JSONArray();
		JSONObject event = new JSONObject();
		event.put("eID", eventOneId);
		event.put("owner", EVENT_ONE_OWNER);
		event.put("name", EVENT_ONE_NAME);
		event.put("posterUrl", EVENT_ONE_POSTER_URL);
		eventResults.add(event);
		eventSearchResult.put("events", eventResults);

		dal = DAL.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	// @Order(1)
	public void createValidUser() {
		assertEquals(userOne, dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS));
		assertEquals(userTwo, dal.createUser(USER_TWO_FIRST_NAME, USER_TWO_LAST_NAME, USER_TWO_EMAIL, USER_TWO_CAMPUS));
	}

	@Test
	public void createNullNameUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("First Name cannot be left empty");
		dal.createUser("", USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Last Name cannot be left empty");
		dal.createUser(USER_ONE_FIRST_NAME, "", USER_ONE_EMAIL, USER_ONE_CAMPUS);
	}

	@Test
	public void createNullEmailUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email cannot be left empty");

		dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, "", USER_ONE_CAMPUS);
	}

	@Test
	public void createExistingUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("The email provided is already in use");

		dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);
	}

	@Test
	public void retrieveExistingUser() {
		assertEquals(userOne, dal.retrieveUser(userOneId));
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
		dal.deleteUser(userOneId);
		dal.deleteUser(userTwoId);
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
		updatedUser.put("uID", userOneId);
		updatedUser.put("firstName", "Jack");
		updatedUser.put("lastName", "Savage");
		updatedUser.put("email", "jack.savage@email.com");
		updatedUser.put("campus", "CMC");

		assertEquals(updatedUser, dal.updateUser(userOneId, "Jack", "Savage", "jack.savage@email.com", "CMC"));
		dal.updateUser(userOneId, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);
	}

	@Test
	public void updateUnknownUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No user exists under the provided information");

		dal.updateUser(0, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, "", USER_ONE_CAMPUS);
	}

	@Test
	public void updateUserNullName() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("First Name cannot be left empty");

		dal.updateUser(userOneId, "", USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Last Name cannot be left empty");

		dal.updateUser(userOneId, USER_ONE_FIRST_NAME, "", USER_ONE_EMAIL, USER_ONE_CAMPUS);
	}

	@Test
	public void updateUserNullEmail() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email cannot be left empty");

		dal.updateUser(userOneId, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, "", USER_ONE_CAMPUS);
	}

	@Test
	public void updateUserExistingEmail() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("The email provided is already in use");

		dal.updateUser(userOneId, USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_CAMPUS);
	}

	@Test
	public void createValidEvent() {
		assertEquals(eventOne, dal.createEvent(EVENT_ONE_OWNER, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL, EVENT_ONE_NAME,
				EVENT_ONE_DESCRIPTION, EVENT_ONE_LOCATION, EVENT_ONE_TAGS));
		assertEquals(eventOne, dal.createEvent(EVENT_TWO_OWNER, EVENT_TWO_TIME, EVENT_TWO_POSTER_URL, EVENT_TWO_NAME,
				EVENT_TWO_DESCRIPTION, EVENT_TWO_LOCATION, EVENT_TWO_TAGS));
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
		dal.deleteEvent(eventOneId, userOneId);
		dal.deleteEvent(eventTwoId, userTwoId);
	}

	@Test
	public void deleteUnknownEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("No event exists under the provided information");

		dal.deleteEvent(eventOneId, userOneId);
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
	public void retrieveEventsByKnownTags() {
		Set<String> mathTags = new HashSet<String>(Arrays.asList("Mathematics"));

		assertEquals(eventSearchResult, dal.retrieveEventsByTag(mathTags, ""));
	}

	@Test
	public void retrieveEventsByKnownOwner() {
		assertEquals(eventSearchResult, dal.retrieveEventsByOwner(EVENT_ONE_OWNER, ""));
	}

	@Test
	public void retrieveEventsByUnknownOwner() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot find the target user");

		dal.retrieveEventsByOwner(0, "");
	}

	@Test
	public void retrieveEventByName() {
		assertEquals(eventSearchResult, dal.retrieveEventsByName("Mat", ""));
	}

	@Test
	public void retrieveEventByTime() {
		assertEquals(eventSearchResult, dal.retrieveEventsByTime(Timestamp.valueOf("2022-04-03 14:55:10.888"),
				Timestamp.valueOf("2022-04-05 14:55:10.888")));
	}

	@Test
	public void subscribeToKnownUser() {
		dal.subscribeTo(userOneId, userTwoId);
		dal.subscribeTo(userTwoId, userOneId);
	}
	
	@Test
	public void subscribeToUnknownUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("User to be subscribed to does not exist");

		dal.subscribeTo(userOneId, 0);
	}
	
	@Test
	public void subscribeTwice() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot subscribe to the same user twice");

		dal.subscribeTo(userOneId, userTwoId);
	}

	@Test
	public void retrieveSubscriptionsFromUser() {
		assertEquals(userSearchResult,dal.retrieveSubscriptions(userOneId));
	}
	
	@Test
	public void retrieveSubscribersFromUser() {
		assertEquals(userSearchResult,dal.retrieveSubscribers(userTwoId));
	}
	
	@Test
	public void unsubcribeFromExistingRelationship() {
		dal.unsubscribeFrom(eventOneId, eventTwoId);
		dal.unsubscribeFrom(eventTwoId, eventOneId);
	}
	
	@Test
	public void unsubcribeFromUnknownRelationship() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot unsubscribe from unknown relationship");

		dal.unsubscribeFrom(eventOneId, eventTwoId);
	}

	@Test
	public void rsvpToExistingEvent() {
		dal.rsvpTo(USER_ONE_EMAIL, USER_ONE_FIRST_NAME, eventTwoId, EVENT_ONE_TIME);
	}

	@Test
	public void rsvpWithNullEmail() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email cannot be left empty");

		dal.rsvpTo("", USER_ONE_FIRST_NAME, eventTwoId, EVENT_ONE_TIME);
	}
	
	@Test
	public void rsvpTwice() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot RSVP to the same event twice");

		dal.rsvpTo(USER_ONE_EMAIL, USER_ONE_FIRST_NAME, eventTwoId, EVENT_ONE_TIME);	
	}

	@Test
	public void retrieveRsvpdEvents() {
		assertEquals(eventSearchResult,dal.retrieveRsvpdEvents(USER_ONE_EMAIL,""));
	}
	
	@Test 
	public void retrieveAttendeesList() {
		assertEquals(userSearchResult,dal.retrieveAttendees(eventTwoId));
	}
	
	@Test 
	public void unRsvpFromKnownEvent() {
		dal.unRsvpFrom(USER_ONE_EMAIL, eventTwoId);
	}
	
	@Test 
	public void unRsvpFromUnknownEvent() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("User is not RSVPd to this event");
		
		dal.unRsvpFrom(USER_ONE_EMAIL, eventTwoId);
	}
}
