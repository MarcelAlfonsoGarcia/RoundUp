package com.roundup.roundupAPI.services;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.roundup.roundupAPI.database.DAL;

/*
This class calls the data access layer and performs the desired operations to
retrieve subscription information from the database.
*/
@Service
public class SubscriptionService {

	public static DAL dal = DAL.getInstance();

	/**
	 * @param followerID: the id of the user who is doing the following
	 * @param followedID: the id of the user being followed
	 * 
	 * @return: the newly added subscription object
	 * 
	 *          This method adds a new subscription to the database
	 */
	public void addSubscription(int followerID, int followedID) {
		/*
		 * 1. Pass the subscription information to the data access layer and tell it to
		 * add a new subscription to the database. 2. Create an subscription object with
		 * the provided subscription information. 3. Return the subscription object.
		 */
		dal.subscribeTo(followerID, followedID);
		;
	};

	/**
	 * @param followerID: the id of the user who is doing the following
	 * @param followedID: the id of the user being followed
	 * 
	 * @return: the delete Subscription object
	 * 
	 *          This method deletes subscription information from the database.
	 */
	public void deleteSubscription(int followerID, int followedID) {
		/*
		 * 1. Pass the subscription id to the data access layer and tell it to delete
		 * the subscription from the database. 2. Create an subscription object with the
		 * provided subscription information. 3. Return the Subscription object.
		 */
		dal.unsubscribeFrom(followerID, followedID);
	}
}