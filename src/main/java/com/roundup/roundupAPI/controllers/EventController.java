package com.roundup.roundupAPI.controllers;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roundup.roundupAPI.services.EventService;

/**
 * This module handles requests that have to do with events. It handles GET,
 * POST, DELETE, and PUT requests from the client API and provides a JSONObject
 * as a response.
 */
@CrossOrigin(origins = "*")
@RestController
public class EventController {
	/*
	 * An instance of the event service
	 */
	private EventService eventService = EventService.getInstance();

	/**
	 * @param body: the body of the http request 
	 * 
	 * @return: a json object with information about the newly added event
	 * 
	 * This method handles POST requests with the api/events/ route as attempts to add
	 * new event information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.POST, value="api/events/")
	public JSONObject addEvent(@RequestBody JSONObject body) {
		/*
		 * 1. Get EventService instance to create a new event. 
		 * 2. Retrieve new event information 
		 * 3. Return JSON serialized Event object
		 */
		int uID = body.get("uID") == null ? (int) body.get("owner") : (int) body.get("uID");
		String description = (String) body.get("description");
		Timestamp eventTime = new Timestamp((long )body.get("eventTime"));
		String posterUrl = (String) body.get("posterUrl");
		String name = (String) body.get("name");
		String location = (String) body.get("location");
		List<String> tagList = (List<String>) body.get("tags");
		Set<String> tags = new HashSet<String>(tagList);
		

