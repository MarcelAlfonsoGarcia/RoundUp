package com.roundup.roundupAPI;

import jakarta.ws.rs.Path;

import com.roundup.roundupAPI.resources.EventResource;
import com.roundup.roundupAPI.resources.RSVPResource;
import com.roundup.roundupAPI.resources.SubscriptionResource;
import com.roundup.roundupAPI.resources.UserResource;
//import com.roundup.demorest.resources.RSVPResource;
//import com.roundup.demorest.resources.SubscriptionResource;
//import com.roundup.demorest.resources.UserResource;


/**
This module acts an entry point for all http requests and routes them to the appropriate resources
so that they can be adequately processed.
*/
@Path("")
public class FrontendAPI {
	/**
	  @return: an event resource instance that handles the request.
	
	  This method handles requests that have the /events prefix and routes
	  them to the events resource
	*/
	@Path("events")
	public EventResource getEventResource() {
	  // route the request to the event resource
		return new EventResource();
	}
	
	/**
	  @return: a user resource instance that handles the request.
	
	  This method handles requests that have the /users prefix and routes
	  them to the users resource
	*/
	@Path("users")
	public UserResource getUserResource() {
	  // route the request to the user resource
		return new UserResource();
	}
	
	/**
	  @return: an rsvp resource instance that handles the request.
	
	  This method handles requests that have the /rsvps prefix and routes
	  them to the rsvp resource
	*/
	@Path("rsvps")
	public RSVPResource getRSVPResource() {
	  // route the request to the rsvp resource
		return new RSVPResource();
	}
	
	/**
	  @return: a jsubscription resource instance that handles the request.
	
	  This method handles requests that have the /subscriptions prefix and routes
	  them to the subscriptions resource
	*/
	@Path("subscriptions")
	public SubscriptionResource getSubscriptionResource() {
	  // route the request to the subscription resource
		return new SubscriptionResource();
	}
}