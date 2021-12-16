package tools.css;

import java.net.URL;

public class CSSContainer 
{
	private String fileName;
	
	/**
	 * ein Pfad innerhalb von resources...kein absoluter Pfad!
	 */
	private String resourceFolder;

	private URL url;
	
	public CSSContainer(String fileName, String resourceFolder)
	{
		this.fileName = fileName;
		//Benschnitt wird im späteren nicht mehr benötigt
		if(resourceFolder.startsWith("/"))
		{
			resourceFolder = resourceFolder.substring(1, resourceFolder.length());
		}
		
		this.resourceFolder = resourceFolder;
	}
	
	public String getFilename() {
		return fileName;
	}

	public void setFilename(String fileName) {
		this.fileName = fileName;
	}
	
	public String toString()
	{
		return fileName;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	
	public String getResourceFolder() {
		return resourceFolder;
	}

	public void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}
}
