package com.roundup.roundupAPI.controllers;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.roundup.roundupAPI.services.SubscriptionService;

/**
 * This module handles requests that have to do with subscriptions. It handles
 * POST and DELETE requests from the client API and provides a JSONObject
 * as a response.
 */
@CrossOrigin(origins = "*")
@RestController
public class SubscriptionController {
	/*
	 * An instance of the subscription service
	 */
	private SubscriptionService subscriptionService = SubscriptionService.getInstance();

	/**
	 * @param body: the body of the http request 
	 * 
	 * This method handles POST requests with the api/subscriptions/ route as attempts
	 * to add new subscription information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/subscriptions/")
	public void addSubscription(@RequestBody JSONObject body) {
		/*
		 * 1. Get SubscriptionService instance to create a new subscription. 
		 */
		int followerID = (int) body.get("followerID");
		int followedID = (int) body.get("followedID");

		subscriptionService.addSubscription(followerID, followedID);
	};

	/**
	 * @param body: the body of the http request 
	 * 
	 * This method handles DELETE requests with the api/subscriptions/ route as attempts
	 * to delete subscription information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "api/subscriptions/")
	public void deleteSubscription(@RequestBody JSONObject body) {
		/*
		 * 1. Get SubscriptionService instance to delete an subscription. 
		 */
		int followerID = (int) body.get("followerID");
		int followedID = (int) body.get("followedID");

		subscriptionService.deleteSubscription(followerID, followedID);
	};

};
