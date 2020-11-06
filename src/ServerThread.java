import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

	private PrintWriter pw;
	private BufferedReader br;
	private ServerMain sm;
	private ArrayList<Score> highScores;

	public ServerThread(Socket s, ServerMain sm)
	{
		//your code here
		try {
			this.sm = sm;
	
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream(), true);
			
			// send this originally
			highScores = Database.getHighScores();
			
			this.start();
		}
		
		catch(Exception e) {}
	}
	
	// sends score to client
	public void sendScore()
	{
		
	}
	
	public synchronized boolean notEqual(ArrayList<Score> check)
	{
		if (highScores.size() != check.size())
		{
			return true;
		}

		for (int i = 0; i < check.size(); i++)
		{
			String name1 = check.get(i).getName();
			String name2 = highScores.get(i).getName();
			int score1 = check.get(i).getScore();
			int score2 = highScores.get(i).getScore();
			
			if (!name1.equals(name2) || score1 != score2)
			{
				return true;
			}
		}
		return false;
	}

	public void run()
	{
		//your code here
		ArrayList<Score> temp = null;
		while (true)
		{
			try
			{
				if (notEqual(temp))
				{
					sm.sendScores(temp);
					highScores = temp;
				}
			}
			catch (Exception e) {}
		}
	}

}