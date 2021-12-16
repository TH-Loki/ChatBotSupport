package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bot.helper.GenericPair;

public class Datenbank extends Thread 
{
	private Connection connection;
	private Statement statement;
	private String url;
	private ResultSet keyWordsSet;
	private String answer = "";
	private List<GenericPair<String, String>> keyWordsAndAnswers;

	public Datenbank() 
	{
	}

	@Override
	public void run()
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			url = "jdbc:mysql://localhost/database";
			connection = DriverManager.getConnection(url, "root", "");
			statement = connection.createStatement();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public String readDBData(String message) 
	{
		keyWordsAndAnswers = new ArrayList<GenericPair<String, String>>();
		try
		{
			String query = "SELECT * FROM keywords" ;
			keyWordsSet = statement.executeQuery(query);
			if(message.contains(" "))
			{
				boolean breakVar = true;
				String[] words = message.split(" ");
				for(int i = 0; i < words.length && breakVar; i++)
				{
					String word = words[i];
					answer = findAnswer(word);
					if(!answer.equals(""))
					{
						breakVar = false;
						break;
					}
				}
			}
			else
			{
				answer = findAnswer(message);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
		
		if(answer.equals(""))
		{
			return "Leider habe ich zu Ihrer Anfrage keinen Eintrag finden können. Bitte formulieren Sie Ihre Frage um, oder kontaktieren Sie einen Mitarbeiter unter 0172/1872792";
		}
		else
		{
			return answer;
		}
	}

	private String findAnswer(String word) 
	{
		System.out.println("Word: " + word);
		
		try 
		{
			while(keyWordsSet.next())
			{
				keyWordsAndAnswers.add(new GenericPair<String, String>(keyWordsSet.getString("KeyWords"), keyWordsSet.getString("Antwort")));
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		for(int i = 0; i < keyWordsAndAnswers.size(); i++)
		{
			String keyWords = keyWordsAndAnswers.get(i).getLeft();
			if(keyWords.contains(word))
			{
				return keyWordsAndAnswers.get(i).getRight();
			}
		}
		return "";
	}

}
