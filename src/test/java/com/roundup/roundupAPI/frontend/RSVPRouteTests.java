package com.roundup.roundupAPI.frontend;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.json.simple.JSONObject;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import com.roundup.roundupAPI.controllers.RSVPController;
import com.roundup.roundupAPI.services.RSVPService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers=RSVPController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RSVPRouteTests {
	private static final String USER_ONE_FIRST_NAME = "John";
	private static final String USER_ONE_LAST_NAME = "Doe";
	private static final String USER_ONE_EMAIL = "john.doe@pomona.edu";


	private static final int EVENT_ONE_ID = 1;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private RSVPService rsvpService;
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void deleteNonexistingRSVPTest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("email", USER_ONE_EMAIL);
		body.put("eID", EVENT_ONE_ID);
		RequestBuilder deleteRsvpRequest = MockMvcRequestBuilders.delete("/api/rsvps/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(deleteRsvpRequest);
		});
		assertTrue(exception.getMessage().endsWith("User is not RSVPd to this event"));
	};
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	public void addNonExistingRSVPTest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("email", USER_ONE_EMAIL);
		body.put("name", USER_ONE_FIRST_NAME + " " + USER_ONE_LAST_NAME);
		body.put("time", System.currentTimeMillis());
		body.put("eID", EVENT_ONE_ID);
		
		RequestBuilder addRsvprequest = MockMvcRequestBuilders.post("/api/rsvps/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		mockMvc.perform(addRsvprequest).andExpect(status().isOk());
	};
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(3)
	public void addExistingRSVPTest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("email", USER_ONE_EMAIL);
		body.put("name", USER_ONE_FIRST_NAME + " " + USER_ONE_LAST_NAME);
		body.put("time", System.currentTimeMillis());
		body.put("eID", EVENT_ONE_ID);
		
		RequestBuilder addRsvprequest = MockMvcRequestBuilders.post("/api/rsvps/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(addRsvprequest);
		});
		assertTrue(exception.getMessage().endsWith("already exists."));
	};
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(4)
	public void deleteExistingRSVPTest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("email", USER_ONE_EMAIL);
		body.put("eID", EVENT_ONE_ID);
		RequestBuilder deleteRsvpRequest = MockMvcRequestBuilders.delete("/api/rsvps/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		mockMvc.perform(deleteRsvpRequest).andExpect(status().isOk());
	};
}
