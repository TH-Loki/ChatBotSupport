package tools.css;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResourceLoader 
{
	

	
	/*
	 * Wer sich die stylsheet Laderei bei fx ausgedacht hat, gehört auch eine Watschn.
	 */
	private static final String RESOURCE_DESTINATION = "/resources/";
	
	
	/**
	 * globale Resourcen für das Grundlayout
	 */
	private static final String CSS_RESOURCE_DESTINATION =  RESOURCE_DESTINATION + "css/";
	
	/**
	 * Pfad zu dem Ordner wo die unterschiedlichen Sprachen abgelegt sind.
	 */
	private static final String LANGUAGE_RESOURCE_DESTINATION =  RESOURCE_DESTINATION + "languages/";
	
	private static final String PREFIX_FILE = "file:/";
	
	/**
	 * Pfad wo die xml für die Logging Konfiguration abgelegt ist.
	 */
	private static final String LOGGING_RESOURCE_CONFIG_DESTINATION =  RESOURCE_DESTINATION + "logging/";
	
	public static URL getGlobalResource(String cssFileName) throws MalformedURLException
	{
		File file = new File(CSS_RESOURCE_DESTINATION+cssFileName);
		String complete = "file:/"+file.getAbsolutePath();
		return  ClassLoader.getSystemResource(complete);
	}
	
	public static Properties getLanguage(String propertyFileName, Locale locale)
	{
		Properties rueckgabe = new Properties();
		try 
		{
			File file = new File("");
			file = new File(file.getAbsolutePath()+ LANGUAGE_RESOURCE_DESTINATION + propertyFileName+".properties" );
			
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
			rueckgabe.load(reader);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return rueckgabe;
	}
	
	public static File getLoggingConfiguration(String fileName)
	{
		File file = new File("");
		System.out.println("gesucht<"+file.getAbsolutePath()+  LOGGING_RESOURCE_CONFIG_DESTINATION + fileName+">");
		file = new File(file.getAbsolutePath()+  LOGGING_RESOURCE_CONFIG_DESTINATION + fileName);
		return file;
	}
	
	public static ObservableList<CSSContainer> getGlobalCSSContainerList() throws MalformedURLException 
	{
		List<CSSContainer> cssList = new ArrayList<CSSContainer>();
		//War das schon immer so bescheiden, mit der Setzung eines Pfades?
		File file = new File("");
		file = new File(file.getAbsolutePath()+CSS_RESOURCE_DESTINATION);
		
		File[] fileArray = file.listFiles();
		for(int i = 0; i < fileArray.length; i++)
		{
			String absPath = fileArray[i].getAbsolutePath();
			if(absPath.endsWith(".css"))
			{
				int lastIndex = absPath.lastIndexOf(File.separator);
				if(absPath.endsWith(".css"))
				{
					String filename = absPath.substring(lastIndex+1, absPath.length());
					cssList.add(new CSSContainer(filename, CSS_RESOURCE_DESTINATION));
				}
			}
		}
		return FXCollections.observableArrayList(cssList);
	}
	
	public static URL getResourceProperty(String resourceProperty) throws MalformedURLException
	{
		
		File file = new File(RESOURCE_DESTINATION + resourceProperty);
		String complete = "file:/"+file.getAbsolutePath();
		return  ClassLoader.getSystemResource(complete);
	}
	
	
	

}
