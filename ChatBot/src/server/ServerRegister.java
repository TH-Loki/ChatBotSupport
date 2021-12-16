package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRegister
{
	public static void registerRemote() throws RemoteException
	{
		 LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	     Registry registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
	 
	     registry.rebind("Server", Server.getInstance());
	     System.out.println("Server läuft...");
	}
}
