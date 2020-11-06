import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ServerMain {
	
	private ArrayList<ServerThread> serverThreads;
	
	public ServerMain(int port)
	{
		try
		{
			System.out.println("Binding to port " + port);
			ServerSocket ss = new ServerSocket(port);
			serverThreads = new ArrayList<>();
			while(true)
			{
				Socket s = ss.accept();   //  Accept the incoming request
				System.out.println("Connection from " + s + " at " + new Date());
				ServerThread st = new ServerThread(s, this);
				System.out.println("Adding this client to active client list");
				serverThreads.add(st);
			}
		}
		catch (Exception ex) {}

	}
	
	// send new list of scores to every client
	public void sendScores(ArrayList<Score> scores)
	{
		
	}

	public static void main(String [] args)
	{
		new ServerMain(6789);
	}
}