package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bot.ChatBot;
import interfaces.client.IClientCallback;
import interfaces.server.IServerService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import server.main.IsClientWorker;

public class Server extends UnicastRemoteObject implements IServerService
{
	private static Server server;

	private IsClientWorker worker;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7655423678945662888L;

	/**
	 * Es gibt maximal einen registrierten Client
	 */
	private IClientCallback client = null;

	private ChatBot bot;

	private Server() throws RemoteException
	{
		super();
	}

	public static Remote getInstance() throws RemoteException
	{
		String ipAddress;

		Socket socket = new Socket();
		try
		{
			socket.connect(new InetSocketAddress("google.com", 80));
			ipAddress = socket.getLocalAddress().getHostAddress();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("IP Adresse: " + ipAddress);
			alert.setContentText("Hier ist Ihre IP Adresse. Tragen Sie diese im Client ein.");

			alert.showAndWait();

			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if (server == null)
		{
			server = new Server();
		}
		return server;
	}

	@Override
	public void register(IClientCallback client) throws RemoteException
	{
		if (this.client == null)
		{
			this.client = client;
			worker = new IsClientWorker();
			worker.start();

			this.client.sendStatus("Connection established");
			initBot();
		}
	}

	@Override
	public void deRegister(IClientCallback client) throws RemoteException
	{
		if (this.client != null && client.equals(this.client))
		{
			this.client = null;
			worker.stopThread();
		}
	}

	public static IClientCallback getRegisteredClient()
	{
		return server.client;
	}

	public static void initBot()
	{
		server.bot = new ChatBot();
		server.bot.start();
	}

	@Override
	public void sendMessage(String message, int ticketID)
	{
		server.bot.receivedMessage(message, ticketID);
	}

	@Override
	public void createNewTicket() throws RemoteException
	{
		bot.chatStarted();
	}

	@Override
	public void deleteTicket(int ticketID) throws RemoteException
	{
		bot.deleteTicket(ticketID);
	}

}
