package com.roundup.roundupAPI.services;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.roundup.roundupAPI.database.DAL;


/*
This class calls the data access layer and performs the desired operations to
retrieve rsvp information from the database.
*/
@Service
public class RSVPService {
	
	private DAL dal = DAL.getInstance();
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
	    2. Create an rsvp object with the provided rsvp information.
	    3. Return the rsvp object.
	  */		
		// performing operation
		dal.rsvpTo(email, name, eventID, time);
	};
	
	
	/**
	  @param rsvspID: the id of the rsvsp object
	
	  @return: the delete RSVP object
	
	  This method deletes rsvsp information from the database.
	*/
	public void deleteRsvp(String email, int eventID) {
	  /*
	    1. Pass the rsvp id to the data access layer and tell it to delete the rsvp from the database.
	    2. Create an rsvp object with the provided rsvp information.
	    3. Return the RSVP object.
	  */
		dal.unRsvpFrom(email, eventID);;
	};
}