package com.roundup.roundupAPI.services;

import java.sql.Timestamp;
import java.util.Set;
import org.json.simple.JSONObject;

import com.roundup.roundupAPI.database.DAL;

/*
This class calls the data access layer and performs the desired operations to
retrieve event information from the database.
*/
//@Service
public class EventService {
	// an instance of the Data Access layer component
	private DAL dal;
	
	// a single instance of the class that is used by external components
	public static EventService instance;
	
	// a constructor for the class
	public EventService() {
		// retrieves an instance for the DAL if one does not exist
		if (dal == null) {
			dal = DAL.getInstance();
		}
	}
	
	// a method that retrieves the instance of the class
	public static EventService getInstance() {
		// 1. creates an instance for the DAL if one does not exist

	    // 2. return class instance.
		if (instance == null) {
			instance = new EventService();
		}
		return instance;
	}
	
	
	/**
	  @param userId: the ID of the user who is creating the event
	  @param description: the description of the event to be created
	  @param eventTime: the time the event will take place
	  @param poster: the url for the poster corresponding to this event
	  @param name: the name of the event given by the user
	  @param location: the location of the event as given by the user
	  @param tags: a list of tags associated with event
	
	  @return: the newly created event object
	
	  This method adds a new event to the database
	*/
	public JSONObject addEvent(int userID, String description, Timestamp eventTime, String poster, String name, String location, Set<String> tags) {
	  /*
	    1. Pass the event information to the data access layer and tell it to add a new event to the database.
	    2. Create an event object with the provided event information.
	    3. Return the event object.
	  */
		return dal.createEvent(userID, eventTime, poster, name, description, location, tags);
	}

	/**
	 * @param eventId: the id of the specific event
	 * @return the newly retrieved event object
	 * 
	 *         THis method retireves and event with the given id.
	 */
	public JSONObject getEvent(int eventID) {
	  /*
	    1. Pass the event id to the data access layer and tell it to retrieve specific event from the database.
	    2. Create an event object with the retrieved information.
	    3. Return the event object.
	  */
		return dal.retrieveEvent(eventID);
	}

	/**
	  @param eventId:  the id of the event to be updated
	  @param userId: the user id of the user trying to update the event
	  @param description: the new description for the event
	  @param eventTime: the new time for the event
	  @param name: the new name for the event
	  @param location: the new location for the event
	  @param tags: a list of tags associated with event
	
	  @return: the newly updated event object
	
	  This method updates an event with new information.
	*/
	public JSONObject updateEvent(int eventID, int userId, String description, Timestamp eventTime, String name, String location, Set<String> tags) {
	  /*
	    1. Pass the event information to the data access layer and tell it to update the event information accordingly.
	    2. Create an event object with the provided event information.
	    3. Return the event object.
	  */
		return dal.updateEvent(eventID, userId, description, eventTime, name, location, tags);
	}

	/**
	 * @param eventId: the id of the specific event
	 * @param userID:  the person requesting the delete
	 * 
	 * @return: the deleted event object
	 * 
	 *          This method deletes an event with the given id.
	 */
	public void deleteEvent(int eventID, int userID) {
	  /*
	    1. Pass the event id to the data access layer and tell it to delete the event from the database.
	  */
		dal.deleteEvent(eventID, userID);
	}

	/**
	 * @return All the tags existing in our database. This is needed since we don't
	 *         allow users to create their own tags
	 * 
	 */
	public JSONObject getAllTags() {
		return dal.retrieveAllTags();
	}

	/**
	 * @param tags:   a list of tags associated with the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events that satisfy the tags and status
	 * 
	 *          This method retrieves a list of events the user that satisfy the
	 *          given tags/ status.
	 */
	public JSONObject getEventsByTag(Set<String> tags, String status) {
	  /*
	    1. Pass the tags and status to the database access layer and retrieve events that satify them.
	    2. Create Event objects with the given information.
	    3. Return the list of Event objects.
	  */
		return dal.retrieveEventsByTag(tags, status);
	}

	/**
	 * @param userId: the id of the user who owns the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events owned by the user and status
	 * 
	 *          This method retrieves a list of events owned by the user
	 */
	public JSONObject getEventsByOwner(int userId, String status) {

		return dal.retrieveEventsByOwner(userId, status);
	}

	/**
	 * @param search: a string that should be contained in the name of the event
	 * @param status: the status of events user is interested in
	 * 
	 * @return: a list of events with the search in the name and status
	 * 
	 *          This method retrieves a list of events that contain the search in
	 *          the name
	 */
	public JSONObject getEventsByName(String search, String status) {

		return dal.retrieveEventsByName(search, status);
	}

	/**
	 * @param fromTime: the earliest time for the events in question
	 * @param toTime:   the latest time for the events in question
	 * 
	 * @return: a list of events happening between the given times
	 * 
	 *          This method retrieves a list of events that are scheduled to occur
	 *          between the given times
	 */
	public JSONObject getEventsByTime(Timestamp fromTime, Timestamp toTime) {

		return dal.retrieveEventsByTime(fromTime, toTime);
	}

	/**
	 * @param eventID: the id of the user
	 * @return: a list of attendees for the event
	 * 
	 *          This method retrieves the list of attendees for a given event.
	 */
	public JSONObject getAttendees(int eventID) {
	  /*
	    1. Passes the event id to the data access layer and asks it for rsvps with the provided event id.
	    2. Asks the data access layer for information on users with the retrieved user ids.
	    3. If successful, it creates user objects with the proided information and, and adds
	    them to a list.
	    4. Return the list of User objects.
	  */
		return dal.retrieveAttendees(eventID);
	};

}