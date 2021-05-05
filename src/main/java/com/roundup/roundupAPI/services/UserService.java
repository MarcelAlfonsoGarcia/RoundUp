package com.roundup.roundupAPI.services;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import java.util.Base64;

import com.roundup.roundupAPI.database.DAL;

/*
This class calls the data access layer and performs the desired operations to
retrieve user information from the database.
*/
@Service
public class UserService {
	
	public static DAL dal = DAL.getInstance();
	
	/**
	  @param firstName: the first name of the new user
	  @param lastName: the last name of the new user
	  @param email: the email of the new user
	  @param password: the password of the new user
	  @param campus: the campus of the new user
	
	  @return: the newly added user
	
	  This method accesses the data layer and calls it to add a new user to the database;
	  */
	public JSONObject addUser(String firstName, String lastName, String email, String password, String campus) {
	  /*
	    1. Passes information to the data access layer to creat new user entry.
	    2. If successful, retrieves the user information to create a new User object.
	    3. Return newly created User object.
	    
	    TOOD: Ask Marcel about adding passwor field
	  */
		return dal.createUser(firstName, lastName, email, password, campus);
	};
	
	/**
	  @param email: the email of the user
	  @param password: the password of the user
	
	  @return the user's authentication token
	
	  This method logs in the user.
	*/
	public JSONObject login(String email, String password) {
	  /*
	    1. Query data acess layer to check if there's a user with the provided user information.
	    2. If user exists, create an authentication token and return it to the user
	    3. Return authentication token
	    
	    TODO: ASk Marcel about login functionality
	  */
		return dal.logInUser(email, password);
	};
	
	/**
	  @param token: an authentication token
	
	  @return the user's information
	
	  This method authenticates in the user.
	*/
	public JSONObject authenticate(String token) {
	  /*
	    1. QUery data access leyer to check if there's a valid unexpired authentication token for the user.
	    2. If the authentication token, retrieve the user information and create a new User object.
	    3. Return user object.
	  */
		String decodedToken = new String(Base64.getDecoder().decode(token));
        String[] authInfo = decodedToken.split(":");
        String email = "";
        String password = "";
        if (authInfo.length > 2) {
            email = authInfo[0];
            for (int i=1; i < authInfo.length; i++ ) {
                password += authInfo[i];
                if (i < authInfo.length - 1) password += ":";
            }
        } else {
            email = authInfo[0];
            password = authInfo[1];
        }
        
		return login(email, password);
	};
	
	/**
	  @param userID: the id of the user
	  @return: the retrieved user object
	
	  This method provides information for a given user
	*/
	public JSONObject getUser(int userID) {
	  /*
	    1. Passes the userID to the data access layer and tries to retrieve the entry.
	    2. If successful, retrieves the user information to create a new User object.
	    3. Return newly created User object.
	  */
		return DAL.getInstance().retrieveUser(userID);
	};
	
	/**
	  @param userID: the id of the user object
	  @return: the deleted user object
	
	  This method deletes a user from the database
	*/
	public void deleteUser(int userID) {
	  /*
	    1. Passes the user id and tells it to delete the entry.
	    2. If successful, retrieves the user information to create a new User object.
	    3. Return newly created User object.
	  */
		dal.deleteUser(userID);
	};
	
	/**
	  @param firstName: the first name of the new user
	  @param lastName: the last name of the new user
	  @param email: the email of the new user
	  @param password: the password of the new user
	  @param campus: the campus of the new user
	
	  @return: the newly added user
	
	  This method accesses the data layer and calls it to add a new user to the database;
	  */
	public JSONObject updateUser(int userID, String firstName, String lastName, String email, String campus) {
	  /*
	    1. Passes the user id to the data access layer and tells it to update the user information.
	    2. If successful, retrieves the user information to create a new User object.
	    3. Return newly created User object.
	  */
		return DAL.getInstance().updateUser(userID, firstName, lastName, email, campus);
	};
	
	/**
	  @param userID: the id of the user
	  
	  @return: a list of rsvps the user has previously created
	
	  This method retrieves a user's Rsvp's
	*/
	public JSONObject getUserRSVPS(String email, String status) {
	  /*
	    1. Passes the user id to the data access layer and asks it for rsvps with the provided user id.
	    2. If successful, it retrieves the list of rsvps and creates an object with each of them, and adds
	    them to a list.
	    3. Return the list of RSVP objects.
	    
	    TODO: Ask Marcel about using the userID instead
	  */
		return DAL.getInstance().retrieveRsvpdEvents(email, status);
	};
	
	/**
	  @param userID: the id of the user
	  @return: a list of subscriptions the user has previously created
	
	  This method retrieves a user's subscriptions
	*/
	public JSONObject getUserSubscriptions(int userID) {
	  /*
	    1. Passes the user id to the data access layer and asks it for subscriptions with the provided user id as a follower.
	    2. If successful, it retrieves the list of rsvps and creates an object with each of them, and adds
	    them to a list.
	    3. Return the list of RSVP objects.
	  */
		return DAL.getInstance().retrieveSubscriptions(userID);
	};
	
	/**
	@param followedID: the id of the user being followed
	
	@return: A list of users that follow the current user
	
	This method provides a list of users who have subscribed to the user.
	*/
	public JSONObject getUserSubscribers(int followedID) {
	/*
	  1. Passes the follower id to the data access layer and asks it for subscribers with the provided followed user id.
	  2. If successful, it retrieves the list of users and creates an object with each of them, and adds
	  them to a list.
	  3. Return the list of Subscription objects.
	*/
		return DAL.getInstance().retrieveSubscribers(followedID);
	};
}
