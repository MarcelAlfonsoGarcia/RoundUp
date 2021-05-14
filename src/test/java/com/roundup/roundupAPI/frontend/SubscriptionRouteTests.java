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

import com.roundup.roundupAPI.controllers.SubscriptionController;
import com.roundup.roundupAPI.services.SubscriptionService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers=SubscriptionController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionRouteTests {
	private static final long USER_ONE_ID = 1;
	private static final long USER_TWO_ID = 2;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SubscriptionService subscriptionService;
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void deleteNonexistingSubscriptionTest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("followerID", USER_ONE_ID);
		body.put("followedID", USER_TWO_ID);
		RequestBuilder deleteSubscriptionRequest = MockMvcRequestBuilders.delete("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(deleteSubscriptionRequest);
		});
		assertTrue(exception.getMessage().endsWith("Cannot unsubscribe from unknown relationship"));
	};
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	public void addNonExistingSubscription() throws Exception {
		JSONObject body = new JSONObject();
		body.put("followerID", USER_ONE_ID);
		body.put("followedID", USER_TWO_ID);
		
		RequestBuilder addSubscriptionrequest = MockMvcRequestBuilders.post("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		mockMvc.perform(addSubscriptionrequest).andExpect(status().isOk());
		
		body.put("followerID", USER_TWO_ID);
		body.put("followedID", USER_ONE_ID);
		
		addSubscriptionrequest = MockMvcRequestBuilders.post("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		mockMvc.perform(addSubscriptionrequest).andExpect(status().isOk());
	};
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(3)
	public void addExistingSubscription() throws Exception {
		JSONObject body = new JSONObject();
		body.put("followerID", USER_ONE_ID);
		body.put("followedID", USER_TWO_ID);
		
		RequestBuilder addExistingSubscriptionOneRequest = MockMvcRequestBuilders.post("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(addExistingSubscriptionOneRequest);
		});
		assertTrue(exception.getMessage().endsWith("already exists."));
		
		
		body.put("followerID", USER_TWO_ID);
		body.put("followedID", USER_ONE_ID);
		
		RequestBuilder addExistingSubscriptionTwoRequest = MockMvcRequestBuilders.post("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		exception = assertThrows(NestedServletException.class, ()-> {
			mockMvc.perform(addExistingSubscriptionTwoRequest);
		});
		assertTrue(exception.getMessage().endsWith("already exists."));
	};
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(4)
	public void deleteExistingSubscriptionTest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("followerID", USER_ONE_ID);
		body.put("followedID", USER_TWO_ID);
		
		RequestBuilder deleteSubscriptionRequest = MockMvcRequestBuilders.delete("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		mockMvc.perform(deleteSubscriptionRequest).andExpect(status().isOk());
		
		body.put("followerID", USER_TWO_ID);
		body.put("followedID", USER_ONE_ID);
		deleteSubscriptionRequest = MockMvcRequestBuilders.delete("/api/subscriptions/").contentType(APPLICATION_JSON_UTF8).content(body.toJSONString());
		mockMvc.perform(deleteSubscriptionRequest).andExpect(status().isOk());
	};	
}
