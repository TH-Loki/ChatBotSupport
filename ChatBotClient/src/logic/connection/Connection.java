package logic.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import interfaces.server.IServerService;
import logic.client.Client;

public class Connection
{
	private static IServerService service;
	
	private static Client client;
	
	public static void connect(String ipAddress) throws RemoteException, MalformedURLException, NotBoundException
	{
		if(ipAddress != null && !ipAddress.equals(""))
		{
			service = (IServerService) Naming.lookup("rmi://" + ipAddress + "/Server");
			
			Socket socket = new Socket();
			try
			{
				socket.connect(new InetSocketAddress("google.com", 80));
				String clientIP = socket.getLocalAddress().getHostAddress();
				System.out.println("clientIP " + clientIP);
				
				System.setProperty("java.rmi.server.hostname", clientIP);
				client = new Client();
				service.register(client);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void addNewChat()
	{
		try
		{
			service.createNewTicket();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	public static void deleteTicket(int ticketID)
	{
		try
		{
			service.deleteTicket(ticketID);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	public static void sendMessage(String message, int ticketID)
	{
		try
		{
			service.sendMessage(message, ticketID);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
}
