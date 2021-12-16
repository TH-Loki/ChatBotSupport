package ui.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader
{
	/**
	 * Globale Ablage der Bilder
	 */
	private static final String GLOBAL_ICONS = "/resources/icons";
	
	public static final String SUFFIX_FILE  = ".png";
	
	public static InputStream getResourceStrem(String pkname, String fname) throws FileNotFoundException
	{
		String resname = "" + pkname + "/" + fname;
		File file = new File("");
		file = new File(file.getAbsolutePath() + resname);
		InputStream inputStream = new FileInputStream(file.getAbsolutePath());
		
		return inputStream;
	}
	
	public static Image getIconImage(String fileName)
	{
		Image image = null;
		
		if(!fileName.contains(SUFFIX_FILE))
		{
			fileName = fileName + SUFFIX_FILE;
		}
		try(InputStream ins = ImageLoader.getResourceStrem(GLOBAL_ICONS, fileName);)
		{
			image = new Image(ins);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return image;
	}
	
	public static void setIconImage(Button button, String filename)
	{
		ImageView imageView = new ImageView(ImageLoader.getIconImage(filename));
		imageView.setFitHeight(15);
		imageView.setFitWidth(15);
		imageView.setOpacity(0.7);
		button.setGraphic(imageView);
	}
}
