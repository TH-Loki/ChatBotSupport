package server.main;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.Server;

public class IsClientWorker extends Thread
{
	private boolean runner = false;
	private int sleep = 30000;
	
	public IsClientWorker()
	{
		runner = true;
	}
	
	public void stopThread()
	{
		runner = false;
	}
	
	@Override
	public void run()
	{
		long endeZeit = System.currentTimeMillis() + sleep;

		while(runner)
		{
			try
			{
				Thread.sleep(1000);
				if(System.currentTimeMillis() >= endeZeit)
				{
					Server.getRegisteredClient().isClientAlive();
					endeZeit = System.currentTimeMillis() + sleep;
				}
			}
			catch(Exception e)
			{
				try
				{
					Remote remote = Server.getInstance();
					if(remote instanceof Server)
					{
						Server server = (Server) remote;
						server.deRegister(Server.getRegisteredClient());
						stopThread();
					}
				}
				catch (RemoteException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
}
