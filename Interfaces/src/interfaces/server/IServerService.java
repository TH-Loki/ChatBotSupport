package interfaces.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import interfaces.client.IClientCallback;

public interface IServerService extends Remote
{
	public void register(IClientCallback client) throws RemoteException;
	
	public void deRegister(IClientCallback client) throws RemoteException;
	
	public void sendMessage(String message, int ticketID) throws RemoteException;
	
	public void createNewTicket() throws RemoteException;
	
	public void deleteTicket(int ticketID) throws RemoteException;
}
