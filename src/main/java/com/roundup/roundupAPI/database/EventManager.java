package com.roundup.roundupAPI.database;

import java.sql.Timestamp;

/**
 * This class is intended to manage the status of events automatically. This
 * means that past events will be set to inactive status and the like. In future
 * versions, we intend to have this class at the center of more advanced
 * business rules regarding posters. Things like appropriate language, college
 * guidelines, and admin specifications will be enforced through this component
 * in future releases.
 * 
 * This class will stand away from the usual components as it does not require
 * client interactions to follow nor does it intend to produce any outputs.
 * Ideally, it will run every day at a preset time to ensure that the events are
 * updated as fast as possible but within the constraints of how much processing
 * power we are allowed to use under a free Heroku plan.
 */
public class EventManager {

	/**
	 * @param none
	 * @return void
	 * 
	 *         This method will have a pre-set time of the day to begin execution.
	 *         Once that time of day arrives, it will obtain an instance of the DAL
	 *         and update the status of every event at once.
	 * 
	 *         It will first record the current date and time, and calculate the
	 *         date and time a week from the present day. It will then perform a
	 *         query to set all active events that have passed (according to present
	 *         date and time) to inactive by calling updateEventStatus from the DAL
	 *         with Jan 1st, 1970 as the from Date and the present date as the To
	 *         date.
	 * 
	 *         It will then perform a query to set all active events that will occur
	 *         in the upcoming week (from the present date and time) to upcoming by
	 *         calling updateEventStatus from the DAL
	 */
	private void updateAllEventsStatus() {
        
		// this time corresponds to Jan 1st 1970
		Timestamp distantPast = new Timestamp(10000);
		
		// this time corresponds to the present time
		Timestamp current = new Timestamp(System.currentTimeMillis());
		
		// this time corresponds to a week from right now
		// the number 604800000 is the amount of milliseconds in a week
		Timestamp future = new Timestamp(System.currentTimeMillis() + 604800000);

		DAL.getInstance().updateEventStatus("active", current, future);

		DAL.getInstance().updateEventStatus("inactive", distantPast, current);
	}

}
