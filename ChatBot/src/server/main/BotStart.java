package server.main;

import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.stage.Stage;
import server.Server;
import server.ServerRegister;

public class BotStart extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			ServerRegister.registerRemote();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		boolean isValid = false;
		do
		{
			System.out.println("warte auf Client");
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if(Server.getRegisteredClient() != null)
			{
				isValid = true;
			}
		}
		while(!isValid);
	}


}
