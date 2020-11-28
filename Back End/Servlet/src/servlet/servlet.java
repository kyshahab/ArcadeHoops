package servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

/**
 * Servlet implementation class servlet
 */
@WebServlet("/servlet")
public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    //When I want to send but not change data
    
    //attemptLogin and getHighScores
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		 try {
			 
			String loginBoolean = request.getParameter("login");
			 
		    User user = null;
		    Boolean login = false;
		    
		    if(loginBoolean.equals("true")) {
		    	String username = request.getParameter("username");
		    	String password = request.getParameter("password");
		    	
		    	user = new User(username, password);
		    } else if(loginBoolean.equals("false")) {
		    	login = true;
		    } else {
		    	Response res = new Response(false);
				 gson.toJson(res, out);
		    }
		    
			
		   if(user != null) {
			   //call on attemptLogin
			   Boolean valid = Database.attemptLogin(user.username, user.password);
			   Response res = new Response(valid);
			   
			   gson.toJson(res, out);
			   
		   } else if(login == true) {
			   	ArrayList<Integer> scores = Database.getHighScores();
				ArrayList<String> names = Database.getHighScoresUsers();
				ArrayList<UserScore> temp = getList(scores, names);
				 
				 
				 gson.toJson(temp, out);
			   
		   } else {
			   
			   Response res = new Response(false);
			   gson.toJson(res, out);
			   
		   }
		  
		 } catch (Exception e) {
			 System.out.println("Error in servlet");
			 e.printStackTrace();
			 Response res = new Response(false);
			  gson.toJson(res, out);
		 }
		  
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	//when I want to change the database
	
	//createUser and addScore
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		StringBuffer jb = new StringBuffer();
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String line = null;
		 try {
			 
			 
			 String createUser = request.getParameter("createUser");
			 
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  
		  
		  User user = null;
		  UserScore userScore = null;
		  
		  
		  if(createUser.equals("true")) {
			  user = gson.fromJson(jb.toString(), User.class);
		  } else if (createUser.equals("false")) {
			  userScore = gson.fromJson(jb.toString(), UserScore.class);
		  } else {
			 Response res = new Response(false);
			 gson.toJson(res, out);
			 return;
		  }

		  
		  
		  if(user != null) {
			  try {
				  Boolean b = Database.createUser(user.username, user.password);
				  Response res = new Response(b);
				  gson.toJson(res, out);
			  }
			  catch (Exception e) {
				  Response res = new Response(false);
				  gson.toJson(res, out);
			  }
		  } else if(userScore != null) {
			  Boolean b = Database.addScore(userScore.username, userScore.score);
			  Response res = new Response(b);
			  gson.toJson(res, out);
		  } else {
			  Response res = new Response(false);
			  gson.toJson(res, out);
		  }
		  
		  
		 } catch (Exception e) {
			 Response res = new Response(false);
			 gson.toJson(res, out);
		 }
	}
	
	public static ArrayList<UserScore> getList(ArrayList<Integer> scores, ArrayList<String> users)
	{
		ArrayList<UserScore> temp = new ArrayList<UserScore>();
		int i = 0;
		while (i < scores.size() && i < users.size())
		{
			temp.add(new UserScore(scores.get(i), users.get(i)));
			i++;
		}
		return temp;
	}

}


class User {
	String username;
	String password;
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
 }

class Response {
	Boolean success;
	public Response(Boolean success) {
		this.success = success;
	}
}

class UserScore {
	Integer score;
	String username;
	public UserScore (Integer s, String user)
	{
		score = s;
		username = user;
	}
}






