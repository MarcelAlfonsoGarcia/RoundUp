package com.roundup.roundupAPI.controllers;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundup.roundupAPI.services.EventService;

/**
 * This module handles requests that have to do with events. It handles GET,
 * POST, DELETE, and PUT requests from the client API and provides a JSONObject
 * as a response.
 */
@RestController
@ComponentScan("com.roundup.roundupAPI.services.EventService")
@RequestMapping(value = "api/events")
public class EventController {
	/*
	 * An instance of the event service
	 */
	@Autowired
	private EventService eventService;

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
	@RequestMapping(method = RequestMethod.POST)
	public JSONObject addEvent(@RequestBody JSONObject body) {
		/*
		 * 1. Get EventService instance to create a new event. 2. Retrieve new event
		 * information 3. Return JSON serialized Event object
		 */
		int uID = (int) body.get("uID");
		String description = (String) body.get("description");
		Timestamp eventTime = (Timestamp) body.get("eventTime");
		String posterUrl = (String) body.get("posterUrl");
		String name = (String) body.get("name");
		String location = (String) body.get("location");
		List<String> tagList = (List<String>) body.get("tags");
		Set<String> tags = new HashSet<String>(tagList);

		return eventService.addEvent(uID, description, eventTime, posterUrl, name, location, tags);
	};

	/*
	 * @param eventID: the id of the event
	 * 
	 * @return: a json object with information about the retrieved event object
	 * 
	 * This method handles GET requests with the /events/eventID route as attempts
	 * to retrieve event information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{eID}")
	public JSONObject getEvent(@PathVariable("eID") int eID) {
		/*
		 * 1. Get EventService instance to retrieve event information. 2. Return JSON
		 * serialized Event object
		 */
		return eventService.getEvent(eID);
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
	@RequestMapping(method = RequestMethod.DELETE)
	public void deleteEvent(@RequestBody JSONObject body) {
		/*
		 * 1. Get EventService instance to delete an event. 2. retrieve deleted event
		 * information 3. Return JSON serialized Event object
		 */
		int eID = (int) body.get("eID");
		int uID = (int) body.get("uID");

		eventService.deleteEvent(eID, uID);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{eID}/")
	public JSONObject updateEvent(@PathVariable("eID") int eID, @RequestBody JSONObject body) {

		/*
		 * 1. Get EventService instance to update an event. 2. Retrieve new event
		 * information 3. Return JSON serialized Event object
		 */
		int uID = (int) body.get("uID");
		String description = (String) body.get("description");
		Timestamp eventTime = (Timestamp) body.get("eventTime");
		String name = (String) body.get("name");
		String location = (String) body.get("location");
		List<String> tagList = (List<String>) body.get("tags");
		Set<String> tags = new HashSet<String>(tagList);

		return eventService.updateEvent(eID, uID, description, eventTime, name, location, tags);
	};

	/**
	 * @return All the tags existing in our database
	 * 
	 *         This method handles GET requests with the /events/tags route as
	 *         attempts to retrieve information on event tags
	 */
	@RequestMapping(method = RequestMethod.GET, value = "api/tags")
	public JSONObject getAllTags() {
		return eventService.getAllTags();
	}

	/**
	 * @param tags:   a list of tags associated with the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events that satisfy the tags and status
	 * 
	 *          This method handles GET requests with the /events/ route as attempts
	 *          to retrieve information on events according to tags
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/tags")
	public JSONObject getEventsByTag(@RequestBody JSONObject body) {
		String status = (String) body.get("status");
		List<String> tagList = (List<String>) body.get("tags");
		Set<String> tags = new HashSet<String>(tagList);
		return eventService.getEventsByTag(tags, status);
	}

	/**
	 * @param userID: the ID of the user who owns the events
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events owned by the user
	 * 
	 *          This method handles GET requests with the /events/userID route as
	 *          attempts to retrieve information on events according to the owner
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{uID}")
	public JSONObject getEventsByOwner(@PathVariable("uID") int uID, @RequestBody JSONObject body) {
		String status = (String) body.get("status");
		return eventService.getEventsByOwner(uID, status);
	}

	/**
	 * @param search: a string that should be contained in the name of the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events with the search in the name
	 * 
	 *          This method handles GET requests with the /events/userID route as
	 *          attempts to retrieve information on events according to the
	 *          specified search
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public JSONObject getEventsByName(@RequestBody JSONObject body) {
		String search = (String) body.get("search");
		String status = (String) body.get("status");
		return eventService.getEventsByName(search, status);
	}

	/**
	 * @param fromTime: the earliest time for the events in question
	 * @param toTime:   the latest time for the events in question
	 * 
	 * @return: a list of events happening between the given times
	 * 
	 *          This method handles GET requests with the /events/ route as attempts
	 *          to retrieve information on events according to the timeframe
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/timeframe")
	public JSONObject getEventsByTime(@RequestBody JSONObject body) {
		Timestamp fromTime = Timestamp.valueOf((String) body.get("fromTime"));
		Timestamp toTime = Timestamp.valueOf((String) body.get("toTime"));

		return eventService.getEventsByTime(fromTime, toTime);
	}

	/*
	 * @param eventID: the id of the event
	 * 
	 * @return: a json object with information about an event's attendees
	 * 
	 * This method handles GET requests with the /events/eventID/attendees route as
	 * attempts to retrieve information on event attendees and accesses the DAL to
	 * do that.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{eID}/attendees/")
	public JSONObject getAttendees(@PathVariable("eID") int eID) {
		/*
		 * 1. Get EventService instance to retrieve event attendees. 2. Return JSON
		 * serialized User objects
		 */
		return eventService.getAttendees(eID);
	};
};
