/**
	DAL = Data Access Layer
	This module will serve as the API meant for connecting and querying the database.
	It will follow a Singleton design pattern that exposes its methods to other classes.
	Each public method will describe a query that will be customizable through the
	passed parameters.
	
	@author Marcel Alfonso
*/
package com.roundup.roundupAPI.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class DAL {

	// the singleton instance of the DAL object
	private static DAL instance;
	private static Connection c;

//	@Value("${spring.datasource.url}")
//	private String dbUrl;

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
		try {
			String dbUrl =  "jdbc:postgresql://ec2-54-161-239-198.compute-1.amazonaws.com:5432/d57evff6a32s3o?password=2810f0e0743eb39b9b84189023ffbd36f43f7156827da2ba984fca64633236be&sslmode=require&user=kproipogvexbbm";
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(dbUrl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("Could not establish a connection to the database: " + e.getMessage());
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
	 * @param lastName:  the last name of the user to be created
	 * @param email:     the email of the user to be created
	 * @param password:  the password of the user to be created
	 * @param campus:    the campus of the user to be created
	 * @return user: A JSON object representing the newly created user, it includes
	 *         the userId
	 * 
	 *         This method will create the user in the database through data input.
	 * 
	 *         Because data safety is not our main concern in this phase of the
	 *         project, we will simply be storing passwords as strings and checking
	 *         them against each other,
	 */
	public JSONObject createUser(String firstName, String lastName, String email, String password, String campus) {

		if (firstName == null || firstName.isEmpty()) {
			throw new IllegalArgumentException("First Name cannot be left empty");
		} else if (lastName == null || lastName.isEmpty()) {
			throw new IllegalArgumentException("Last Name cannot be left empty");
		} else if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email cannot be left empty");
		} else if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Password cannot be left empty");
		}

		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder(
					"INSERT INTO users (firstName, lastName, email, password, campus) VALUES (");
			query.append("'" + firstName + "'").append(", ").append("'" + lastName + "'").append(", ")
					.append("'" + email + "'").append(", ").append("'" + password + "'").append(", ")
					.append("'" + campus + "'").append(") RETURNING *;");

			return userJsonTransformer(s.executeQuery(query.toString()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot create user in the database: " + e.getMessage());
		}
	}

	/**
	 * @param email:    the email of the user we want to retrieve information from
	 * @param password: the password of the user we want to retrieve information
	 *                  from
	 * @return user: A JSON object with all the information of the user retrieved
	 * 
	 *         This method will obtain all the user information stored in the user
	 *         table of the database.
	 * 
	 *         Query results will be transformed into JSON with the
	 *         userJsonTransformer method
	 */
	public JSONObject logInUser(String email, String password) {

		try (Statement s = c.createStatement()) {
			ResultSet userResult = s.executeQuery(
					"SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "';");

			return userJsonTransformer(userResult);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not retrieve user from the database: " + e.getMessage());
		}
	}

	/**
	 * @param userId: the id of the user we want to retrieve information from
	 * @return user: A JSON object with all the information of the user retrieved
	 * 
	 *         This method will obtain all the user information stored in the user
	 *         table of the database.
	 * 
	 *         Query results will be transformed into JSON with the
	 *         userJsonTransformer method
	 */
	public JSONObject retrieveUser(int userId) {
		try (Statement s = c.createStatement()) {
			ResultSet userResult = s.executeQuery("SELECT * FROM users WHERE uID = " + userId + ";");

			return userJsonTransformer(userResult);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not retrieve user from the database: " + e.getMessage());
		}
	}

	/**
	 * @param userId: the id of the user we want to delete
	 * @return nothing
	 * 
	 *         This method will delete a user from the database
	 */
	public void deleteUser(int userId) {
		try (Statement s = c.createStatement()) {
			String check = "SELECT * FROM users WHERE uID = " + userId + ";";
			ResultSet checkResult = s.executeQuery(check);

			if (!checkResult.next()) {
				throw new NullPointerException("No user exists under the provided information");
			}

			String events = "SELECT eID FROM events WHERE owner = " + userId + ";";
			ResultSet eventsResult = s.executeQuery(events);
			while (eventsResult.next()) {
				this.deleteEvent(eventsResult.getInt("eID"), userId);
			}

			s.executeUpdate("DELETE FROM subscribes WHERE followerID = " + userId + " OR followedID = " + userId + ";");
			s.executeUpdate("DELETE FROM users WHERE uID = " + userId + ";");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not delete user from the datbase: " + e.getMessage());
		}
	}

	/**
	 * @param userId:    the user id of the user to be updated
	 * @param firstName: the first name of the user to be updated
	 * @param lastName:  the last name of the user to be updated
	 * @param email:     the email of the user to be updated
	 * @param campus:    the campus of the user to be updated
	 * @param cohort:    the cohort of the user to be updated
	 * @return user: A JSON object representing the newly updated user
	 * 
	 *         This method will update all the fields in the database for a specific
	 *         user.
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

		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("UPDATE users SET ");
			query.append("firstName = '" + firstName).append("', ").append("lastName = '" + lastName).append("', ")
					.append("email = '" + email).append("', ").append("campus = '" + campus);
			query.append("' WHERE uID =" + userId);
			query.append(" RETURNING *;");

			ResultSet rs = s.executeQuery(query.toString());

			return userJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not update user in the database: " + e.getMessage());
		}
	}

	/**
	 * @param userId:      the ID of the user who is creating the event
	 * @param description: the description of the event to be created
	 * @param eventTime:   the time the event will take place
	 * @param posterUrl:   the url for the poster corresponding to this event
	 * @param name:        the name of the event given by the user
	 * @param location:    the location of the event as given by the user
	 * @return user: A JSON object representing the newly created event, it includes
	 *         the eventId
	 * 
	 *         This method will create the event in the database through data input.
	 * 
	 *         Query results will be transformed into JSON with the
	 *         eventJsonTransformer method
	 */
	public JSONObject createEvent(int userId, Timestamp eventTime, String posterUrl, String name, String description,
			String location, Set<String> tags) {

		if (eventTime == null) {
			throw new IllegalArgumentException("Event Time cannot be left empty");
		} else if (posterUrl == null || posterUrl.isEmpty()) {
			throw new IllegalArgumentException("Poster must be uploaded");
		} else if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Event Name cannot be left empty");
		}

		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder(
					"INSERT INTO events (owner, eventTime, posterUrl, name, description, location, popularity, status) VALUES (");
			query.append(userId).append(", '").append(eventTime).append("', '").append(posterUrl).append("', '")
					.append(name).append("', '").append(description).append("', '").append(location).append("', ")
					.append(0).append(", ").append("'active'").append(") RETURNING *;");

			ResultSet rs = s.executeQuery(query.toString());

			JSONObject event = eventJsonTransformer(rs);
			int eventId = (int) event.get("eID");

			String tagInsert = "";
			for (String tag : tags) {
				tagInsert = "INSERT INTO tags (event, tag) VALUES (" + eventId + ", '" + tag + "');";
				s.addBatch(tagInsert);
			}
			s.executeBatch();

			String tagsQuery = "SELECT DISTINCT tag FROM tags WHERE event = " + eventId + ";";
			ResultSet tagsRs = s.executeQuery(tagsQuery);
			eventJsonAddTags(event, tagsRs);

			rs.close();
			tagsRs.close();
			return event;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not add event to the database: " + e.getMessage());
		}
	}

	/**
	 * @param eventId: the id of the event we want to retrieve information from
	 * @return event: A JSON object with all the information of the event retrieved
	 * 
	 *         This method will obtain all the event information stored in the
	 *         events table of the database.
	 * 
	 *         Query results will be transformed into JSON with the
	 *         eventJsonTransformer method
	 */
	public JSONObject retrieveEvent(int eventId) {
		try (Statement s = c.createStatement()) {
			String eventQuery = "SELECT * FROM events WHERE eID = " + eventId + ";";
			String tagsQuery = "SELECT DISTINCT tag FROM tags WHERE event = " + eventId + ";";

			ResultSet eventRs = s.executeQuery(eventQuery);
			JSONObject event = eventJsonTransformer(eventRs);

			ResultSet tagsRs = s.executeQuery(tagsQuery);
			eventJsonAddTags(event, tagsRs);
			return event;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not load event from the database: " + e.getMessage());
		}
	}

	/**
	 * @param eventId: the id of the event we want to delete
	 * @param userId:  the id of the user who intends to delete the event
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
		try (Statement s = c.createStatement()) {
			String check = "SELECT * FROM events WHERE eID = " + eventId + ";";
			ResultSet checkResult = s.executeQuery(check);

			if (!checkResult.next()) {
				throw new NullPointerException("No event exists under the provided information");
			}

			s.executeUpdate("DELETE FROM rsvps WHERE event = " + eventId);
			s.executeUpdate("DELETE FROM tags WHERE event = " + eventId);
			s.executeUpdate("DELETE FROM events WHERE eID = " + eventId + " AND owner = " + userId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not delete event from the database: " + e.getMessage());
		}
	}

	/**
	 * @param eventId:     the id of the event to be updated
	 * @param userId:      the user id of the user trying to update the event
	 * @param description: the new description for the event
	 * @param eventTime:   the new time for the event
	 * @param name:        the new name for the event
	 * @param location:    the new location for the event
	 * @return user: A JSON object representing the newly updated event
	 * 
	 *         This method will update all the fields in the database for a specific
	 *         event given that the owner is the one that prompted the update.
	 * 
	 *         Query results will be transformed into JSON with the
	 *         eventJsonTransformer method
	 */
	public JSONObject updateEvent(int eventId, int userId, String description, Timestamp eventTime, String name,
			String location, Set<String> tags) {

		if (eventTime == null) {
			throw new IllegalArgumentException("Event Time cannot be left empty");
		} else if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Event Name cannot be left empty");
		}

		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("UPDATE events SET ");
			query.append("eventTime = '" + eventTime).append("', name = '").append(name).append("', description = '")
					.append(description).append("', location = '").append(location);
			query.append("' WHERE eID =" + eventId).append(" AND owner = " + userId);
			query.append(" RETURNING *;");

			JSONObject event = eventJsonTransformer(s.executeQuery(query.toString()));

			// we cannot update the rows in the table, simply remove them and re-add them
			s.executeUpdate("DELETE FROM tags WHERE event = " + eventId);

			String tagInsert = "";
			for (String tag : tags) {
				tagInsert = "INSERT INTO tags (event, tag) VALUES (" + eventId + ", '" + tag + "');";
				s.addBatch(tagInsert);
			}
			s.executeBatch();

			String tagsQuery = "SELECT DISTINCT tag FROM tags WHERE event = " + eventId + ";";
			eventJsonAddTags(event, s.executeQuery(tagsQuery));
			return event;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not update event in the database: " + e.getMessage());
		}
	}

	/**
	 * @param fromTime: the date time to be used as a reference for inactivity
	 * @param toTime:   the date time to be used as a reference for the future
	 * @param status:   the new status for the events
	 * @return nothing
	 * 
	 *         This method will update all events that happened in the time interval
	 *         to the specific status
	 * 
	 *         Run this query: UPDATE events SET status = 'status' WHERE eventTime
	 *         >= fromTime AND eventTime <= toTime
	 */
	public void updateEventStatus(String status, Timestamp fromTime, Timestamp toTime) {
		try (Statement s = c.createStatement()) {
			String query = "UPDATE events SET status = '" + status + "' WHERE eventTime BETWEEN '" + fromTime
					+ "' AND '" + toTime + "';";
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve events by time from the database: " + e.getMessage());
		}
	}

	/**
	 * @param eventId: the id of the event we want to retrieve information from
	 * @return tags: A JSON object with all the tags retrieved
	 * 
	 *         This method will obtain all the tags related to a specific event
	 * 
	 *         JSON will have the form of { "tags" : ["tag1", "tag2"]}
	 */
	@SuppressWarnings("unchecked")
	public JSONObject retrieveAllTags() {
		try (Statement s = c.createStatement()) {
			ResultSet rs = s.executeQuery("SELECT DISTINCT tag FROM tags");

			JSONObject tags = new JSONObject();
			JSONArray tagList = new JSONArray();

			while (rs.next()) {
				tagList.add(rs.getString("tag"));
			}

			tags.put("tags", tagList);

			return tags;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve the tags from the database: " + e.getMessage());
		}
	}

	/**
	 * @param tags:   list of tags to lookup by
	 * @param status: status of the events in question
	 * @return events: JSONObject of the list of events returned
	 * 
	 *         This method will retrieve some basic information of all the events
	 *         that have the set tags, we can also request what status they will
	 *         have. If no status is given, all the events with the set tags will be
	 *         returned.
	 * 
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByTag(Set<String> tags, String status) {
		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("SELECT eID, owner, posterUrl, name, eventTime, location, description FROM events WHERE eID IN ");
			query.append("(SELECT event FROM tags WHERE tag IN ");

			StringJoiner sj = new StringJoiner(",", "(", ")");
			for (String tag : tags) {
				sj.add("'" + tag + "'");
			}
			query.append(sj.toString()).append(")");

			if (!status.isEmpty()) {
				query.append(" AND status = " + status);
			}

			query.append(";");
			ResultSet rs = s.executeQuery(query.toString());

			return eventListJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve events by tags from the database: " + e.getMessage());
		}
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
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByOwner(int userId, String status) {
		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder(
					"SELECT eID, owner, posterUrl, name, eventTime, location, description FROM events WHERE owner = " + userId);

			if (!status.isEmpty()) {
				query.append(" AND status = " + status);
			}

			query.append(";");
			ResultSet rs = s.executeQuery(query.toString());

			return eventListJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve events by owner from the database: " + e.getMessage());
		}
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
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveEventsByName(String search, String status) {

		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder(
					"SELECT eID, owner, posterUrl, name, eventTime, location, description FROM events WHERE name ILIKE '%" + search + "%'");

			if (!status.isEmpty()) {
				query.append(" AND status = " + status);
			}

			query.append(";");
			ResultSet rs = s.executeQuery(query.toString());

			return eventListJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve events by search from the database: " + e.getMessage());
		}
	}

	/**
	 * @param fromTime: the earliest time for the events in question
	 * @param toTime:   the latest time for the events in question
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

		try (Statement s = c.createStatement()) {
			String query = "SELECT eID, owner, posterUrl, name, eventTime, location, description FROM events WHERE eventTime BETWEEN '" + fromTime
					+ "' AND '" + toTime + "';";
			ResultSet rs = s.executeQuery(query);
			
			return eventListJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve events by time from the database: " + e.getMessage());
		}
	}

	/**
	 * @param followerId: the ID of the user who wants to follow another
	 * @param followedId: the ID of the user who will be followed by another
	 * @return nothing
	 * 
	 *         This method permits the subcsription of one user to another by adding
	 *         a row to the subscribes table.
	 */
	public void subscribeTo(int followerId, int followedId) {
		try (Statement s = c.createStatement()) {
			String query = "INSERT INTO subscribes (followerID, followedID) VALUES (" + followerId + ", " + followedId
					+ ");";
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not subscribe to user in database: " + e.getMessage());
		}
	}

	/**
	 * @param followerId: the ID of the user who wants to unfollow another
	 * @param followedId: the ID of the user who will lose the follower
	 * @return nothing
	 * 
	 *         This method permits the un-subcsription of one user to another by
	 *         deleting a row from the subscribes table.
	 */
	public void unsubscribeFrom(int followerId, int followedId) {
		try (Statement s = c.createStatement()) {
			String check = "SELECT * FROM subscribes WHERE followerID = " + followerId + " AND followedID = "
					+ followedId + ";";
			ResultSet checkResult = s.executeQuery(check);

			if (!checkResult.next()) {
				throw new NullPointerException("Cannot unsubscribe from unknown relationship");
			}

			String query = "DELETE FROM subscribes WHERE followerID = " + followerId + " AND followedID = " + followedId
					+ ";";
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not unssubscribe from user in database: " + e.getMessage());
		}
	}

	/**
	 * @param userId: the ID of the user who wants to get their subscribes
	 * @return users: JSONObject containing the list of users
	 * 
	 *         This method retrieves some basic information from all the users the
	 *         current user is subscribed to.
	 * 
	 *         Creating the JSON will be done through the userListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveSubscriptions(int userId) {
		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("SELECT uID, firstName, lastName FROM users WHERE uID IN ");
			query.append("(SELECT followedID FROM subscribes WHERE followerID = ").append(userId).append(");");

			return userListJsonTransformer(s.executeQuery(query.toString()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve subscriptions from user in database: " + e.getMessage());
		}
	}

	/**
	 * @param userId: the ID of the user who wants to get their subscribers
	 * @return users: JSONObject containing the list of users
	 * 
	 *         This method retrieves some basic information from all the users that
	 *         are subscribed to the current user.
	 * 
	 *         Creating the JSON will be done through the userListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveSubscribers(int userId) {
		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("SELECT uID, firstName, lastName FROM users WHERE uID IN ");
			query.append("(SELECT followerID FROM subscribes WHERE followedID = ").append(userId).append(");");

			return userListJsonTransformer(s.executeQuery(query.toString()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve subscriptions from user in database: " + e.getMessage());
		}
	}

	/**
	 * @param email:   the email of the user who will rsvp
	 * @param name:    the name of the user who will rsvp
	 * @param eventId: the id of the event to be rsvpd to
	 * @param time:    the current date time
	 * @return nothing
	 * 
	 *         This method permits RSVP to one event by adding a row to the rsvps
	 *         table.
	 */
	public void rsvpTo(String email, String name, int eventId, Timestamp time) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email cannot be left empty");
		}

		try (Statement s = c.createStatement()) {
			String query = "INSERT INTO rsvps (email, name, event, signupTime) VALUES ('" + email + "', '" + name
					+ "', " + eventId + ", '" + time + "');";

			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not rsvp to event: " + e.getMessage());
		}
	}

	/**
	 * @param email:   the email of the user who will un-rsvp
	 * @param eventId: the id of the event to be un-rsvpd to
	 * @return nothing
	 * 
	 *         This method permits un-RSVP to one event by removing a row to the
	 *         rsvps table.
	 */
	public void unRsvpFrom(String email, int eventId) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email cannot be left empty");
		}

		try (Statement s = c.createStatement()) {

			String check = "SELECT * FROM rsvps WHERE email = '" + email + "' AND event = " + eventId + ";";
			ResultSet checkResult = s.executeQuery(check);

			if (!checkResult.next()) {
				throw new NullPointerException("User is not RSVPd to this event");
			}

			String query = "DELETE FROM rsvps WHERE email = '" + email + "' AND event = " + eventId + ";";
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not rsvp to event: " + e.getMessage());
		}
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
	 *         Creating the JSON will be done through the eventListJsonTransformer
	 *         method
	 */
	public JSONObject retrieveRsvpdEvents(String email, String status) {

		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("SELECT eID, owner, posterUrl, name FROM events WHERE eID IN ");
			query.append("(SELECT event FROM rsvps WHERE email = '" + email + "')");
			if (!status.isEmpty()) {
				query.append(" AND status = " + status);
			}

			query.append(";");
			ResultSet rs = s.executeQuery(query.toString());

			return eventListJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve RSVPd events: " + e.getMessage());
		}
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
		try (Statement s = c.createStatement()) {
			StringBuilder query = new StringBuilder("SELECT uID, firstName, lastName FROM users WHERE email IN ");
			query.append("(SELECT email FROM rsvps WHERE event = " + eventId + ");");
			ResultSet rs = s.executeQuery(query.toString());

			return userListJsonTransformer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("Could not retrieve RSVPd events: " + e.getMessage());
		}
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

		if (rs.next()) {
			user.put("uID", rs.getInt("uID"));
			user.put("firstName", rs.getString("firstName"));
			user.put("lastName", rs.getString("lastName"));
			user.put("email", rs.getString("email"));
			user.put("campus", rs.getString("campus"));
		} else {
			rs.close();
			throw new NullPointerException("User was not found in the database");
		}

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
	private JSONObject eventJsonTransformer(ResultSet eventRs) throws SQLException {
		if (eventRs.next()) {
			JSONObject event = new JSONObject();

			event.put("eID", eventRs.getInt("eID"));
			event.put("owner", eventRs.getInt("owner"));
			event.put("eventTime", eventRs.getTimestamp("eventTime"));
			event.put("posterUrl", eventRs.getString("posterUrl"));
			event.put("name", eventRs.getString("name"));
			event.put("description", eventRs.getString("description"));
			event.put("location", eventRs.getString("location"));
			event.put("popularity", eventRs.getInt("popularity"));
			event.put("status", eventRs.getString("status"));

			eventRs.close();
			return event;
		} else {
			eventRs.close();
			throw new NullPointerException("Event was not found in the database");
		}
	}

	@SuppressWarnings("unchecked")
	private void eventJsonAddTags(JSONObject event, ResultSet tagsRs) throws SQLException {
		JSONArray tags = new JSONArray();
		while (tagsRs.next()) {
			tags.add(tagsRs.getString("tag"));
		}
		event.put("tags", tags);
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
			user.put("lastName", rs.getString("lastName"));

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
			event.put("eventTime", rs.getTimestamp("eventTime"));
			event.put("description", rs.getString("description"));
			event.put("location", rs.getString("location"));
			event.put("posterUrl", rs.getString("posterUrl"));
			

			eventList.add(event);
		}

		events.put("events", eventList);
		rs.close();
		return events;
	}
}