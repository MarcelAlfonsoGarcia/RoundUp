package com.roundup.roundupAPI.services;

import java.sql.Timestamp;
import org.springframework.stereotype.Service;
import com.roundup.roundupAPI.database.DAL;


/*
This class calls the data access layer and performs the desired operations to
retrieve rsvp information from the database.
*/
public class RSVPService {
	// an instance of the Data Access layer component
	private DAL dal;
	
	// a single instance of the class that is used by external components
	public static RSVPService instance;
	
	// a constructor for the class
	public RSVPService() {
		// retrieves an instance for the DAL if one does not exist
		if (dal == null) {
			dal = DAL.getInstance();
		}
	}
	
	// a method that retrieves the instance of the class
	public static RSVPService getInstance() {
	    // 1. creates an instance for the DAL if one does not exist

	    // 2. return class instance.
		if (instance == null) {
			instance = new RSVPService();
		}
		return instance;
	}
	/**
	    @param email: the email of the user who is rsvp'ing
	    @param name: the name of the user who is rsvp'ing
	    @param eventID: the id of event.
	    @param: time the time at which the user rsvp'd
	    This method adds a new event to the database
	*/
	public void addRsvp(String email, String name, int eventID, Timestamp time){
	  /*
	    1. Pass the rsvp information to the data access layer and tell it to add a new rsvp to the database.
	  */		
		// performing operation
		dal.rsvpTo(email, name, eventID, time);
	};

	/**
	 * @param rsvspID: the id of the rsvsp object
	 * 
	 * @return: the delete RSVP object
	 * 
	 *          This method deletes rsvsp information from the database.
	 */
	public void deleteRsvp(String email, int eventID) {
	  /*
	    1. Pass the rsvp id to the data access layer and tell it to delete the rsvp from the database.
	  */
		dal.unRsvpFrom(email, eventID);
	};
}