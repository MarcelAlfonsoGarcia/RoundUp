package com.roundup.roundupAPI.controllers;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.roundup.roundupAPI.services.SubscriptionService;

/**
 * This module handles requests that have to do with a subscription
 */
@RestController
public class SubscriptionController {
	/*
	 * An instance of the subscription service
	 */
	@Autowired
	private SubscriptionService subscriptionService;

	/*
	 * @param followerID: the id of the user who's doing the following
	 * 
	 * @param followedID: the id of the user who's being followed
	 * 
	 * @return: a json object with information about the newly added subscription
	 * 
	 * This method handles POST requests with the /subscriptions route as attempts
	 * to add new subscription information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/subscriptions/")
	public void addSubscription(@RequestBody JSONObject body) {
		/*
		 * 1. Get SubscriptionService instance to create a new subscription. 2. Retrieve
		 * new subscription information 3. Return JSON serialized Subscription object
		 */
		int followerID = (int) body.get("followerID");
		int followedID = (int) body.get("followedID");

		subscriptionService.addSubscription(followerID, followedID);
	};

	/*
	 * @param followerID: the id of the user who's doing the following
	 * 
	 * @param followedID: the id of the user who's being followed
	 * 
	 * @return: a json object with information about the deleted subscription object
	 * 
	 * This method handles DELETE requests with the /subscriptions route as attempts
	 * to delete subscription information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "api/subscriptions/")
	public void deleteSubscription(@RequestBody JSONObject body) {
		/*
		 * 1. Get SubscriptionService instance to delete an subscription. 2. retrieve
		 * deleted subscription information 3. Return JSON serialized Subscription
		 * object
		 */
		int followerID = (int) body.get("followerID");
		int followedID = (int) body.get("followedID");

		subscriptionService.deleteSubscription(followerID, followedID);
	};

};
