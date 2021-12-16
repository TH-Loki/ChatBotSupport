package logic.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bot.Ticket;
import interfaces.client.IClientCallback;
import ui.dialogs.UIPresenter;
import ui.dialogs.helper.ActiveScene;

public class Client extends UnicastRemoteObject implements IClientCallback
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822950527878457282L;
	

	public Client() throws RemoteException
	{
		super();
	}

	@Override
	public void sendStatus(String status) throws RemoteException
	{
		System.out.println("Status: " + status);
		if(status.equals("Connection established"))
		{
			UIPresenter.getInstance().changeActiveScene(ActiveScene.MAINVIEW, 0);
		}
	}

	@Override
	public void isClientAlive() throws RemoteException
	{
	}

	@Override
	public void updateTicket(Ticket ticket) throws RemoteException
	{
		UIPresenter.getInstance().updateTicket(ticket);
	}

}
