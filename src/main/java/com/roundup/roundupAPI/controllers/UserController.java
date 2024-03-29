package com.roundup.roundupAPI.controllers;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundup.roundupAPI.services.UserService;

/**
 * This module handles requests that have to do with users. It handles
 * GET, PUT, POST, and DELETE requests from the client API and provides a JsonObject
 * as a response.
 */
@CrossOrigin(origins = "*")
@RestController
public class UserController {

	private UserService userService = UserService.getInstance();

	/**
	 * @param body: the body of the http request 
	 * 
	 * @return: a json object with information about the newly added user
	 * 
	 * This method handles requests POST requests to the api/users/ route as attempts to create
	 * a new user and passes information to ther user service to do that.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/users/")
	public JSONObject createUser(@RequestBody JSONObject body) {
		/*
		 * 1. Get UserService instance 
		 * 2. Retrieve new user information 
		 * 3. Return JSON serialized User object
		 */

		String firstName = (String) body.get("firstName");
		String lastName = (String) body.get("lastName");
		String email = (String) body.get("email");
		String password = (String) body.get("password");
		String campus = (String) body.get("campus");

		return userService.addUser(firstName, lastName, email, password, campus);
	};

	/**
	 * @param body: the body of the http request 
	 * 
	 * @return: a json object with information about the newly logged in user
	 * 
	 * This method handles POST requests to the api/users/login route as attempts to log
	 * in the user and tries to check if the provided credentials are correct.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/users/login/")
	public JSONObject login(@RequestBody JSONObject body) {
		/*
		 * 1. Get UserService instance to check user credentials. 
		 * 2. If successful, retrieve user information and token 
		 * 3. Return JSON serialized User object and token
		 */
		String email = (String) body.get("email");
		String password = (String) body.get("password");
		return  userService.login(email, password);
	  };
	  
	
	/**
	 * @param body: the body of the http request 
	 * 
     * @return: a json object with information about the newly added user
	 *
     * This method handles GET requests to the api/users/ route as attempts to retrieve 
     * user information with an authentication token
    */
    @RequestMapping(method=RequestMethod.GET, value="api/users/")
    public JSONObject loadUser(@RequestHeader("Authorization") String authHeader) {
    /*
      1. Get UserService instance to check user credentials.
      2. If successful, retrieve user information and token
      3. Return JSON serialized User object and token
    */
        try {
                String token = authHeader.split(" ")[1];
                return userService.loadUser(token);
        } catch (Exception e) {
                e.printStackTrace();
                return null;
        }
      };
      
      
	/**
	 * @param uID: the user's id
	 * 
	 * @return: a json object with information about the retrieved user
	 * 
	 * This method handles GET requests to the api/users/uID/ route as attempts to
	 * retrieve user information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "api/users/{uID}/")
	public JSONObject getUser(@PathVariable("uID") int uID) {
		/*
		 * 1. Get UserService instance to retrieve user information. 
		 * 2. Return JSON serialized User object
		 */
		return userService.getUser(uID);
	};

	/**
	 * @param uID: the user's id
	 * 
	 * @return: a json object with information about the deleted user
	 * 
	 * This method handles DELETE requests with the api/users/uID/ route as attempts to
	 * delete user information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "api/users/{uID}/")
	public void deleteUser(@PathVariable("uID") int uID) {
		/*
		 * 1. Get UserService instance to delete a user. 
		 */
		userService.deleteUser(uID);
	};

	/**
	 * @param uID: the user's id
	 * @param body: the body of the http request
	 * 
	 * @return: a json object with the updated user information
	 * 
	 * This method handles PUT requests with the api/users/uID/ route as attempts to
	 * update user information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "api/users/{uID}/")
	public JSONObject updateUser(@PathVariable("uID") int uID, @RequestBody JSONObject body) {
		/*
		 * 1. Get UserService instance to update a user's information. 
		 * 2. retrieve new user information 
		 * 3. Return JSON serialized User object
		 */
		String firstName = (String) body.get("firstName");
		String lastName = (String) body.get("lastName");
		String email = (String) body.get("email");
		String campus = (String) body.get("campus");


		return userService.updateUser(uID, firstName, lastName, email, campus);
	};

	/**
	 * @param email: the user's email
	 * @param status: the status of the events in question
	 * 
	 * @return: a json object with information about the user's rsvp history
	 * 
	 * This method handles GET requests with the api/users/uID/rsvps/status/ route as attempts
	 * to retrieve user rsvp information and accesses the DAL to do that.
	 */

	@RequestMapping(method = RequestMethod.GET, value = "api/users/{email}/rsvps/{status}/")
	public JSONObject getUserRSVPS(@PathVariable("email") String email, @PathVariable("status") String status) {
		/*
		 * 1. Get UserService instance to retrieve user rsvps. 
		 * 2. Return JSON serialized Rsvp objects
		 */
		status = (status == null) ? "": status;
		return userService.getUserRSVPS(email, status);
	};

	/**
	 * @param uID: the user's id
	 * 
	 * @return: a json object with information about the user's subscriptions
	 * 
	 * This method handles GET requests with the api/users/uID/subscriptions/ routes as
	 * attempts to retrieve user subscription information and accesses the DAL to do
	 * that.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "api/users/{uID}/subscriptions/")
	public JSONObject getUserSubscriptions(@PathVariable("uID") int uID) {
		/*
		 * 1. Get UserService instance to retrieve user subscriptions. 
		 * 2. Return JSON serialized Subscription objects
		 */
		return userService.getUserSubscriptions(uID);
	};

	/**
	 * @param uID: the user's id
	 * 
	 * @return: a json object with information about the user's subscribers
	 * 
	 * This method handles GET requests with the api/users/uID/subscribers/ routes as
	 * attempts to retrieve a user's subscribers and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "api/users/{uID}/subscribers/")
	public JSONObject getUserSubscribers(@PathVariable("uID") int uID) {
		/*
		 * 1. Get UserService instance to retrieve user subscribers. 
		 * 2. Return JSON serialized User objects
		 */
		return userService.getUserSubscribers(uID);
	};
}