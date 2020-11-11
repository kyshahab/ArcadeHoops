import java.util.*;
import java.sql.*;

public class Database {
	
	// set these to be connections to database
		private static String db;
		private static String user;
		private static String pwd;
		private static Connection conn;
		
		public static Connection establishConnection() {
			db = "jdbc:mysql://localhost:3306/arcade_hoops?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Los_Angeles";
			user = "root";
			pwd = "Artin2020";
			try {
				return DriverManager.getConnection(db, user, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return conn;
		}		
		
		//returns false if username already exists	
		public static boolean checkUsername(String name) throws SQLException {
			Connection conn = establishConnection();
			String queryCheck = "SELECT count(*) from users WHERE username = ?";
			PreparedStatement ps;
			ResultSet resultSet = null;
			try {
				ps = conn.prepareStatement(queryCheck);
				ps.setString(1, name);
				resultSet = ps.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int count = 0;
			
			if(resultSet.next()) {
			    count = resultSet.getInt(1);
			}
			if (count>0) {
				return false;
			}
			return true;
		}
		public static boolean checkPassword(String pass) throws SQLException {
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
		public static void createUser(String name, String pass) throws SQLException
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
		public static boolean attemptLogin(String name, String pass) throws SQLException 
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
				PreparedStatement ps = conn.prepareStatement(query);
				try {
					ps.setString(1, name);
					ps.setString(2, pass);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				ResultSet resultSet = null;
				try {
					resultSet = ps.executeQuery();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int count = 0;
				try {
					if(resultSet.next()) {
					    try {
							count = resultSet.getInt(1);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count>0) {
					return true;
				}
				return false;
			}
		}
		
		// add to the Scores table in the database
		public static void addScore(String name, int s) throws SQLException
		{
			establishConnection();
			String q = "Select id from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setString(1, name);
			ResultSet resultSet = ps.executeQuery();
			int id = 0;
			if(resultSet.next()) {
			    id = resultSet.getInt(1);
			}
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
			try(Statement stmt = conn.createStatement()){
				ResultSet resultSet = stmt.executeQuery(q);
				while(resultSet.next()) {
					Integer score = resultSet.getInt("score");
					highScores.add(score);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return highScores;
		}
		public static ArrayList<String> getHighScoresUsers()
		{
			establishConnection();
			ArrayList<String> highUsers = new ArrayList<String>();
			String q = "select users.username as user from scores LEFT JOIN users ON scores.user_id = users.id ORDER BY score DESC LIMIT 10;";
			try(Statement stmt = conn.createStatement()){
				ResultSet resultSet = stmt.executeQuery(q);
				while(resultSet.next()) {
					String user = resultSet.getString("user");
					highUsers.add(user);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return highUsers;
		}
	
	
}
