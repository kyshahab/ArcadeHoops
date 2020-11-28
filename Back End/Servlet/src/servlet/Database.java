package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
	
	// set these to be connections to database
		private static String db;
		private static String user;
		private static String pwd;
		private static Connection conn;
		
		public static Connection establishConnection() {
			db = "jdbc:mysql://localhost:3306/arcade_hoops?serverTimezone=UTC";
			
			user = "root";
			pwd = "MeadowPark!3";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				return DriverManager.getConnection(db, user, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e) {}
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

			try {
				Connection conn = establishConnection();
				String queryCheck = "SELECT count(*) from users WHERE password = ?";
				PreparedStatement ps;
				ps = conn.prepareStatement(queryCheck);
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
			} catch(SQLException e) {
				e.printStackTrace();
				return true;
			}
		}
		// add to the Users table in the database, throw an exception if username exists
		public static boolean createUser(String name, String pass) throws SQLException, IOException
		{
			Connection conn = establishConnection();
			if(checkUsername(name)==false || checkPassword(pass)==false) {
				throw new IOException(); 
			}
			else {
				try {
					
					//still need to check if username or password are unique or not
					String query = "INSERT INTO users(username,  password) VALUES(?, ?)";
					PreparedStatement ps  = conn.prepareStatement(query);
					ps.setString(1, name);
					ps.setString(2, pass);
					ps.executeUpdate();
					return true;
				}catch(SQLException se) {
					se.printStackTrace();
					return false;
				}
			}
		}
		
		// return true if password matches username, false if invalid user-password combination
		public static boolean attemptLogin(String name, String pass) throws SQLException 
		{
			Connection conn = establishConnection();
			if(checkUsername(name)==true) {
				return false;
			}
			else if(checkPassword(pass)==true) {
				return false;
			}
			else {
				String query = "SELECT count(*) FROM users WHERE username = ? AND password = ?";
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
		public static boolean addScore(String name, int s) throws SQLException
		{
			Connection conn = establishConnection();
			String q = "SELECT id FROM users WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setString(1, name);
			ResultSet resultSet = ps.executeQuery();
			int id = 0;
			if(resultSet.next()) {
			    id = resultSet.getInt(1);
			}
			try {
				
				String query = "INSERT INTO scores(score, user_id) VALUES(?, ?)";
				ps = conn.prepareStatement(query);
				ps.setInt(1, s);
				ps.setInt(2, id);
				ps.executeUpdate();
				return true;
			}catch(SQLException se) {
				se.printStackTrace();
				return false;
			}
		}
		
		// return top 10 scores in database (or every score if there are less than 10) with corresponding username
		public static ArrayList<Integer> getHighScores()
		{
			try {
				
				Connection conn = establishConnection();
				ArrayList<Integer> highScores = new ArrayList<Integer>();
				String q = "SELECT score FROM scores ORDER BY score DESC LIMIT 10;";
				PreparedStatement ps = conn.prepareStatement(q);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Integer score = rs.getInt("score");
					highScores.add(score);
				}
				return highScores;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ArrayList<Integer>();
			}
			
		}
		public static ArrayList<String> getHighScoresUsers()
		{
			try {
				Connection conn = establishConnection();
				ArrayList<String> highUsers = new ArrayList<String>();
				String q = "SELECT users.username as user FROM scores LEFT JOIN users ON scores.user_id = users.id ORDER BY score DESC LIMIT 10;";
				PreparedStatement ps = conn.prepareStatement(q);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String user = rs.getString("user");
					highUsers.add(user);
				}
				return highUsers;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ArrayList<String>();
			}
		}
	
	
}
