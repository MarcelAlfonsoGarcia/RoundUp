package com.roundup.roundupAPI.services;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.roundup.roundupAPI.database.DAL;

/*
This class calls the data access layer and performs the desired operations to
retrieve subscription information from the database.
*/
//@Service
public class SubscriptionService {
	// an instance of the Data Access layer component
	private DAL dal;
	
	// a single instance of the class that is used by external components
	public static SubscriptionService instance;
	
	// a constructor for the class
	public SubscriptionService() {
		// retrieves an instance for the DAL if one does not exist
		if (dal == null) {
			dal = DAL.getInstance();
		}
	}
	
	// a method that retrieves the instance of the class
	public static SubscriptionService getInstance() {
	    // 1. creates an instance for the DAL if one does not exist

	    // 2. return class instance.
		if (instance == null) {
			instance = new SubscriptionService();
		}
		return instance;
	}

	/**
	 * @param followerID: the id of the user who is doing the following
	 * @param followedID: the id of the user being followed
	 * 
	 *          This method adds a new subscription to the database
	 */
	public void addSubscription(int followerID, int followedID) {
		/*
		 * 1. Pass the subscription information to the data access layer and tell it to
		 * add a new subscription to the database. 
		 */
		dal.subscribeTo(followerID, followedID);
	};

	/**
	 * @param followerID: the id of the user who is doing the following
	 * @param followedID: the id of the user being followed
	 * 
	 *          This method deletes subscription information from the database.
	 */
	public void deleteSubscription(int followerID, int followedID) {
		/*
		 * 1. Pass the subscription id to the data access layer and tell it to delete
		 * the subscription from the database. 
		 */
		dal.unsubscribeFrom(followerID, followedID);
	}
}