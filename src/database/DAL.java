/**
	DAL = Data Access Layer
	This module will serve as the API meant for connecting and querying the database.
	It will follow a Singleton design pattern that exposes its methods to other classes.
	Each public method will describe a query that will be customizable through the
	passed parameters.
*/
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class DAL {

	// the singleton instance of the DAL object
	private static DAL instance;
	private static Connection c;

	/**
	 * @param none
	 * @return DAL type object
	 * 
	 *         Instantiator for the DAL object.
	 * 
	 *         Simply connect using the database address, valid username, and
	 *         password Must remember to close the connection once all operations
	 *         are finished
	 */
	private DAL() {
		String dbUrl = System.getenv("JDBC_DATABASE_URL");
		try {
			c = DriverManager.getConnection(dbUrl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param none
	 * @return DAL type object
	 * 
	 *         This method will attempt to obtain a DAL instance. If one already
	 *         exists, it will be returned. If not, one will be created.
	 */
	public static DAL getInstance() {
		if (instance == null) {
			instance = new DAL();
		}

		return instance;
	}

	/**
	 * @param firstName: the first name of the user to be created
	 * @param lastName: the last name of the user to be created
	 * @param email: the email of the user to be created
	 * @param campus: the campus of the user to be created
	 * @return user: A JSON object representing the newly created user, it includes
	 *         the userId
	 * 
	 *         This method will create the user in the database through data input.
	 * 
	 *         Check that the firstName, lastName, and email fields are not null If
	 *         so return a JSON error message saying that the fields cannot be left
	 *         empty Check that the email does not already exist in the database, we
	 *         do so by running the boolean query: EXISTS (SELECT 1 FROM users WHERE
	 *         email = email) If true, return error message saying that the email is
	 *         already in use If false, attempt to create the new user in the table
	 *         with the given data INSERT INTO users (firstName, lastName, email,
	 *         campus,) VALUES (firstName, lastName, email, campus) Transform
	 *         information into JSONObject and return
	 * 
	 *         To return the user, we will have to run a SELECT query with the
	 *         specific user info
	 * 
	 *         Query results will be transformed into JSON with the
	 *         userJsonTransformer method
	 */
	public JSONObject createUser(String firstName, String lastName, String email, String campus) {

		if (firstName == null || firstName.isEmpty()) {
			throw new IllegalArgumentException("First Name cannot be left empty");
		} else if (lastName == null || lastName.isEmpty()) {
			throw new IllegalArgumentException("Last Name cannot be left empty");
		} else if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email cannot be left empty");
		}
		
		Statement s = null;
		try {
			s = c.createStatement();
			String check = "EXISTS (SELECT 1 FROM users WHERE email = " + email + ");";
			
			StringBuilder query = new StringBuilder("INSERT INTO users (firstName, lastName, email, campus) VALUES (");
			query.append(firstName).append(", ").append(lastName).append(", ").append(email).append(", ").append(campus)
					.append(") RETURNING *;");

			ResultSet userResult = s.executeQuery(query.toString());
			s.close();
			return userJsonTransformer(userResult);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("First Name cannot be left empty");
		}
	}

	/**
	 * @param userId: the id of the user we want to retrieve information from
	 * @return user: A JSON object with all the information of the user retrieved
	 * 
	 *         This method will obtain all the user information stored in the user
	 *         table of the database.
	 * 
	 *         Check that the user exists in the database, we do so by running the
	 *         boolean query: EXISTS (SELECT 1 FROM users WHERE uID = userId) If
	 *         false, return error message saying that the user does not exist If
	 *         true, run a query to retrieve the user from the database SELECT *
	 *         FROM users WHERE uID = userId Transform information into JSONObject
	 *         and return
	 * 
	 *         Query results will be transformed into JSON with the
	 *         userJsonTransformer method
	 */
	public JSONObject retrieveUser(int userId) {
		Statement s = null;

		try {
			s = c.createStatement();
			String check = "EXISTS (SELECT 1 FROM users WHERE uID = " + userId + ");";

			ResultSet userResult = s.executeQuery("SELECT * FROM users WHERE uID = " + userId);
			s.close();

			return userJsonTransformer(userResult);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("First Name cannot be left empty");
		}
	}

	/**
	 * @param userId: the id of the user we want to delete
	 * @return nothing
	 * 
	 *         This method will delete a user from the database
	 * 
	 *         Check that the user to be deleted exists by running the query: EXISTS
	 *         (SELECT 1 FROM users WHERE uID = userId) If false, return an error
	 *         message saying that the user does not exist If true, run a query to
	 *         remove the user from the database DELETE FROM users WHERE uID =
	 *         userId And return a message informing that the user was deleted
	 */
	public void deleteUser(int userId) {
		Statement s = null;

		try {
			s = c.createStatement();
			String check = "EXISTS (SELECT 1 FROM users WHERE uID = " + userId + ");";

			s.executeUpdate("DELETE FROM users WHERE uID = " + userId);
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("First Name cannot be left empty");
		}
	}

	/**
	 * @param userId: the user id of the user to be updated
	 * @param firstName: the first name of the user to be updated
	 * @param lastName: the last name of the user to be updated
	 * @param email: the email of the user to be updated
	 * @param campus: the campus of the user to be updated
	 * @param cohort: the cohort of the user to be updated
	 * @return user: A JSON object representing the newly updated user
	 * 
	 *         This method will update all the fields in the database for a specific
	 *         user.
	 * 
	 *         Check that the firstName, lastName, and email are not empty if so,
	 *         return an error message saying they can't be empty Check that the new
	 *         email isn't in use, do so by running the query EXISTS (SELECT 1 FROM
	 *         users WHERE email = email) if so, return an error message saying that
	 *         the email is in use Otherwise, check that the user exists by running
	 *         the query: EXISTS (SELECT 1 FROM users WHERE uID = userId) If false,
	 *         return an errror message saying that the user does not exist If true,
	 *         run the query: UPDATE users SET firstName = firstName, lastName =
	 *         lastName, .... WHERE uID = userId Transform information into
	 *         JSONObject and return
	 * 
	 *         To return the user, we will have to run a SELECT query with the
	 *         specific user info
	 * 
	 *         Query results will be transformed into JSON with the
	 *         userJsonTransformer method
	 */
	public JSONObject updateUser(int userId, String firstName, String lastName, String email, String campus) {
		
		if (firstName == null || firstName.isEmpty()) {
			throw new IllegalArgumentException("First Name cannot be left empty");
		} else if (lastName == null || lastName.isEmpty()) {
			throw new IllegalArgumentException("Last Name cannot be left empty");
		} else if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email cannot be left empty");
		}
		
		Statement s = null;
		try {
			s = c.createStatement();
			String check = "EXISTS (SELECT 1 FROM users WHERE uID = " + userId + ");";

			StringBuilder query = new StringBuilder("UPDATE users SET ");
			query.append(firstName).append(", ").append(lastName).append(", ").append(email).append(", ").append(campus)
					.append(") ");
			query.append("WHERE uID =" + userId);
			query.append(" RETURNING *;");

			ResultSet rs = s.executeQuery(query.toString());
			s.close();

			return userJsonTransformer(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("First Name cannot be left empty");
		}
	}

	/**
	 * @param userId: the ID of the user who is creating the event
	 * @param description: the description of the event to be created
	 * @param eventTime: the time the event will take place
	 * @param posterUrl: the url for the poster corresponding to this event
	 * @param name: the name of the event given by the user
	 * @param location: the location of the event as given by the user
	 * @return user: A JSON object representing the newly created event, it includes
	 *         the eventId
	 * 
	 *         This method will create the event in the database through data input.
	 * 
	 *         Check that the posterUrl, name, and eventTime are not empty, if so
	 *         return error Check that the tags are not repeating, if so return an
	 *         error Check that the same event has not been created twice EXISTS
	 *         (SELECT 1 FROM events WHERE uID = userId AND name = name) Attempt to
	 *         create the new event in the table with the given data INSERT INTO
	 *         events (uID, description, eventTime, poster, name, location) VALUES
	 *         (uID, description, eventTime, poster, name, location) AND INSERT INTO
	 *         tags(tag, eID) VALUES(tags. eventID) Transform information into
	 *         JSONObject and return
	 * 
	 *         To return the event, we will have to run a SELECT query with the
	 *         specific event info
	 * 
	 *         Query results will be transformed into JSON with the
	 *         eventJsonTransformer method
	 */
	public JSONObject createEvent(int userId, Timestamp eventTime, String posterUrl, String name, String description,
			String location, List<String> tags) {
		
		if (eventTime == null) {
			throw new IllegalArgumentException("Event Time cannot be left empty");
		} else if (posterUrl == null || posterUrl.isEmpty()) {
			throw new IllegalArgumentException("Poster must be uploaded");
		} else if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Poster Name cannot be left empty");
		}
		
		Statement s = null;

		try {
			s = c.createStatement();
			StringBuilder query = new StringBuilder(
					"INSERT INTO events (owner, eventTime, posterUrl, name, description, location, popularity, status) VALUES (");
			query.append(userId).append(", ").append(eventTime).append(", ").append(posterUrl).append(", ").append(name)
					.append(", ").append(description).append(", ").append(location).append(") RETURNING *;");

			ResultSet rs = s.executeQuery(query.toString());
			int eventId = rs.getInt(rs.getInt("eID"));

			for (String tag : tags) {
				String tagInsert = "INSERT INTO tags (event, tag) VALUES (" + eventId + ", " + tag + ");";
				s.executeUpdate(tagInsert);
			}

			s.close();
			return eventJsonTransformer(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param eventId: the id of the event we want to retrieve information from
	 * @return event: A JSON object with all the information of the event retrieved
	 * 
	 *         This method will obtain all the event information stored in the
	 *         events table of the database.
	 * 
	 *         Run a query to retrieve the event from the database SELECT eID,
	 *         description, eventTime, posterUrl, name, location, tag FROM events
	 *         INNER JOIN tags ON events.eID = tags.eID WHERE events.eID = eventId
	 *         Transform information into JSONObject and return, if the query is
	 *         empty simply return an error message
	 * 
	 *         Query results will be transformed into JSON with the
	 *         eventJsonTransformer method
	 */
	public JSONObject retrieveEvent(int eventId) {
		Statement s = null;

		try {
			s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM events WHERE eID = " + eventId + ";");
			s.close();

			return eventJsonTransformer(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param eventId: the id of the event we want to delete
	 * @param userId: the id of the user who intends to delete the event
	 * @return nothing
	 * 
	 *         This method will delete an event from the database given that the
	 *         user trying to perform the action is the event owner
	 * 
	 *         Check that the eventId and userId combination exists in the database:
	 *         EXISTS (SELECT 1 FROM events WHERE eID = eventID AND uID = userId) If
	 *         false, throw an error. If true, run a query to remove the event from
	 *         the database DELETE FROM events WHERE eID = eventId AND uID = userId
	 */
	public void deleteEvent(int eventId, int userId) {
		Statement s = null;

		try {
			s = c.createStatement();
			s.executeUpdate("DELETE FROM events WHERE eID = " + eventId + " AND owner = " + userId);
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param eventId: the id of the event to be updated
	 * @param userId: the user id of the user trying to update the event
	 * @param description: the new description for the event
	 * @param eventTime: the new time for the event
	 * @param name: the new name for the event
	 * @param location: the new location for the event
	 * @return user: A JSON object representing the newly updated event
	 * 
	 *         This method will update all the fields in the database for a specific
	 *         event given that the owner is the one that prompted the update.
	 * 
	 *         Check that the posterUrl, name, and eventTime are not empty, if so
	 *         return error Check that the tags are not repeating, if so return an
	 *         error Check that the event exists in the database EXISTS (SELECT 1
	 *         FROM events WHERE uID = userId AND name = name) If false, throw an
	 *         error message If true, run the query: UPDATE events SET description =
	 *         description, eventTime = eventTime, .... WHERE eID = eventId AND uID
	 *         = userId Transform information into JSONObject and return
	 * 
	 *         To return the event, we will have to run a SELECT query with the
	 *         specific event info
	 * 
	 *         Query results will be transformed into JSON with the
	 *         eventJsonTransformer method
	 */
	public JSONObject updateEvent(int eventId, int userId, String description, Timestamp eventTime, String name,
			String location, List<String> tags) {

		try {
			return eventJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fromTime: the date time to be used as a reference for inactivity
	 * @param toTime: the date time to be used as a reference for the future
	 * @param status: the new status for the events
	 * @return nothing
	 * 
	 *         This method will update all events that happened in the time interval
	 *         to the specific status
	 * 
	 *         Run this query: UPDATE events SET status = 'status' WHERE eventTime
	 *         >= fromTime AND eventTime <= toTime
	 */
	public void updateEventStatus(String status, Timestamp fromTime, Timestamp toTime) {

	}

	/**
	 * @param eventId: the id of the event we want to retrieve information from
	 * @return tags: A JSON object with all the tags retrieved
	 * 
	 *         This method will obtain all the tags related to a specific event
	 * 
	 *         Run a query to retrieve the tags from the database SELECT DISTINCT
	 *         tag FROM tags Transform information into JSONObject and return
	 * 
	 *         JSON will have the form of { "tags" : ["tag1", "tag2"]}
	 */
	@SuppressWarnings("unchecked")
	public JSONObject retrieveAllTags() {
		Statement s = null;

		try {
			s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT DISTINCT tag FROM tags");
			s.close();

			JSONObject tags = new JSONObject();
			JSONArray tagList = new JSONArray();

			while (rs.next()) {
				tagList.add(rs.getString("tag"));
			}

			tags.put("tags", tagList);

			return tags;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param tags: list of tags to lookup by
	 * @param status: status of the events in question
	 * @return events: JSONObject of the list of events returned
	 * 
	 *         This method will retrieve some basic information of all the events
	 *         that have the set tags, we can also request what status they will
	 *         have. If no status is given, all the events with the set tags will be
	 *         returned.
	 * 
	 *         Run the query: SELECT eID, uID, poster, name FROM events WHERE eID =
	 *         (SELECT eID FROM tags WHERE tag IN tags) AND status = status
	 *         Transform information into JSONObject and return
	 * 
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByTag(List<String> tags, String status) {
		Statement s = null;

		try {
			s = c.createStatement();
			StringBuilder query = new StringBuilder("SELECT eID, uID, poster, name tag FROM tags ");

			ResultSet rs = s.executeQuery(query.toString());
			s.close();

			return eventListJsonTransformer(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param userId: the id of the user who owns the posters
	 * @param status: status of the events in question
	 * @return events: JSONObject of the list of events returned
	 * 
	 *         This method will retrieve some basic information of all the events
	 *         that are owned by the passed user with a certain status. If no status
	 *         is given, all the events from the user will be returned.
	 * 
	 *         Check that the owner exists in the table: EXISTS (SELECT 1 FROM users
	 *         WHERE uID = userId) Run the query: SELECT eID, uID, poster, name FROM
	 *         events WHERE uID = userId AND status = status Transform information
	 *         into JSONObject and return
	 * 
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByOwner(int userId, String status) {

		try {
			return eventListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param search: the search by the user interested in the event
	 * @param status: status of the events in question
	 * @return events: JSONObject of the list of events returned
	 * 
	 *         This method will retrieve some basic information of all the events
	 *         that contain the searched term in the name with a certain status. If
	 *         no status is passed, all the events that fit the search pill be
	 *         returned.
	 * 
	 *         Run the query: SELECT eID, uID, poster, name FROM events WHERE search
	 *         ~ name AND status = status Transform information into JSONObject and
	 *         return
	 * 
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByName(String search, String status) {

		try {
			return eventListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fromTime: the earliest time for the events in question
	 * @param toTime: the latest time for the events in question
	 * @return events: JSONObject of the list of events returned
	 * 
	 *         This method will retrieve some basic information of all the events
	 *         that happen in between the given times
	 * 
	 *         Run the query: SELECT eID, uID, poster, name FROM events WHERE
	 *         eventTime >= fromTime AND eventTime <= toTime Transform information
	 *         into JSONObject and return
	 * 
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByTime(Timestamp fromTime, Timestamp toTime) {

		try {
			return eventListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param followerId: the ID of the user who wants to follow another
	 * @param followedId: the ID of the user who will be followed by another
	 * @return nothing
	 * 
	 *         This method permits the subcsription of one user to another by adding
	 *         a row to the subscriptions table.
	 * 
	 *         Check that the followed person exists: EXISTS (SELECT 1 FROM users
	 *         WHERE uID = followedId) If not, error out. Check that the specific
	 *         entry does not already exist, EXISTS (SELECT 1 FROM subscriptions
	 *         WHERE followerID = followerId AND followedID = followedId) If it
	 *         does, return an error. Otherwise, run the query INSERT INTO
	 *         subscriptions (followerID, followedID) VALUES (followerId,
	 *         followedId) Return a confirmation message
	 */
	public void subscribeTo(int followerId, int followedId) {
	}

	/**
	 * @param followerId: the ID of the user who wants to unfollow another
	 * @param followedId: the ID of the user who will lose the follower
	 * @return nothing
	 * 
	 *         This method permits the un-subcsription of one user to another by
	 *         deleting a row from the subscriptions table.
	 * 
	 *         Check that the specific entry exists, EXISTS (SELECT 1 FROM
	 *         subscriptions WHERE followerID = followerId AND followedID =
	 *         followedId) If it doesn't, throw an error. If it does run the query
	 *         DELETE FROM subscriptions WHERE followerID= followerId AND followedID
	 *         = followedId Return a confirmation message
	 */
	public void unsubscribeTo(int followerId, int followedId) {
	}

	/**
	 * @param userId: the ID of the user who wants to get their subscriptions
	 * @return users: JSONObject containing the list of users
	 * 
	 *         This method retrieves some basic information from all the users the
	 *         current user is subscribed to.
	 * 
	 *         Run the query SELECT uID, firstName, lastName FROM users WHERE uID =
	 *         (SELECT followedID FROM subscriptions WHERE followerID= userId)
	 *         Transform information into JSONObject and return.
	 * 
	 *         Creating the JSON will be done through the userListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveSubscriptions(int userId) {

		try {
			return userListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param userId: the ID of the user who wants to get their subscribers
	 * @return users: JSONObject containing the list of users
	 * 
	 *         This method retrieves some basic information from all the users that
	 *         are subscribed to the current user.
	 * 
	 *         Run the query SELECT uID, firstName, lastName FROM users WHERE uID =
	 *         (SELECT followerID FROM subscriptions WHERE followedID = userId)
	 *         Transform information into JSONObject and return
	 * 
	 *         Creating the JSON will be done through the userListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveSubscribers(int userId) {

		try {
			return userListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param email: the email of the user who will rsvp
	 * @param name: the name of the user who will rsvp
	 * @param eventId: the id of the event to be rsvpd to
	 * @param time: the current date time
	 * @return nothing
	 * 
	 *         This method permits RSVP to one event by adding a row to the rsvps
	 *         table.
	 * 
	 *         Check that the email is not empty, if so error out. Check that the
	 *         email isn't already rsvpd to that event, EXISTS (SELECT 1 FROM rsvps
	 *         WHERE email = email AND eID = eventID) If it does, return an error.
	 *         Otherwise, run the query INSERT INTO rsvps (email, name, eID, time)
	 *         VALUES (email, name, eventId, time) Return a confirmation message
	 */
	public void rsvpTo(String email, String name, int eventId, Timestamp time) {
	}

	/**
	 * @param email: the email of the user who will un-rsvp
	 * @param eventId: the id of the event to be un-rsvpd to
	 * @return nothing
	 * 
	 *         This method permits un-RSVP to one event by removing a row to the
	 *         rsvps table.
	 * 
	 *         Check that the email is not empty, if so error out. Check that the
	 *         email is already rsvpd to that event, EXISTS (SELECT 1 FROM rsvps
	 *         WHERE email = email AND eID = eventID) If it isn't, return an error.
	 *         Otherwise, run the query DELETE FROM rsvps WHERE email = email AND
	 *         eID= eventId Return a confirmation message
	 */
	public void unRsvpFrom(String email, int eventId) {
	}

	/**
	 * @param userId: the id of the user whose events we want to retrieve
	 * @param status: status of the events in question
	 * @return events: JSON object containing all the queried events
	 * 
	 *         This method retireves all the events RSVPd to by a user depending on
	 *         the passed status. If the status is not passed then all RSVPs (past
	 *         and present) will be returned.
	 * 
	 *         Run the query SELECT eID, uID, name, poster FROM events WHERE eID =
	 *         (SELECT eID FROM rsvps WHERE uID = userId) AND status = status
	 *         Transform information into JSONObject and return
	 * 
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveRsvpdEvents(String email, String status) {

		try {
			return eventListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param eventId: the id of the event to get attendees from
	 * @return users: JSON object containing all the queried users
	 * 
	 *         This method retrieves all the users who RSVPd to an event.
	 * 
	 *         Run the query SELECT uID, firstName, lastName FROM users WHERE uID =
	 *         (SELECT uID FROM rsvps WHERE eID = eventId) Transform information
	 *         into JSONObject and return
	 * 
	 *         Creating the JSON will be done through the userListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveAttendees(int eventId) {

		try {
			return userListJsonTransformer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param rs: the result of the query ran
	 * @return user: the user resulting from the specified query
	 * @throws SQLException
	 * 
	 *                      Transforms a query result from the users database into a
	 *                      user json object
	 * 
	 *                      JSON will have the form of {"uID": uID, "firstName":
	 *                      firstName, "lastName": lastName, ... }
	 */
	@SuppressWarnings("unchecked")
	private JSONObject userJsonTransformer(ResultSet rs) throws SQLException {
		JSONObject user = new JSONObject();
		user.put("uID", rs.getInt("uID"));
		user.put("firstName", rs.getString("firstName"));
		user.put("lastName", rs.getString("firstName"));
		user.put("email", rs.getString("email"));
		user.put("campus", rs.getString("campus"));
		
		rs.close();
		return user;
	}

	/**
	 * @param rs: the result of the query ran
	 * @return event: the event resulting from the specified query
	 * 
	 *         Transforms a query result from the events database into an event json
	 *         object
	 * 
	 *         JSON will have the form of {"eID": 1, "owner": 1, "description":
	 *         "things happen", ... }
	 */
	@SuppressWarnings("unchecked")
	private JSONObject eventJsonTransformer(ResultSet rs) throws SQLException {
		JSONObject event = new JSONObject();
		event.put("eID", rs.getInt("eID"));
		event.put("owner", rs.getInt("owner"));
		event.put("eventTime", rs.getTimestamp("eventTime"));
		event.put("posterUrl", rs.getString("posterUrl"));
		event.put("name", rs.getString("name"));
		event.put("description", rs.getString("description"));
		event.put("location", rs.getString("location"));
		event.put("popularity", rs.getInt("popularity"));
		event.put("status", rs.getString("status"));
		
		rs.close();
		return event;
	}

	/**
	 * @param rs: the result of the query ran
	 * @return users: the users resulting from the specified query
	 * 
	 *         Transforms a query of multiple users into a json array object
	 * 
	 *         JSON will have the form of: { "users": [ {"uID": 1, "firstName":
	 *         "John", "lastName": "Doe" }, {"uID": 2, "firstName": "Jill",
	 *         "lastName": "Swan" }, ... ]}
	 */
	@SuppressWarnings("unchecked")
	private JSONObject userListJsonTransformer(ResultSet rs) throws SQLException {
		JSONObject users = new JSONObject();
		JSONArray userList = new JSONArray();

		while (rs.next()) {
			JSONObject user = new JSONObject();
			user.put("uID", rs.getInt("uID"));
			user.put("firstName", rs.getString("firstName"));
			user.put("lastName", rs.getString("firstName"));

			userList.add(user);
		}

		users.put("users", userList);
		rs.close();
		return users;
	}

	/**
	 * @param rs: the result of the query ran
	 * @return events: the events resulting from the specified query
	 * 
	 *         Transforms a query of multiple events into a json array object
	 * 
	 * 
	 *         JSON will have the form of: { "events": [ {"eID": eID, "owner": uID,
	 *         "name": name, ... }, {"eID": eID, "owner": uID, "name": name, ... },
	 *         ... ]}
	 */
	@SuppressWarnings("unchecked")
	private JSONObject eventListJsonTransformer(ResultSet rs) throws SQLException {
		JSONObject events = new JSONObject();
		JSONArray eventList = new JSONArray();

		while (rs.next()) {
			JSONObject event = new JSONObject();
			event.put("eID", rs.getInt("eID"));
			event.put("owner", rs.getInt("owner"));
			event.put("name", rs.getString("name"));
			event.put("posterUrl", rs.getString("posterUrl"));

			eventList.add(event);
		}

		events.put("events", eventList);
		rs.close();
		return events;
	}
}