		return eventService.addEvent(uID, description, eventTime, posterUrl, name, location, tags);
	};

	/**
	  * @param eID: the id associated with the event
	  *
	  * @return: a json object with information about the retrieved event object
	  *
	  * This method handles GET requests with the api/events/eventID/ route as attempts to
	  * retrieve event information and accesses the DAL to do that.
	*/
	@RequestMapping(method=RequestMethod.GET, value="api/events/{eID}/")
	public JSONObject getEvent(@PathVariable("eID") int eID) {
		/*
		 * 1. Get EventService instance to retrieve event information. 
		 * 2. Return JSON serialized Event object
		 */
		return eventService.getEvent(eID);
	};

	/**
	 * @param params: the http request parameters
	 * 
	 * This method handles DELETE requests with the api/events/ route as
	 * attempts to delete event information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value="api/events/")
	public void deleteEvent(@RequestParam LinkedHashMap params) {
		/*
		 * 1. Get EventService instance to delete an event.
		 */
		int eID = Integer.parseInt((String) params.get("eID"));
		int uID = Integer.parseInt((String) params.get("uID"));

		eventService.deleteEvent(eID, uID);
	};

	/**
	 * @param eID: the id of the event
	 * @param body: the body of the http request 
	 * 
	 * @return: a json object with information about the newly updated event
	 * 
	 * This method handles PUT requests with the api/events/eID/ route as attempts to update
	 * event information and accesses the DAL to do that.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "api/events/{eID}/")
	public JSONObject updateEvent(@PathVariable("eID") int eID, @RequestBody JSONObject body) {

		/*
		 * 1. Get EventService instance to update an event. 
		 * 2. Retrieve new event information 
		 * 3. Return JSON serialized Event object
		 */
		int uID = (int) body.get("uID");
		String description = (String) body.get("description");
		Timestamp eventTime = new Timestamp((long )body.get("eventTime"));
		String name = (String) body.get("name");
		String location = (String) body.get("location");
		List<String> tagList = (List<String>) body.get("tags");
		Set<String> tags = new HashSet<String>(tagList);

		return eventService.updateEvent(eID, uID, description, eventTime, name, location, tags);
	  };
	
	  /**
		 * @return All the tags existing in our database
		 * 
		 * This method handles GET requests with the api/tags route as
		 * attempts to retrieve information on event tags
		 */
		@RequestMapping(method=RequestMethod.GET, value="api/tags/")
		public JSONObject getAllTags() {
			/*
			 * 1. Get EventService instance to retrieve all tags. 
			 * 2. Return JSON serialized tags
			 */
			return eventService.getAllTags();
		}

		/**
		 *@param params: the http request parameters
		 * 
		 * @return: a list of events that satisfy the tags and status
		 * 
		 * This method handles GET requests with the api/events/tags route as
		 * attempts to retrieve information on events according to tags
		 */
		@RequestMapping(method=RequestMethod.GET, value="api/events/tags/")
		public JSONObject getEventsByTag(@RequestParam LinkedHashMap params) {
			/*
			 * 1. Get EventService instance to retrieve event by tags. 
			 * 2. Return JSON serialized event objects
			 */
			List<String> tagList = new ArrayList<>();
			String[] tagsArray = ((String) params.get("tags")).split(",");
			for (int i=0; i<tagsArray.length; i++) {
			        tagList.add(tagsArray[i]);
			}
			String status = ((String) params.get("status") == null) ? "": (String) params.get("status");
			Set<String> tags = new HashSet<String>(tagList);
			return eventService.getEventsByTag(tags, status);
		}

		/**
		 * @param uID: the id of the user in question
		 * @param params: the http request parameters
		 * 
		 * @return: a list of events owned by the user
		 * 
		 * This method handles GET requests with the api/events/owner/uID/ route as
		 * attempts to retrieve information on events according to the owner
		 */
		@RequestMapping(method=RequestMethod.GET, value="api/events/owner/{uID}/")
		public JSONObject getEventsByOwner(@PathVariable ("uID") int uID, @RequestParam LinkedHashMap params) {
			/*
			 * 1. Get EventService instance to retrieve event by owner. 
			 * 2. Return JSON serialized event objects
			 */
			String status = ((String) params.get("status") == null) ? "": (String) params.get("status");

			return eventService.getEventsByOwner(uID, status);
		}
		
		/**
		 * @@param params: the http request parameters
		 * 
		 * @return: a list of events with the search in the name
		 * 
		 * This method handles GET requests with the api/events/search/ route as
		 * attempts to retrieve information on events according to the specified search
		 */
		@RequestMapping(method=RequestMethod.GET, value="api/events/search/")
		public JSONObject getEventsByName(@RequestParam LinkedHashMap<String, String> params) {
			/*
			 * 1. Get EventService instance to retrieve event by name search. 
			 * 2. Return JSON serialized event objects
			 */
			String search = (String) params.get("search");
			String status = ((String) params.get("status") == null) ? "": (String) params.get("status");
			return eventService.getEventsByName(search, status);
		}
		
		/**
		 * @param params: the http request parameters
		 * 
		 * @return: a list of events happening between the given times
		 * 
		 * This method handles GET requests with the api/events/timeframe/ route as
		 * attempts to retrieve information on events according to the timeframe
		 */
		@RequestMapping(method=RequestMethod.GET, value="api/events/timeframe/")
		public JSONObject getEventsByTime(@RequestParam LinkedHashMap<String, String> params) {
			/*
			 * 1. Get EventService instance to retrieve event events by fromframe. 
			 * 2. Return JSON serialized event objects
			 */
			Timestamp fromTime = new Timestamp((Long.parseLong((String) params.get("fromTime"))));
			Timestamp toTime = new Timestamp((Long.parseLong((String) params.get("toTime"))));
			return eventService.getEventsByTime(fromTime, toTime);
		}
		
	/**
	 * @param eID: the id of the event
	 * 
	 * @return: a json object with information about an event's attendees
	 * 
	 * This method handles GET requests with the api/events/eID/attendees/ route as
	 * attempts to retrieve information on event attendees and accesses the DAL to
	 * do that.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "api/events/{eID}/attendees/")
	public JSONObject getEventAttendees(@PathVariable("eID") int eID) {
		/*
		 * 1. Get EventService instance to retrieve event attendees. 
		 * 2. Return JSON serialized User objects
		 */
		return eventService.getAttendees(eID);
	};
};
