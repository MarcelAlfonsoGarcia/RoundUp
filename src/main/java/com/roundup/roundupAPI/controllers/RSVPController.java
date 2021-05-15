package com.roundup.roundupAPI.controllers;

import java.sql.Timestamp;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.roundup.roundupAPI.services.RSVPService;

/**
 * This module handles requests that have to do with rsvps. It handles
 * POST and DELETE requests from the client API and provides a JSONObject
 * as a response.
 */
@CrossOrigin(origins = "*")
@RestController
public class RSVPController {

	/*
	 * An instance of the rsvp service
	 */
	private RSVPService rsvpService = RSVPService.getInstance();

	/**
	 * @param body: the body of the http request
	 * 
	 * This method handles POST requests with the api/rsvps/ route as attempts to add
	 * new rsvp information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/rsvps/")
	public void addRsvp(@RequestBody JSONObject body) {
		/*
		 * 1. Get RsvpService instance to create a new rsvp.
		 */
		String email = (String) body.get("email");
		String name = (String) body.get("name");
		int eID = (int) body.get("eID");
		Timestamp time = new Timestamp((long) body.get("time"));
		
		rsvpService.addRsvp(email, name, eID, time);
	};

	/**
	 * @param body: the body of the http request 
	 * 
	 * This method handles DELETE requests with the api/rsvps/ route as attempts to
	 * delete rsvp information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "api/rsvps/")
	public void deleteRsvp(@RequestBody JSONObject body) {
		/*
		 * 1. Get RsvpService instance to delete an rsvp.
		 */
		String email = (String) body.get("email");
		int eID = (int) body.get("eID");

		rsvpService.deleteRsvp(email, eID);
	};
};