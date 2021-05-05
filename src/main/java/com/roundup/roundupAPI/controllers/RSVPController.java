package com.roundup.roundupAPI.controllers;

import java.sql.Timestamp;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.roundup.roundupAPI.services.RSVPService;

/**
 * This module handles requests that have to do with rsvps. It handles GET,
 * POST, DELETE, and PUT requests from the client API and provides a JsonObject
 * as a response.
 */
@RestController
public class RSVPController {

	/*
	 * An instance of the rsvp service
	 */
	@Autowired
	private RSVPService rsvpService;

	/*
	 * @param eventID: the id of the associated event
	 * 
	 * @param userID: the id of the associated user
	 * 
	 * @return: a json object with information about the newly added rsvp
	 * 
	 * This method handles POST requests with the /rsvps route as attempts to add
	 * new rsvp information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/rsvps/")
	public void addRsvp(@RequestBody JSONObject body) {
		/*
		 * 1. Get RsvpService instance to create a new rsvp. 2. Retrieve new rsvp
		 * information 3. Return JSON serialized Rsvp object
		 */
		String email = (String) body.get("email");
		String name = (String) body.get("name");
		int eID = (int) body.get("eID");

		rsvpService.addRsvp(email, name, eID);
	};

	/*
	 * @param email: the email of the user
	 * 
	 * @param eventID: the id of the event
	 * 
	 * @return: a json object with information about the deleted rsvp object
	 * 
	 * This method handles DELETE requests with the /rsvps route as attempts to
	 * delete rsvp information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "api/rsvps/")
	public void deleteRsvp(@RequestBody JSONObject body) {
		/*
		 * 1. Get RsvpService instance to delete an rsvp. 2. retrieve deleted rsvp
		 * information 3. Return JSON serialized RSVP object
		 */
		String email = (String) body.get("email");
		int eID = (int) body.get("eID");

		rsvpService.deleteRsvp(email, eID);
	};
};