package bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bot.enums.EChatSender;
import bot.helper.GenericPair;

public class Ticket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -482079909205208588L;
	private int ticketID;
	private List<GenericPair<EChatSender, String>> chatHistory;
	
	public Ticket(int ticketID)
	{
		this.setTicketID(ticketID);
		chatHistory = new ArrayList<GenericPair<EChatSender,String>>();
	}

	public int getTicketID()
	{
		return ticketID;
	}

	public void setTicketID(int ticketID)
	{
		this.ticketID = ticketID;
	}
	
	public void addMessageToChat(EChatSender sender, String text)
	{
		if(chatHistory != null)
		{
			chatHistory.add(new GenericPair<EChatSender, String>(sender, text));
		}
	}

	public List<GenericPair<EChatSender, String>> getChatHistory()
	{
		return chatHistory;
	}
	
	public String getLastText()
	{
		if(chatHistory != null && chatHistory.size() > 0)
		{
			return chatHistory.get(chatHistory.size() -1).getRight();
		}
		return "";
	}
}
