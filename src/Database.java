import java.util.*;

public class Database {
	
	// set these to be connections to database
	private static String db;
	private static String user;
	private static String pwd;
	
	
	// add to the Users table in the database, throw an exception if username exists
	public static void createUser(String username, String password)
	{
		
	}
	
	// return true if password matches username, false if invalid user-password combination
	public static boolean attemptLogin(String username, String password)
	{
	
	}
	
	// add to the Scores table in the database
	public static void addScore(String username, int score)
	{
		
	}
	
	// return top 10 scores in database (or every score if there are less than 10)
	public static ArrayList<Score> getHighScores()
	{
		
	}
}
	

