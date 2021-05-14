package com.roundup.roundupAPI.frontend;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.NestedServletException;

import com.roundup.roundupAPI.controllers.EventController;
import com.roundup.roundupAPI.services.EventService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers=EventController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventRouteTests {
	private static long eventOneId;
	private static final long EVENT_ONE_TIME = System.currentTimeMillis();
	private static final String EVENT_ONE_POSTER_URL = "url1";
	private static final String EVENT_ONE_NAME = "Math Colloquia";
	private static final String EVENT_ONE_DESCRIPTION = "There is a thingy with math";
	private static final String EVENT_ONE_LOCATION = "Estella";
	private static final int EVENT_ONE_POPULARITY = 0;
	private static final String EVENT_ONE_STATUS = "active";
	private static final Set<String> EVENT_ONE_TAGS = new HashSet<String>(
			Arrays.asList("Mathematics", "Colloquium", "test"));

	private static long eventTwoId;
	private static final long EVENT_TWO_TIME = System.currentTimeMillis();
	private static final String EVENT_TWO_POSTER_URL = "url2";
	private static final String EVENT_TWO_NAME = "CS Colloquia";
	private static final String EVENT_TWO_DESCRIPTION = "There is a thingy with cs";
	private static final String EVENT_TWO_LOCATION = "Edmunds";
	private static final int EVENT_TWO_POPULARITY = 0;
	private static final String EVENT_TWO_STATUS = "active";
	private static final Set<String> EVENT_TWO_TAGS = new HashSet<String>(
			Arrays.asList("Computer Science", "Colloquium"));
	
	private static JSONObject eventOne;
	private static JSONObject eventTwo;
	
	private static final int USER_ONE_ID = 1;
	private static final int USER_TWO_ID = 2;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EventService eventService;
	
	@SuppressWarnings("unchecked")
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		eventOne = new JSONObject();
		eventOne.put("uID", USER_ONE_ID);
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
		eventTwo.put("uID", USER_TWO_ID);
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
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@Order(1)
	public void deleteNonExistingEventTest() {
		MultiValueMap params = new LinkedMultiValueMap<>();
		params.add("eID", "0");
		params.add("uID", "0");
		
		RequestBuilder deleteEventRequest = MockMvcRequestBuilders.delete("/api/events/").params(params);
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(deleteEventRequest);
		});
