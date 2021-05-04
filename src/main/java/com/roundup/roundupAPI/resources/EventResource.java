package com.roundup.roundupAPI.resources;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import com.roundup.roundupAPI.services.EventService;

/**
 * This module handles requests that have to do with events. It handles GET,
 * POST, DELETE, and PUT requests from the client API and provides a JSONObject
 * as a response.
 */
@Path("/events")
public class EventResource {
	/*
	 * An instance of the event service
	 */
	private EventService eventServiceInstance = new EventService();

	/*
	 * @param userID: the userID of the associated owner
	 * 
	 * @param description: the description of the event
	 * 
	 * @param eventTime: the event time
	 * 
	 * @param poster: the url of the associated poster
	 * 
	 * @param name: the name of the event
	 * 
	 * @param location: the location of the event
	 * 
	 * @param tags: a list of tags associated with event
	 * 
	 * @return: a json object with information about the newly added event
	 * 
	 * This method handles POST requests with the /events route as attempts to add
	 * new event information and accesses the DAL to do that.
	 */
	@POST
//	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject addEvent(@QueryParam("userID") int userID, @QueryParam("description") String description,
			@QueryParam("eventTime") Timestamp eventTime, @QueryParam("poster") String poster,
			@QueryParam("name") String name, @QueryParam("location") String location,
			@QueryParam("tags") Set<String> tags) {
		/*
		 * 1. Get EventService instance to create a new event. 2. Retrieve new event
		 * information 3. Return JSON serialized Event object
		 */
		return eventServiceInstance.addEvent(userID, description, eventTime, poster, name, location, tags);
	};

	/*
	 * @param eventID: the id of the event
	 * 
	 * @return: a json object with information about the retrieved event object
	 * 
	 * This method handles GET requests with the /events/eventID route as attempts
	 * to retrieve event information and accesses the DAL to do that.
	 */
	@GET
	@Path("/{eventID}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getEvent(@PathParam("eventID") int eventID) {
		/*
		 * 1. Get EventService instance to retrieve event information. 2. Return JSON
		 * serialized Event object
		 */
		return eventServiceInstance.getEvent(eventID);
	};

	/*
	 * @param eventID: the id of the event
	 * 
	 * @param userID: the id of the user
	 * 
	 * @return: a json object with information about the deleted event object
	 * 
	 * This method handles DELETE requests with the /events/eventID route as
	 * attempts to delete event information and accesses the DAL to do that.
	 */
	@DELETE
	@Path("/{eventID}")
//	@Produces(MediaType.APPLICATION_JSON)
	public void deleteEvent(@PathParam("eventID") int eventID, @PathParam("userID") int userID) {
		/*
		 * 1. Get EventService instance to delete an event. 2. retrieve deleted event
		 * information 3. Return JSON serialized Event object
		 */
		eventServiceInstance.deleteEvent(eventID, userID);
	};

	/*
	 * @param eventID: the id of the event
	 * 
	 * @param userID: the userID of the associated owner
	 * 
	 * @param description: the description of the event
	 * 
	 * @param eventTime: the event time
	 * 
	 * @param poster: the url of the associated poster
	 * 
	 * @param name: the name of the event
	 * 
	 * @param location: the location of the event
	 * 
	 * @param tags: a list of tags associated with event
	 * 
	 * @return: a json object with information about the newly updated event
	 * 
	 * This method handles PUT requests with the /events route as attempts to update
	 * event information and accesses the DAL to do that.
	 */
	@PUT
	@Path("/{eventID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject updateEvent(@PathParam("eventID") int eventID, @QueryParam("userID") int userID,
			@QueryParam("description") String description, @QueryParam("eventTime") Timestamp eventTime,
			@QueryParam("poster") String poster, @QueryParam("name") String name,
			@QueryParam("location") String location, @QueryParam("tags") Set<String> tags) {
		/*
		 * 1. Get EventService instance to update an event. 2. Retrieve new event
		 * information 3. Return JSON serialized Event object
		 */
		return eventServiceInstance.updateEvent(eventID, userID, description, eventTime, name, location, tags);
	};

	/**
	 * @return All the tags existing in our database
	 * 
	 * This method handles GET requests with the /events/tags route as
	 * attempts to retrieve information on event tags
	 */
	@GET
	@Path("/tags")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAllTags() {
		return eventServiceInstance.getAllTags();
	}

	/**
	 * @param tags:   a list of tags associated with the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events that satisfy the tags and status
	 * 
	 * This method handles GET requests with the /events/ route as
	 * attempts to retrieve information on events according to tags
	 */
//	@GET
//	@Path("/")
//	@Produces(MediaType.APPLICATION_JSON)
//	public JSONObject getEventsByTag(@QueryParam("tags") Set<String> tags, @QueryParam("status") String status) {
//		return eventServiceInstance.getEventsByTag(tags, status);
//	}

	/**
	 * @param userID: the ID of the user who owns the events
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events owned by the user
	 * 
	 * This method handles GET requests with the /events/userID route as
	 * attempts to retrieve information on events according to the owner
	 */
//	@GET
//	@Path("/{userID}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public JSONObject getEventsByOwner(@QueryParam("userID") int userID, @QueryParam("status") String status) {
//		return eventServiceInstance.getEventsByOwner(userID, status);
//	}
//	
	/**
	 * @param search: a string that should be contained in the name of the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events with the search in the name
	 * 
	 * This method handles GET requests with the /events/userID route as
	 * attempts to retrieve information on events according to the specified search
	 */
//	@GET
//	@Path("/")
//	@Produces(MediaType.APPLICATION_JSON)
//	public JSONObject getEventsByName(@QueryParam("search") String search, @QueryParam("status") String status) {
//		return eventServiceInstance.getEventsByName(search, status);
//	}
	
	/**
	 * @param fromTime: the earliest time for the events in question
	 * @param toTime:   the latest time for the events in question
	 * 
	 * @return: a list of events happening between the given times
	 * 
	 * This method handles GET requests with the /events/ route as
	 * attempts to retrieve information on events according to the timeframe
	 */
//	@GET
//	@Path("/")
//	@Produces(MediaType.APPLICATION_JSON)
//	public JSONObject getEventsByTime(@QueryParam("fromTime") Timestamp fromTime, @QueryParam("toTime") Timestamp toTime) {
//		return eventServiceInstance.getEventsByTime(fromTime, toTime);
//	}
	
	/*
	 * @param eventID: the id of the event
	 * 
	 * @return: a json object with information about an event's attendees
	 * 
	 * This method handles GET requests with the /events/eventID/attendees route as
	 * attempts to retrieve information on event attendees and accesses the DAL to
	 * do that.
	 */
	@GET
	@Path("/{eventID}/attendees")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAttendees(@PathParam("eventID") int eventID) {
		/*
		 * 1. Get EventService instance to retrieve event attendees. 2. Return JSON
		 * serialized User objects
		 */
		return eventServiceInstance.getAttendees(eventID);
	};
};
//public class EventResource {
//	@GET
//	@Path("/")
//	@Produces(MediaType.TEXT_PLAIN)
//	public String test() {
//		return "EVENTS HERE!!!";
//	}
//	
//}