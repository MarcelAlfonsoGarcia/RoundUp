package com.roundup.roundupAPI.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
//import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;

import com.roundup.roundupAPI.services.RSVPService;


/**
This module handles requests that have to do with rsvps.
It handles GET, POST, DELETE, and PUT requests from the client API and
provides a JsonObject as a response.
*/
@Path("/rsvps")
public class RSVPResource {
	
	/* 
	 * An instance of the rsvp service  
	*/
	private RSVPService rsvpServiceInstance = new RSVPService();

	/*
	  @param eventID: the id of the associated event
	  @param userID: the id of the associated user
	
	  @return: a json object with information about the newly added rsvp
	
	  This method handles POST requests with the /rsvps route as attempts to
	  add new rsvp information and accesses the DAL to do that.
	*/
	@POST
//	@Path("/addRsvp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void addRsvp(@QueryParam("email") String email, @QueryParam("name") String name, @QueryParam("eventID") int eventID) {
	  /*
	    1. Get RsvpService instance to create a new rsvp.
	    2. Retrieve new rsvp information
	    3. Return JSON serialized Rsvp object
	  */
		rsvpServiceInstance.addRsvp(email, name, eventID);
	};
	
	
	/*
	  @param email: the email of the user
	  @param eventID: the id of the event
	
	  @return: a json object with information about the deleted rsvp object
	
	  This method handles DELETE requests with the /rsvps route as attempts to
	  delete rsvp information and accesses the DAL to do that.
	*/
	@DELETE
//	@Path("/deleteRsvp")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteRsvp(@PathParam("email") String email, @PathParam("eventID") int eventID) {
	  /*
	    1. Get RsvpService instance to delete an rsvp.
	    2. retrieve deleted rsvp information
	    3. Return JSON serialized RSVP object
	  */
		rsvpServiceInstance.deleteRsvp(email, eventID);
	};
};