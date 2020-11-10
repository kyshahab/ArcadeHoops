import java.util.*;
import java.sql.*;

public class Database {
	
	// set these to be connections to database
	private static String db;
	private static String user;
	private static String pwd;
	
	public static void establishConnection() {
		db = "jdbc:mysql://localhost:3306/arcade_hoops?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Los_Angeles";
		user = "root";
		pass = "Artin2020";
		try (Connection conn = DriverManager.getConnection(db, user, pass);
	}		
	
	//returns false if username already exists	
	public static boolean checkUsername(name) {
		establishConnection();
		String queryCheck = "SELECT count(*) from users WHERE username = ?";
		PreparedStatement ps = conn.prepareStatement(queryCheck);
		ps.setString(1, name);
		ResultSet resultSet = ps.executeQuery();
		int count = 0;
		if(resultSet.next()) {
		    count = resultSet.getInt(1);
		}
		if (count>0) {
			return false;
		}
		return true;
	}
	public static boolean checkPassword(pass) {
		establishConnection();
		String queryCheck = "SELECT count(*) from users WHERE password = ?";
		PreparedStatement ps = conn.prepareStatement(queryCheck);
		ps.setString(1, pass);
		ResultSet resultSet = ps.executeQuery();
		int count = 0;
		if(resultSet.next()) {
		    count = resultSet.getInt(1);
		}
		if (count>0) {
			return false;
		}
		return true;
	}
	// add to the Users table in the database, throw an exception if username exists
	public static void createUser(String name, String pass)
	{
		establishConnection();
		if(checkUsername(name)==false) {
			
			System.out.println("Sorry, this username is already taken");
		}
		else if(checkPassword(pass)==false) {
			System.out.println("Sorry, this password is already taken");
		}
		else {
			//still need to check if username or password are unique or not
			String query = "insert into users(username,  password) values(name, pass)";
			try (Statement stmt  = conn.createStatement()){
				stmt.executeQuery(query);
			}catch(SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	// return true if password matches username, false if invalid user-password combination
	public static boolean attemptLogin(String name, String pass) 
	{
		establishConnection();
		if(checkUsername(name)==false) {
			return false;
		}
		else if(checkPassword(pass)==false) {
			return false;
		}
		else {
			String query = "SELECT count(*) from users WHERE username = ?, password = ?";
			PreparedStatement ps = conn.prepareStatement(queryCheck);
			ps.setString(1, name);
			ps.setString(2, pass);
			ResultSet resultSet = ps.executeQuery();
			int count = 0;
			if(resultSet.next()) {
			    count = resultSet.getInt(1);
			}
			if (count>0) {
				return true;
			}
			return false;
		}
	}
	
	// add to the Scores table in the database
	public static void addScore(String name, int s)
	{
		establishConnection();
		String q = "Select id from users where username = ?";
		PreparedStatement ps = conn.prepareStatement(queryCheck);
		ps.setString(1, name);
		ResultSet resultSet = ps.executeQuery();
		int id = resultSet;
		String query = "insert into scores(score, user_id) values(s, id)";
		try (Statement stmt  = conn.createStatement()){
			stmt.executeQuery(query);
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	// return top 10 scores in database (or every score if there are less than 10) with corresponding username
	public static ArrayList<Integer> getHighScores()
	{
		establishConnection();
		ArrayList<Integer> highScores = new ArrayList<Integer>();
		String q = "select score ORDER BY score DESC LIMIT 10;";
		try(Statement stmt = con.createStatement()){
			ResultSet resultSet = stmt.executeQuery(q);
			while(resultSet.next()) {
				Integer score = rs.getInt("score");
				highScores.add(score);
			}
		}
		return highScores;
	}
	public static ArrayList<String> getHighScoresUsers()
	{
		establishConnection();
		ArrayList<String> highUsers = new ArrayList<String>();
		String q = "select users.username as user from scores LEFT JOIN users ON scores.user_id = users.id ORDER BY score DESC LIMIT 10;";
		try(Statement stmt = con.createStatement()){
			ResultSet resultSet = stmt.executeQuery(q);
			while(resultSet.next()) {
				String user = rs.getInt("user");
				highUsers.add(user);
			}
		}
		return highUsers;
	}
	
	
}
