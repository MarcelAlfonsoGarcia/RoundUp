package com.roundup.roundupAPI.database;

import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class StressTest {

	private static int userOne;
	private static int userTwo;
	private static int userThree;
	private static int userFour;
	private static int userFive;
	private static int userSix;
	private static int userSeven;
	private static int userEight;
	private static int userNine;
	private static int userTen;
	private static int userEleven;
	private static int userTwelve;
	private static int userThirteen;
	private static int userFourteen;
	private static int userFifteen;
	private static int userSixteen;
	private static int userSeventeen;
	private static int userEighteen;
	private static int userNineteen;
	private static int userTwenty;

	private static DAL dalOne;
	private static DAL dalTwo;
	private static DAL dalThree;
	private static DAL dalFour;
	private static DAL dalFive;
	private static DAL dalSix;
	private static DAL dalSeven;
	private static DAL dalEight;
	private static DAL dalNine;
	private static DAL dalTen;

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		dalOne = DAL.getInstance();
		dalTwo = DAL.getInstance();
		dalThree = DAL.getInstance();
		dalFour = DAL.getInstance();
		dalFive = DAL.getInstance();
		dalSix = DAL.getInstance();
		dalSeven = DAL.getInstance();
		dalEight = DAL.getInstance();
		dalNine = DAL.getInstance();
		dalTen = DAL.getInstance();
	}
	
	@Order(1)
	@Test
	public void testMultipleUserCreations() {
		Long start = System.currentTimeMillis();

		JSONObject userOneDb = dalOne.createUser("Harold", "Kumar", "hk1@email.com", "pass1", "WHC");
		userOne = (int) userOneDb.get("uID");

		JSONObject userTwoDb = dalTwo.createUser("Harold", "Kumar", "hk2@email.com", "pass1", "WHC");
		userTwo = (int) userTwoDb.get("uID");

		JSONObject userThreeDb = dalThree.createUser("Harold", "Kumar", "hk3@email.com", "pass1", "WHC");
		userThree = (int) userThreeDb.get("uID");

		JSONObject userFourDb = dalFour.createUser("Harold", "Kumar", "hk4@email.com", "pass1", "WHC");
		userFour = (int) userFourDb.get("uID");

		JSONObject userFiveDb = dalFive.createUser("Harold", "Kumar", "hk5@email.com", "pass1", "WHC");
		userFive = (int) userFiveDb.get("uID");

		JSONObject userFixDb = dalSix.createUser("Harold", "Kumar", "hk6@email.com", "pass1", "WHC");
		userSix = (int) userFixDb.get("uID");

		JSONObject userSevenDb = dalSeven.createUser("Harold", "Kumar", "hk7@email.com", "pass1", "WHC");
		userSeven = (int) userSevenDb.get("uID");

		JSONObject userEightDb = dalEight.createUser("Harold", "Kumar", "hk8@email.com", "pass1", "WHC");
		userEight = (int) userEightDb.get("uID");

		JSONObject userNineDb = dalNine.createUser("Harold", "Kumar", "hk9@email.com", "pass1", "WHC");
		userNine = (int) userNineDb.get("uID");

		JSONObject userTenDb = dalTen.createUser("Harold", "Kumar", "hk10@email.com", "pass1", "WHC");
		userTen = (int) userTenDb.get("uID");

		JSONObject userElevenDb = dalOne.createUser("Harold", "Kumar", "hk11@email.com", "pass1", "WHC");
		userEleven = (int) userElevenDb.get("uID");

		JSONObject userTwelveDb = dalTwo.createUser("Harold", "Kumar", "hk12@email.com", "pass1", "WHC");
		userTwelve = (int) userTwelveDb.get("uID");

		JSONObject userThirteenDb = dalThree.createUser("Harold", "Kumar", "hk13@email.com", "pass1", "WHC");
		userThirteen = (int) userThirteenDb.get("uID");
		
		JSONObject userFourteenDb = dalFour.createUser("Harold", "Kumar", "hk14@email.com", "pass1", "WHC");
		userFourteen = (int) userFourteenDb.get("uID");
		
		JSONObject userFifteenDb = dalFive.createUser("Harold", "Kumar", "hk15@email.com", "pass1", "WHC");
		userFifteen = (int) userFifteenDb.get("uID");
		
		JSONObject userSixteenDb = dalSix.createUser("Harold", "Kumar", "hk16@email.com", "pass1", "WHC");
		userSixteen = (int) userSixteenDb.get("uID");
		
		JSONObject userSeventeenDb = dalSeven.createUser("Harold", "Kumar", "hk17@email.com", "pass1", "WHC");
		userSeventeen = (int) userSeventeenDb.get("uID");
		
		JSONObject userEighteenDb = dalEight.createUser("Harold", "Kumar", "hk18@email.com", "pass1", "WHC");
		userEighteen = (int) userEighteenDb.get("uID");
		
		JSONObject userNineteenDb = dalNine.createUser("Harold", "Kumar", "hk19@email.com", "pass1", "WHC");
		userNineteen = (int) userNineteenDb.get("uID");
		
		JSONObject userTwentyDb = dalTen.createUser("Harold", "Kumar", "hk20@email.com", "pass1", "WHC");
		userTwenty = (int) userTwentyDb.get("uID");
		
		Long end = System.currentTimeMillis();
		
		assertTrue(start - end < 60000);
	}
	
	@Order(2)
	@Test
	public void testMultipleUserRetrievals() {
		Long start = System.currentTimeMillis();
				
		JSONObject userOneDb = dalOne.retrieveUser(userOne);
		JSONObject userTwoDb = dalTwo.retrieveUser(userTwo);
		JSONObject userThreeDb = dalThree.retrieveUser(userThree);
		JSONObject userFourDb = dalFour.retrieveUser(userFour);
		JSONObject userFiveDb = dalFive.retrieveUser(userFive);
		JSONObject userFixDb = dalSix.retrieveUser(userSix);
		JSONObject userSevenDb = dalSeven.retrieveUser(userSeven);
		JSONObject userEightDb = dalEight.retrieveUser(userEight);
		JSONObject userNineDb = dalNine.retrieveUser(userNine);
		JSONObject userTenDb = dalTen.retrieveUser(userTen);
		JSONObject userElevenDb = dalOne.retrieveUser(userEleven);
		JSONObject userTwelveDb = dalTwo.retrieveUser(userTwelve);
		JSONObject userThirteenDb = dalThree.retrieveUser(userThirteen);
		JSONObject userFourteenDb = dalFour.retrieveUser(userFourteen);
		JSONObject userFifteenDb = dalFive.retrieveUser(userFifteen);
		JSONObject userSixteenDb = dalSix.retrieveUser(userSixteen);		
		JSONObject userSeventeenDb = dalSeven.retrieveUser(userSeventeen);		
		JSONObject userEighteenDb = dalEight.retrieveUser(userEighteen);		
		JSONObject userNineteenDb = dalNine.retrieveUser(userNineteen);		
		JSONObject userTwentyDb = dalTen.retrieveUser(userTwenty);
		
		Long end = System.currentTimeMillis();
		
		assertTrue(start - end < 60000);
	}
	
	@Order(3)
	@Test
	public void testMultipleUserDeletions() {
		Long start = System.currentTimeMillis();
		
		dalOne.deleteUser(userTwenty);
		dalOne.deleteUser(userNineteen);
		dalTwo.deleteUser(userEighteen);
		dalTwo.deleteUser(userSeventeen);
		dalThree.deleteUser(userSixteen);
		dalThree.deleteUser(userFifteen);
		dalFour.deleteUser(userFourteen);
		dalFour.deleteUser(userThirteen);
		dalFive.deleteUser(userTwelve);
		dalFive.deleteUser(userEleven);
		dalSix.deleteUser(userTen);
		dalSix.deleteUser(userNine);
		dalSeven.deleteUser(userEight);
		dalSeven.deleteUser(userSeven);
		dalEight.deleteUser(userSix);
		dalEight.deleteUser(userFive);
		dalNine.deleteUser(userFour);
		dalNine.deleteUser(userThree);
		dalTen.deleteUser(userTwo);
		dalTen.deleteUser(userOne);
		
		Long end = System.currentTimeMillis();
		
		assertTrue(start - end < 60000);
	}
}
