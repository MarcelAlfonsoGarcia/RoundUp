package com.roundup.roundupAPI.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;

import com.roundup.roundupAPI.services.UserService;


/**
This module handles requests that have to do with the user id.
It handles GET, POST, DELETE, and PUT requests from the client API and
provides a JSONObject as a response.
*/
@Path("/users")
public class UserResource {
	/* 
	 * An instance of the user service  
	*/
	private UserService userServiceInstance = new UserService();
	/*
	  @param firstName: the first name of the new user
	  @param lastName: the last name of the new user
	  @param email: the email of the new user
	  @param password: the password of the new user
	  @param campus: the campus of the new user
	
	  @return: a json object with information about the newly added user
	
	  This method handles POST requests with the /users/register route as attempts
	  to create a new user and creates a new user.
	*/
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void createUser( @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("campus") String campus) {
	  /*
	    1. Get UserService instance
	    2. Retrieve new user information
	    3. Return JSON serialized User object
	  */
		userServiceInstance.addUser(firstName, lastName, email, password, campus);
	};
	
	
	/*
	  @param email: the email of the new user
	  @param password: the password of the new user
	
	  @return: a json object with information about the newly added user
	
	  This method handles requests with the /users/login route as attempts to log in the user
	  and tries to check if the provided credentials are correct.
	*/
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject login(
	  @PathParam("email") String email,
	  @PathParam("password") String password
	  ) {
	    /*
	      1. Get UserService instance to check user credentials.
	      2. If successful, retrieve user information and token
	      3. Return JSON serialized User object and token
	    */
		return userServiceInstance.login(email, password);
	  };
	
	/*
	  @param userID: the user's id
	
	  @return: a json object with information about the retrieved user
	
	  This method handles GET requests with the /users/userID route as attempts to
	  retrieve user information and accesses the DAL to do that.
	*/
	@GET
	@Path("/{userID}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUser(@PathParam("userID") int userID) {
	  /*
	    1. Get UserService instance to retrieve user information.
	    2. Return JSON serialized User object
	  */
		System.out.println(userID);
		return userServiceInstance.getUser(userID);
	};
	
	/*
	  @param userID: the user's id
	
	  @return: a json object with information about the deleted user
	
	  This method handles DELETE requests with the /users/userID route as attempts to
	  delete user information and accesses the DAL to do that.
	*/
	@DELETE
	@Path("/{userID}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteUser(@PathParam("userID") int userID) {
	  /*
	    1. Get UserService instance to delete a user.
	    2. retrieve deleted user information
	    3. Return JSON serialized User object
	  */
		userServiceInstance.deleteUser(userID);
	};
	
	/*
	  @param userID: the user's id
	
	  @return: a json object with the updated user information
	
	  This method handles PUT requests with the /users/userID route as attempts to
	  update user information and accesses the DAL to do that.
	*/
	@PUT
	@Path("/{userID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject updateUser(@PathParam("userID") int userID, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @QueryParam("email") String email, @QueryParam("campus") String campus) {
	  /*
	    1. Get UserService instance to update a user's information.
	    2. retrieve new user information
	    3. Return JSON serialized User object
	  */
		return userServiceInstance.updateUser(userID, firstName, lastName, email, campus);
	};
	
	/*
	  @param email: the user's email
	  @param status: the status of the events in question
	
	  @return: a json object with information about the user's rsvp history
	
	  This method handles GET requests with the /users/userID/rsvps route as attempts to
	  retrieve user rsvp information and accesses the DAL to do that.
	*/
	@GET
	@Path("/{email}/rsvps/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserRSVPS(@PathParam("email") String email, @PathParam("status") String status) {
	  /*
	    1. Get UserService instance to retrieve user rsvps.
	    2. Return JSON serialized Rsvp objects
	  */
		return userServiceInstance.getUserRSVPS(email, status);
	};
	
	/*
	  @param userID: the user's id
	
	  @return: a json object with information about the user's subscriptions
	
	  This method handles GET requests with the /users/userID/subscriptions routes as attempts to
	  retrieve user subscription information and accesses the DAL to do that.
	*/
	@GET
	@Path("/{userID}/subscriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserSubscriptions(@PathParam("userID") int userID) {
	  /*
	    1. Get UserService instance to retrieve user subscriptions.
	    2. Return JSON serialized Subscription objects
	  */
		return userServiceInstance.getUserSubscriptions(userID);
	};
	
	/*
		@param userID: the user's id
		
		@return: a json object with information about the user's subscribers
		
		This method handles GET requests with the /users/userID/subscribers routes as attempts to
		retrieve a user's subscribers and accesses the DAL to do that.
	*/
	@GET
	@Path("/{userID}/subscribers")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserSubscribers(@PathParam("userID") int userID) {
	/*
	  1. Get UserService instance to retrieve user subscribers.
	  2. Return JSON serialized Uuser objects
	*/
		return userServiceInstance.getUserSubscribers(userID);
	};
};