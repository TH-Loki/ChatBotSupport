package bot;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import bot.enums.EChatSender;
import server.Server;

public class ChatBot extends Thread
{
	private boolean runner = false, received = false;
	private int sleep = 30000;
	private String lastUserMessage;
	private List<Ticket> tickets;
	private int counter = 0;
	private Ticket ticketToSend;
	
	public ChatBot()
	{
		runner = true;
		tickets = new ArrayList<Ticket>();
	}

	@Override
	public void run()
	{
		long endeZeit = System.currentTimeMillis() + sleep;
		while (runner)
		{
			try
			{
				if (!received)
				{
					Thread.sleep(1000);
					if (System.currentTimeMillis() >= endeZeit)
					{
						endeZeit = System.currentTimeMillis() + sleep;
					}
				}
				else
				{
					Server.getRegisteredClient().updateTicket(ticketToSend);
					received = false;
				}
			}
			catch (InterruptedException | RemoteException e)
			{
			}
		}
	}

	public void chatStarted()
	{
		counter++;
		Ticket ticket = new Ticket(counter);
		String firstMessage = "Schönen guten Tag, Ich bin Ihr persönlicher Chat Assistent.Wie darf ich Ihnen behilflich sein?";
		ticket.addMessageToChat(EChatSender.SERVER, firstMessage);
		tickets.add(ticket);
		try
		{
			Server.getRegisteredClient().updateTicket(ticket);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public void receivedMessage(String message, int ticketID)
	{
		for(int i = 0; i < tickets.size(); i++)
		{
			if(tickets.get(i).getTicketID() == ticketID)
			{
				ticketToSend = tickets.get(i);
			}
		}
		received = true;
		lastUserMessage = message;
	}

	public void deleteTicket(int ticketID)
	{
		for(int i = 0; i < tickets.size(); i++)
		{
			if(tickets.get(i).getTicketID() == ticketID)
			{
				tickets.remove(i);
			}
		}
	}
}
