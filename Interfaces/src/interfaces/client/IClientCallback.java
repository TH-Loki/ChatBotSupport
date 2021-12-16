package interfaces.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import bot.Ticket;

public interface IClientCallback extends Remote
{
	public void sendStatus(String status) throws RemoteException;
	
	public void isClientAlive() throws RemoteException;
	
	public void updateTicket(Ticket ticket) throws RemoteException;
}
