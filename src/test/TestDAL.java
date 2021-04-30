package test;

import static org.junit.Assert.assertTrue;
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
	private static final String USER_ONE_PASSWORD = "password";
	private static final String USER_ONE_CAMPUS = "POM";

	private static int userTwoId;
	private static final String USER_TWO_FIRST_NAME = "Sue";
	private static final String USER_TWO_LAST_NAME = "Storm";
	private static final String USER_TWO_EMAIL = "sue.storm@hmc.edu";
	private static final String USER_TWO_PASSWORD = "1234";
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
		assertTrue(compareUsers(userOne, dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_PASSWORD,
				USER_ONE_CAMPUS)));
		assertTrue(compareUsers(userTwo, dal.createUser(USER_TWO_FIRST_NAME, USER_TWO_LAST_NAME, USER_TWO_EMAIL, USER_TWO_PASSWORD,
				USER_TWO_CAMPUS)));
	}

	@Test
	public void createNullNameUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("First Name cannot be left empty");
		dal.createUser("", USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_ONE_PASSWORD, USER_ONE_CAMPUS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Last Name cannot be left empty");
		dal.createUser(USER_ONE_FIRST_NAME, "", USER_ONE_EMAIL, USER_ONE_PASSWORD, USER_ONE_CAMPUS);
	}

	@Test
	public void createNullLoginUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email cannot be left empty");

		dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, "", USER_ONE_PASSWORD, USER_ONE_CAMPUS);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Password cannot be left empty");

		dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_TWO_EMAIL, "", USER_ONE_CAMPUS);
	}

	@Test
	public void createExistingUser() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("The email provided is already in use");

		dal.createUser(USER_ONE_FIRST_NAME, USER_ONE_LAST_NAME, USER_ONE_EMAIL, USER_TWO_PASSWORD, USER_ONE_CAMPUS);
	}

	@Test
	public void logInExistingUser() {
		assertTrue(compareUsers(userOne, dal.logInUser(USER_ONE_EMAIL, USER_ONE_PASSWORD)));
		assertTrue(compareUsers(userTwo, dal.logInUser(USER_TWO_EMAIL, USER_TWO_PASSWORD)));
	}

	@Test
	public void logInBadFields() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email or Password is incorrect");

		dal.logInUser("jonlol", USER_TWO_PASSWORD);

		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Email or Password is incorrect");

		dal.logInUser(USER_ONE_EMAIL, "password1");
	}

	@Test
	public void retrieveExistingUser() {
		assertTrue(compareUsers(userOne, dal.retrieveUser(userOneId)));
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

		assertTrue(compareUsers(updatedUser, dal.updateUser(userOneId, "Jack", "Savage", "jack.savage@email.com", "CMC")));
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
		assertTrue(compareEvents(eventOne, dal.createEvent(EVENT_ONE_OWNER, EVENT_ONE_TIME, EVENT_ONE_POSTER_URL, EVENT_ONE_NAME,
				EVENT_ONE_DESCRIPTION, EVENT_ONE_LOCATION, EVENT_ONE_TAGS)));
		assertTrue(compareEvents(eventOne, dal.createEvent(EVENT_TWO_OWNER, EVENT_TWO_TIME, EVENT_TWO_POSTER_URL, EVENT_TWO_NAME,
				EVENT_TWO_DESCRIPTION, EVENT_TWO_LOCATION, EVENT_TWO_TAGS)));
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
		assertTrue(compareEvents(eventOne, dal.retrieveEvent(eventOneId)));
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
		// TODO: tags
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

		assertTrue(compareEvents(updatedEvent, dal.updateEvent(eventOneId, EVENT_ONE_OWNER, "New Description",
				Timestamp.valueOf("2022-04-05 14:55:15.888"), "New Name", "New Location", null)));

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
		dal.updateEventStatus("inactive", Timestamp.valueOf("2022-04-03 14:55:10.888"),
				Timestamp.valueOf("2022-04-05 14:55:10.888"));
	}

	@SuppressWarnings("unchecked")
	@Test
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

		JSONObject tagsJson = dal.retrieveAllTags();
		JSONArray tagsArray = (JSONArray) tagsJson.get("tags");
		
		boolean fullyContained = true;
		for (int i = 0; i < tagsArray.size(); i++) {
			fullyContained = fullyContained && tagSet.contains((String) tagsArray.get(i));
		}
		assertTrue(fullyContained);
	}

	@Test
	public void retrieveEventsByKnownTags() {
		Set<String> mathTags = new HashSet<String>(Arrays.asList("Mathematics"));

		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByTag(mathTags, "")));
	}

	@Test
	public void retrieveEventsByKnownOwner() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByOwner(EVENT_ONE_OWNER, "")));
	}

	@Test
	public void retrieveEventsByUnknownOwner() {
		exceptionRule.expect(IllegalArgumentException.class);
		exceptionRule.expectMessage("Cannot find the target user");

		dal.retrieveEventsByOwner(0, "");
	}

	@Test
	public void retrieveEventByName() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByName("Mat", "")));
	}

	@Test
	public void retrieveEventByTime() {
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveEventsByTime(Timestamp.valueOf("2022-04-03 14:55:10.888"),
				Timestamp.valueOf("2022-04-05 14:55:10.888"))));
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
		assertTrue(compareUserLists(userSearchResult, dal.retrieveSubscriptions(userOneId)));
	}

	@Test
	public void retrieveSubscribersFromUser() {
		assertTrue(compareUserLists(userSearchResult, dal.retrieveSubscribers(userTwoId)));
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
		assertTrue(compareEventLists(eventSearchResult, dal.retrieveRsvpdEvents(USER_ONE_EMAIL, "")));
	}

	@Test
	public void retrieveAttendeesList() {
		assertTrue(compareUserLists(userSearchResult, dal.retrieveAttendees(eventTwoId)));
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

	private static boolean compareUsers(JSONObject uOne, JSONObject uTwo) {
		int uOneId = (int) uOne.get("uID");
		String uOneFirst = (String) uOne.get("firstName");
		String uOneLast = (String) uOne.get("lastName");
		String uOneEmail = (String) uOne.get("email");
		String uOneCampus = (String) uOne.get("campus");

		int uTwoId = (int) uTwo.get("uID");
		String uTwoFirst = (String) uTwo.get("firstName");
		String uTwoLast = (String) uTwo.get("lastName");
		String uTwoEmail = (String) uTwo.get("email");
		String uTwoCampus = (String) uTwo.get("campus");

		boolean idComp = uOneId == uTwoId;
		boolean firstComp = uOneFirst.equals(uTwoFirst);
		boolean lastComp = uOneLast.equals(uTwoLast);
		boolean emailComp = uOneEmail.equals(uTwoEmail);
		boolean campusComp = uOneCampus.equals(uTwoCampus);

		return idComp && firstComp && lastComp && emailComp && campusComp;
	}

	private static boolean compareEvents(JSONObject eOne, JSONObject eTwo) {
		int eOneId = (int) eOne.get("eID");
		int eOneOwner = (int) eOne.get("owner");
		Timestamp eOneTime = (Timestamp) eOne.get("eventTime");
		String eOneUrl = (String) eOne.get("posterUrl");
		String eOneName = (String) eOne.get("name");
		String eOneDescription = (String) eOne.get("description");
		String eOneLocation = (String) eOne.get("location");
		String eOnePopularity = (String) eOne.get("popularity");
		String eOneStatus = (String) eOne.get("status");

		int eTwoId = (int) eTwo.get("eID");
		int eTwoOwner = (int) eTwo.get("owner");
		Timestamp eTwoTime = (Timestamp) eTwo.get("eventTime");
		String eTwoUrl = (String) eTwo.get("posterUrl");
		String eTwoName = (String) eTwo.get("name");
		String eTwoDescription = (String) eTwo.get("description");
		String eTwoLocation = (String) eTwo.get("location");
		String eTwoPopularity = (String) eTwo.get("popularity");
		String eTwoStatus = (String) eTwo.get("status");
		
		JSONArray tagsOne = (JSONArray) eOne.get("tags");
		JSONArray tagsTwo= (JSONArray) eTwo.get("tags");

		if (tagsOne.size() != tagsTwo.size()) {
			return false;
		}

		boolean idComp = eOneId == eTwoId;
		boolean ownerComp = eOneOwner == eTwoOwner;
		boolean timeComp = eOneTime.equals(eTwoTime);
		boolean urlComp = eOneUrl.equals(eTwoUrl);
		boolean nameComp = eOneName.equals(eTwoName);
		boolean descriptionComp = eOneDescription.equals(eTwoDescription);
		boolean locationComp = eOneLocation.equals(eTwoLocation);
		boolean popularityComp = eOnePopularity.equals(eTwoPopularity);
		boolean statusComp = eOneStatus.equals(eTwoStatus);
		
		boolean tagsComp = true;
		for (int i = 0; i < tagsOne.size(); i++) {
			String eOneTag = (String) tagsOne.get(i);
			String eTwoTag = (String) tagsTwo.get(i);
			
			tagsComp = tagsComp & eOneTag.equals(eTwoTag);
		}

		return tagsComp && idComp && ownerComp && timeComp && urlComp && nameComp && descriptionComp && locationComp
				&& popularityComp && statusComp;
	}
	
	private static boolean compareEventLists(JSONObject listOne, JSONObject listTwo) {
		JSONArray arrOne = (JSONArray) listOne.get("events");
		JSONArray arrTwo= (JSONArray) listTwo.get("events");

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
		JSONArray arrOne = (JSONArray) listOne.get("events");
		JSONArray arrTwo= (JSONArray) listTwo.get("events");

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
