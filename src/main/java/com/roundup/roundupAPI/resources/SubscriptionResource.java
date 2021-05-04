package com.roundup.roundupAPI.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;

import com.roundup.roundupAPI.services.SubscriptionService;


/**
This module handles requests that have to do with a subscription
*/
@Path("/subscriptions")
public class SubscriptionResource {
	/* 
	 * An instance of the subscription service  
	*/
	private SubscriptionService subscriptionServiceInstance = new SubscriptionService();
	
	/*
	  @param followerID: the id of the user who's doing the following
	  @param followedID: the id of the user who's being followed
	
	  @return: a json object with information about the newly added subscription
	
	  This method handles POST requests with the /subscriptions route as attempts to
	  add new subscription information and accesses the DAL to do that.
	*/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void addSubscription(@QueryParam("followerID") int followerID,@QueryParam("followedID") int followedID) {
	  /*
	    1. Get SubscriptionService instance to create a new subscription.
	    2. Retrieve new subscription information
	    3. Return JSON serialized Subscription object
	  */
		subscriptionServiceInstance.addSubscription(followerID, followedID);
	};
	
	/*
	  @param followerID: the id of the user who's doing the following
	  @param followedID: the id of the user who's being followed
	
	  @return: a json object with information about the deleted subscription object
	
	  This method handles DELETE requests with the /subscriptions route as attempts to
	  delete subscription information and accesses the DAL to do that.
	*/
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteSubscription(@QueryParam("followerID") int followerID,@QueryParam("followedID") int followedID) {
	  /*
	    1. Get SubscriptionService instance to delete an subscription.
	    2. retrieve deleted subscription information
	    3. Return JSON serialized Subscription object
	  */
		subscriptionServiceInstance.deleteSubscription(followerID, followedID);
	};

};

