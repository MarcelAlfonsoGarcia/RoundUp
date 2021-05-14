package com.roundup.roundupAPI.frontend;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Base64;
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
import org.springframework.web.util.NestedServletException;

import com.roundup.roundupAPI.controllers.UserController;
import com.roundup.roundupAPI.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers=UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRouteTests {
	private static long userOneId;
	private static final String USER_ONE_FIRST_NAME = "John";
	private static final String USER_ONE_LAST_NAME = "Doe";
	private static final String USER_ONE_EMAIL = "john.doe@pomona.edu";
	private static final String USER_ONE_PASSWORD = "password";
	private static final String USER_ONE_CAMPUS = "POM";

	private static long userTwoId;
	private static final String USER_TWO_FIRST_NAME = "Sue";
	private static final String USER_TWO_LAST_NAME = "Storm";
	private static final String USER_TWO_EMAIL = "sue.storm@hmc.edu";
	private static final String USER_TWO_PASSWORD = "1234";
	private static final String USER_TWO_CAMPUS = "HMC";

	private static JSONObject userOne;
	private static JSONObject userTwo;


	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	

	@SuppressWarnings("unchecked")
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		userOne = new JSONObject();
		userOne.put("firstName", USER_ONE_FIRST_NAME);
		userOne.put("lastName", USER_ONE_LAST_NAME);
		userOne.put("email", USER_ONE_EMAIL);
		userOne.put("campus", USER_ONE_CAMPUS);

		userTwo = new JSONObject();
		userTwo.put("firstName", USER_TWO_FIRST_NAME);
		userTwo.put("lastName", USER_TWO_LAST_NAME);
		userTwo.put("email", USER_TWO_EMAIL);
		userTwo.put("campus", USER_TWO_CAMPUS);
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void loginWithFalseCredentialsTest() throws Exception {
		JSONObject body = convertStringToJSONObject(userOne.toJSONString());
		body.put("password", USER_ONE_PASSWORD);
		RequestBuilder loginRequest = MockMvcRequestBuilders.post("/api/users/login/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		MvcResult result = mockMvc.perform(loginRequest).andReturn();
		assertTrue(result.getResponse().getContentAsString().isEmpty());
	  };
	  
	  
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	@Order(2)
	public void registerNonExistingUserTest() throws Exception {
		JSONObject body = convertStringToJSONObject(userOne.toJSONString());
		body.put("password", USER_ONE_PASSWORD);
		
		RequestBuilder registerUserOneRequest = MockMvcRequestBuilders.post("/api/users/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		MvcResult result = mockMvc.perform(registerUserOneRequest).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());

		assertTrue(compareUsers(userOne, resultJson));
		userOneId = new Long((long) resultJson.get("uID"));
		
		
		// repeating procedure for a second user
		body = convertStringToJSONObject(userTwo.toJSONString());
		body.put("password", USER_TWO_PASSWORD);
		
		RequestBuilder registerUserTwoRequest = MockMvcRequestBuilders.post("/api/users/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		result = mockMvc.perform(registerUserTwoRequest).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userTwo, resultJson));
		userTwoId = new Long((long) resultJson.get("uID"));
	};
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(3)
	public void registerExistingUserTest() {
		JSONObject body = convertStringToJSONObject(userOne.toJSONString());
		body.put("password", USER_ONE_PASSWORD);
		
		RequestBuilder registerRequest = MockMvcRequestBuilders.post("/api/users/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(registerRequest);
		});
		
		assertTrue(exception.getMessage().endsWith("already exists."));
	};

	
	@SuppressWarnings("unchecked")
	@Test
	@Order(4)
	public void loginUserWithCorrectCredentialsTest() throws Exception {
		JSONObject body = convertStringToJSONObject(userOne.toJSONString());
		body.put("password", USER_ONE_PASSWORD);
		
		RequestBuilder loginRequest = MockMvcRequestBuilders.post("/api/users/login/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		MvcResult result = mockMvc.perform(loginRequest).andExpect(status().isOk()).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userOne, resultJson));
	 };
	  


//    
    @Test
    @Order(5)
	public void getExistingUserDetailsTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/api/users/" + userOneId + "/");
		MvcResult result = mockMvc.perform(request).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userOne, resultJson));
		
		request = MockMvcRequestBuilders.get("/api/users/" + userTwoId + "/");
		result = mockMvc.perform(request).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userTwo, resultJson));
	};
	
    @Test
    @Order(5)
	public void getUserDetailsWithValidTokenTest() throws Exception {
    	String auth = USER_ONE_EMAIL + ":" + USER_ONE_PASSWORD;
		String token = Base64.getEncoder().encodeToString(auth.getBytes());
		RequestBuilder request = MockMvcRequestBuilders.get("/api/users/").header("Authorization", "Basic " + token);
		MvcResult result = mockMvc.perform(request).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userOne, resultJson));
		
		request = MockMvcRequestBuilders.get("/api/users/" + userTwoId + "/").header("Authorization", "Basic " + token);
		result = mockMvc.perform(request).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userTwo, resultJson));
	};
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(6)
	public void updateExistingUserTest() throws Exception {
		JSONObject body = convertStringToJSONObject(userOne.toJSONString());
		body.put("firstName", "James");
		RequestBuilder updateUserOneRequest = MockMvcRequestBuilders.put("/api/users/"+ userOneId + "/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		MvcResult result = mockMvc.perform(updateUserOneRequest).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertEquals(resultJson.get("firstName"), "James");
		

		body = convertStringToJSONObject(userTwo.toJSONString());
		body.put("firstName", "Cindy");
		RequestBuilder updateUserTwoRequest = MockMvcRequestBuilders.put("/api/users/"+ userTwoId + "/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		result = mockMvc.perform(updateUserTwoRequest).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertEquals(resultJson.get("firstName"), "Cindy");
	};
	

	@Test
	@Order(7)
	public void getExistingUserRSVPSTest() throws Exception {
		RequestBuilder getUserRSVPSRequest = MockMvcRequestBuilders.get("/api/users/" + USER_ONE_EMAIL + "/rsvps/" + 
	"'active'/");
		MvcResult result = mockMvc.perform(getUserRSVPSRequest).andReturn();
		JSONArray rsvps = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("rsvps");
		assertTrue(rsvps == null);
	};

	@Test
	@Order(7)
	public void getExistingUserSubscriptionsTest() throws Exception {
		RequestBuilder getUserSubscriptionsRequest = MockMvcRequestBuilders.get("/api/users/" + userOneId + "/subscriptions/");
		MvcResult result = mockMvc.perform(getUserSubscriptionsRequest).andReturn();
		JSONArray subscriptions = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("subscriptions");
		assertTrue(subscriptions == null);
	};

	@Test
	@Order(7)
	public void getExistingUserSubscribersTest() throws Exception {
		RequestBuilder getUserSubscribersRequest = MockMvcRequestBuilders.get("/api/users/" + userOneId + "/subscribers/");
		MvcResult result = mockMvc.perform(getUserSubscribersRequest).andReturn();
		JSONArray subscribers = (JSONArray) convertStringToJSONObject(result.getResponse().getContentAsString()).get("users");
		assertTrue(subscribers.isEmpty());
	};

	
	@Test
	@Order(8)
	public void deleteExistingUserTest() throws Exception {
		RequestBuilder deleteUserOneRequest = MockMvcRequestBuilders.delete("/api/users/"+ userOneId + "/");
		mockMvc.perform(deleteUserOneRequest).andExpect(status().isOk());

		RequestBuilder deleteUserTwoRequest = MockMvcRequestBuilders.delete("/api/users/"+ userTwoId + "/");
		mockMvc.perform(deleteUserTwoRequest).andExpect(status().isOk());
	};
	
	
	@Test
	@Order(9)
	public void deleteNonExistingUserTest() throws Exception {
		RequestBuilder deleteUserOneRequest = MockMvcRequestBuilders.delete("/api/users/"+ userOneId + "/");
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(deleteUserOneRequest);
		});
		assertTrue(exception.getMessage().endsWith("No user exists under the provided information"));
		
		RequestBuilder deleteUserTwoRequest = MockMvcRequestBuilders.delete("/api/users/"+ userTwoId + "/");
		exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(deleteUserTwoRequest);
		});
		assertTrue(exception.getMessage().endsWith("No user exists under the provided information"));
	};
	
	
	@Test
	@Order(10)
	public void getNonExistingUserDetailsTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/api/users/" + userOneId + "/");
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(request);
		});
		assertTrue(exception.getMessage().endsWith("User was not found in the database"));
	};
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(11)
	public void updateNonExistingUserTest() throws Exception {
		JSONObject body = convertStringToJSONObject(userOne.toJSONString());
		body.put("firstName", "James");
		RequestBuilder updateUserOneRequest = MockMvcRequestBuilders.put("/api/users/"+ userOneId + "/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(updateUserOneRequest);
		});
		assertTrue(exception.getMessage().endsWith("User was not found in the database"));
	};
	
	
    @Test
    @Order(5)
	public void getUserDetailsWithInvalidTokenTest() throws Exception {
		String auth = USER_ONE_EMAIL + ":" + USER_ONE_PASSWORD;
		String token = Base64.getEncoder().encodeToString(auth.getBytes());
		RequestBuilder request = MockMvcRequestBuilders.get("/api/users/" + userOneId + "/").header("Authorization", "Basic " + token);
		MvcResult result = mockMvc.perform(request).andReturn();
		JSONObject resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userOne, resultJson));
		
		request = MockMvcRequestBuilders.get("/api/users/" + userTwoId + "/").header("Authorization", "Basic " + token);
		result = mockMvc.perform(request).andReturn();
		resultJson = convertStringToJSONObject(result.getResponse().getContentAsString());
		assertTrue(compareUsers(userTwo, resultJson));
	};
	
	
	public boolean compareUsers(JSONObject userOne, JSONObject userTwo) {
		for (Object key: userOne.keySet()) {
			if (!userOne.get(key).equals(userTwo.get(key))) {
				return false;
			}
		}
		return true;
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