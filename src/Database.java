import java.util.*;
import java.sql.*;

public class Database {
	
	// set these to be connections to database
		private static String db;
	private static String user;
	private static String pwd;
	private static Connection conn;

	//running tests
//	public static void main(String[] args) {
//		try {
//			
//			addScore("Katrina", 20);
//			System.out.println(attemptLogin("nat", "p"));
//			System.out.println(attemptLogin("gamer1", "pass1"));
//			System.out.println(getHighScores());
//			System.out.println(getHighScoresUsers());
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

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

	// returns false if username already exists
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

		if (resultSet.next()) {
			count = resultSet.getInt(1);
		}
		if (count > 0) {
			return false;
		}
		return true;
	}

	public static boolean checkPassword(String pass) throws SQLException {
		Connection conn = establishConnection();
		String queryCheck = "SELECT count(*) from users WHERE password = ?";
		PreparedStatement ps;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement(queryCheck);
			ps.setString(1, pass);
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int count = 0;

		if (resultSet.next()) {
			count = resultSet.getInt(1);
		}
		if (count > 0) {
			return false;
		}
		return true;
	}

	// add to the Users table in the database, throw an exception if username exists
	public static void createUser(String name, String pass) throws SQLException {
		Connection conn = establishConnection();
		if (checkUsername(name) == false) {

			System.out.println("Sorry, this username is already taken");
		} else {
			// still need to check if username or password are unique or not
			String query = "insert into users(username,  password) values(?, ?)";
			try (Statement stmt = conn.createStatement()) {
				PreparedStatement p = conn.prepareStatement(query);
				p.setString(1, name);
				p.setString(2, pass);
				p.executeUpdate();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	// return true if password matches username, false if invalid user-password
	// combination
	public static boolean attemptLogin(String name, String pass) throws SQLException {
		Connection conn = establishConnection();
		if (checkUsername(name) == true) {
			return false;
		} else if (checkPassword(pass) == true) {
			return false;
		} else {
			String query = "SELECT count(*) from users WHERE username = ? AND password = ?";
			PreparedStatement ps;
			ResultSet resultSet = null;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, name);
				ps.setString(2, pass);
				resultSet = ps.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int count = 0;

			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			if (count > 0) {
				return true;
			}
			return false;
		}
	}

	// add to the Scores table in the database
	public static void addScore(String name, int s) throws SQLException {
		Connection conn = establishConnection();
		if (checkUsername(name) == true) {
			System.out.println("User does not exist");
			return;
		}
		String q = "Select id from users where username = ?;";
		PreparedStatement ps = conn.prepareStatement(q);
		ps.setString(1, name);
		ResultSet resultSet = ps.executeQuery();
		int id = 0;
		if (resultSet.next()) {
			id = resultSet.getInt(1);
		}
		String query = "insert into scores(score, user_id) values(?, ?);";
		try (Statement stmt = conn.createStatement()) {
			PreparedStatement p = conn.prepareStatement(query);
			p.setInt(1, s);
			p.setInt(2, id);
			p.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	// return top 10 scores in database (or every score if there are less than 10)
	// with corresponding username
	public static ArrayList<Integer> getHighScores() {
		Connection conn = establishConnection();
		ArrayList<Integer> highScores = new ArrayList<Integer>();
		String q = "select score from scores ORDER BY score DESC LIMIT 10;";
		try (Statement stmt = conn.createStatement()) {
			ResultSet resultSet = stmt.executeQuery(q);
			while (resultSet.next()) {
				Integer score = resultSet.getInt("score");
				highScores.add(score);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return highScores;
	}

	public static ArrayList<String> getHighScoresUsers() {
		Connection conn = establishConnection();
		ArrayList<String> highUsers = new ArrayList<String>();
		String q = "select users.username as user from scores LEFT JOIN users ON scores.user_id = users.id ORDER BY score DESC LIMIT 10;";
		try (Statement stmt = conn.createStatement()) {
			ResultSet resultSet = stmt.executeQuery(q);
			while (resultSet.next()) {
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