//		System.out.println(exception.getMessage());
		assertTrue(exception.getMessage().endsWith("No event exists under the provided information"));
	};

	@Test
	@Order(2)
	public void getNonExistingEventDetailsTest() {
		RequestBuilder getEventDetailsRequest = MockMvcRequestBuilders.get("/api/events/" + 0 + "/");
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(getEventDetailsRequest);
		});
		assertTrue(exception.getMessage().endsWith("Event was not found in the database"));
	};
	
	
	@SuppressWarnings({"unchecked" })
	@Test
	@Order(3)
	public void updateNonExistingEvent() throws Exception {
		JSONObject body = convertStringToJSONObject(eventOne.toJSONString());
		body.put("name", "Temp Name");
		RequestBuilder updateEventOneRequest = MockMvcRequestBuilders.put("/api/events/" + eventOneId + "/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(updateEventOneRequest);
		});
		assertTrue(exception.getMessage().endsWith("Event was not found in the database"));
	};
	
	

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	@Order(4)
	public void addNonexistingEventTest() throws Exception {
		RequestBuilder addEventOneRequest = MockMvcRequestBuilders.post("/api/events/").contentType(APPLICATION_JSON_UTF8).content(eventOne.toJSONString());
		MvcResult result = mockMvc.perform(addEventOneRequest).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		resultJson.put("uID", USER_ONE_ID);
		assertTrue(compareEvents(eventOne, resultJson));
		eventOneId = new Long((long) resultJson.get("eID"));
		
		RequestBuilder addEventTwoRequest = MockMvcRequestBuilders.post("/api/events/").contentType(APPLICATION_JSON_UTF8).content(eventTwo.toJSONString());
		result = mockMvc.perform(addEventTwoRequest).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		resultJson.put("uID", USER_TWO_ID);
		assertTrue(compareEvents(eventTwo, resultJson));
		eventTwoId = new Long((long) resultJson.get("eID"));
	};

	@SuppressWarnings({"unchecked" })
	@Test
	@Order(5)
	public void getExistingEvent() throws Exception {
		RequestBuilder getEventOneRequest = MockMvcRequestBuilders.get("/api/events/" + eventOneId + "/");
		MvcResult result = mockMvc.perform(getEventOneRequest).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		resultJson.put("uID", USER_ONE_ID);
		assertTrue(compareEvents(eventOne, resultJson));
		
		
		RequestBuilder getEventTwoRequest = MockMvcRequestBuilders.get("/api/events/" + eventTwoId + "/");
		result = mockMvc.perform(getEventTwoRequest).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		resultJson.put("uID", USER_TWO_ID);
		assertTrue(compareEvents(eventTwo, resultJson));
	};

	@SuppressWarnings({"unchecked" })
	@Test
	@Order(6)
	public void updateExistingEvent() throws Exception {
		JSONObject body = convertStringToJSONObject(eventOne.toJSONString());
		body.put("name", "Temp Name");
		RequestBuilder updateEventOneRequest = MockMvcRequestBuilders.put("/api/events/" + eventOneId + "/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		MvcResult result = mockMvc.perform(updateEventOneRequest).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(resultJson.get("name").equals("Temp Name"));
		
		body.put("name", EVENT_ONE_NAME);
		updateEventOneRequest = MockMvcRequestBuilders.put("/api/events/" + eventOneId + "/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		result = mockMvc.perform(updateEventOneRequest).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(resultJson.get("name").equals(EVENT_ONE_NAME));
	};
	


	@SuppressWarnings("rawtypes")
	@Test
	@Order(7)
	public void getAllTags() throws Exception {
		RequestBuilder getAllTagsRequest = MockMvcRequestBuilders.get("/api/tags/");
		MvcResult result = mockMvc.perform(getAllTagsRequest).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(!((ArrayList) resultJson.get("tags")).isEmpty());

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Order(8)
	public void getEventsByExistingTag() throws Exception {
		MultiValueMap params = new LinkedMultiValueMap<>();
		params.add("tags", "test");
		params.add("status", "");

		RequestBuilder getEventsByTagsRequest = MockMvcRequestBuilders.get("/api/events/tags/").params(params);
		MvcResult result = mockMvc.perform(getEventsByTagsRequest).andReturn();
		JSONArray events = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("events");
		JSONObject event = (JSONObject) events.get(0);
		assertTrue(event.get("name").equals(EVENT_ONE_NAME));
		assertTrue(event.get("posterUrl").equals(EVENT_ONE_POSTER_URL));
	}


	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@Test
	@Order(9)
	public void getEventsByExistingOwner() throws Exception {
		MultiValueMap params = new LinkedMultiValueMap<>();
		params.add("status", "");

		RequestBuilder getEventsByOwnerOneRequest = MockMvcRequestBuilders.get("/api/events/owner/" + USER_ONE_ID + "/").params(params);
		MvcResult result = mockMvc.perform(getEventsByOwnerOneRequest).andReturn();
		JSONArray events = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("events");
		JSONObject event = (JSONObject) events.get(0);
		assertTrue(((long) event.get("owner")) == new Long(USER_ONE_ID)) ;
		
		
		RequestBuilder getEventsByOwnerTwoRequest = MockMvcRequestBuilders.get("/api/events/owner/" + USER_TWO_ID + "/").params(params);
		result = mockMvc.perform(getEventsByOwnerTwoRequest).andReturn();
		events = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("events");
		event = (JSONObject) events.get(0);
		assertTrue(((long) event.get("owner")) == new Long(USER_TWO_ID)) ;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Order(10)
	public void getEventsByExistingName() throws Exception {
		MultiValueMap params = new LinkedMultiValueMap<>();
		params.add("search", "" + EVENT_ONE_NAME);
		params.add("status", "'active'");

		RequestBuilder getEventsByNameRequest = MockMvcRequestBuilders.get("/api/events/search/").params(params);
		MvcResult result = mockMvc.perform(getEventsByNameRequest).andReturn();
		JSONArray events = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("events");
		JSONObject event = (JSONObject) events.get(0);
		assertTrue(event.get("name").equals(EVENT_ONE_NAME)) ;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Order(11)
	public void getEventsByExistingTime() throws Exception {
		MultiValueMap params = new LinkedMultiValueMap<>();
		params.add("fromTime", "" + (EVENT_ONE_TIME - 24*60*60*1000));
		params.add("toTime", "" + (EVENT_ONE_TIME + 24*60*60*1000));

		RequestBuilder getEventsByTimeRequest = MockMvcRequestBuilders.get("/api/events/timeframe/").params(params);
		MvcResult result = mockMvc.perform(getEventsByTimeRequest).andReturn();
		JSONArray events = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("events");
		assertTrue(!events.isEmpty()) ;
	}
		

	@Test
	@Order(12)
	public void getExistingEventAttendees() throws Exception {
		RequestBuilder getEventAttendeesRequest = MockMvcRequestBuilders.get("/api/events/" + eventOneId + "/attendees/");
		MvcResult result = mockMvc.perform(getEventAttendeesRequest).andReturn();
		JSONArray users = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("users");
		assertTrue(users.isEmpty());
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@Order(13)
	public void deleteExistingEventTest() throws Exception {
		MultiValueMap params = new LinkedMultiValueMap<>();
		params.add("eID", "" + eventOneId);
		params.add("uID", "" + USER_ONE_ID);
		RequestBuilder deleteEventRequest = MockMvcRequestBuilders.delete("/api/events/").params(params);
		mockMvc.perform(deleteEventRequest).andExpect(status().isOk());
		
		
		params.set("eID", "" + eventTwoId);
		params.set("uID", "" + USER_TWO_ID);
		deleteEventRequest = MockMvcRequestBuilders.delete("/api/events/").params(params);
		mockMvc.perform(deleteEventRequest).andExpect(status().isOk());
	};
	
	
	public boolean compareEvents(JSONObject eventOne, JSONObject eventTwo) {
		return eventOne.get("uID").equals(eventTwo.get("uID")) && 
				eventOne.get("name").equals(eventTwo.get("name")) &&
				eventOne.get("posterUrl").equals(eventTwo.get("posterUrl")) &&
				eventOne.get("location").equals(eventTwo.get("location"));
	}
	
	
	
	
	public JSONObject convertStringToJSONObject(String JSONString) {
	    JSONParser parser = new JSONParser();  
	    try {
			JSONObject json = (JSONObject) parser.parse(JSONString);
			return json;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}  
	}
}
