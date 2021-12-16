package ui.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ui.dialogs.interfaces.DragMouseComponent;

public class UITools
{
	public static Node createHorizontalSpacer() 
	{
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	public static Node createVerticalSpacer()
	{
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	/**
	 * Vergroessern, verkleineren eines übergebenen Bildes. Rückgabe erfolgt als ImageView
	 * <br>Der Skalierungsfaktor gibt die neue Dimension an z.B. 0,5f = hälfte verkleinern. 
	 */
	public static ImageView scaleImage(Image image, double scaleFactorX,  double scaleFactorY)
	{
		if(image == null || scaleFactorX == 0 || scaleFactorY == 0)
			return new ImageView(image);
		
		
		double newWidth = image.getWidth() * scaleFactorX;
		double newHeight = image.getHeight() * scaleFactorY;
		
		ImageView rueckgabe = new ImageView(image);
		
		
		rueckgabe.setFitWidth(newWidth);
		rueckgabe.setFitHeight(newHeight);
		return rueckgabe;
	}
	
	public static Cursor findCursorPosition(double startX, double startY, MouseEvent event) 
	{
		//Resize wird erstmal hier nicht geben...ist meiner Meinung nach groß genug
		//dannn wird das Pfeilkreuz angezeigt.
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
			return Cursor.MOVE;
		
		return Cursor.DEFAULT;
	}

	/**
	 * Bei den meisten Dialogen wird der eigentliche Inhalt einheitlich mit Abständen versehen
	 * @param vBoxContent
	 */
	public static void setVBoxContentSpacing(VBox vBoxContent) 
	{
		vBoxContent.setSpacing(7);
		vBoxContent.setPadding(new Insets(15, 15, 15, 15));
	}

	/**
	 * Mechanismus für die normale Bewegung eines Dialogs auf der Oberfläche.
	 * <br>wird vorallem bei den "losgelösten" Dialogen benötigt
	 * @param startX
	 * @param startY
	 * @param stage
	 * @param node
	 */
	public static void appendMouseDragComponents(DragMouseComponent dragMouseComponent)
	{

		//listener für die Maus
		dragMouseComponent.getThis().setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				dragMouseComponent.setStartX(event.getSceneX());
				dragMouseComponent.setStartY(event.getSceneY());
				//herausfinden ob move oder oder resize erforderlich ist
				Cursor value = UITools.findCursorPosition(dragMouseComponent.getStartX(), dragMouseComponent.getStartY(), event);
				dragMouseComponent.getStage().getScene().setCursor(value);
			}
		});
		
		dragMouseComponent.getThis().setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) 
			{
				
				
				if(dragMouseComponent.getStage().getScene().getCursor() == Cursor.MOVE)
				{
					dragMouseComponent.getStage().getScene().getWindow().setX( event.getScreenX() - dragMouseComponent.getStartX() );
					dragMouseComponent.getStage().getScene().getWindow().setY( event.getScreenY() - dragMouseComponent.getStartY());
				}
				else 
				{
					//handleResize(event);
				}
			}
			
		});
		
		dragMouseComponent.getThis().setOnMouseMoved(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event)
			{
				//Hier die Prüfung ob der Cursor verändert werden muss bezieht sich auf das resize
				Cursor value = UITools.findCursorPosition(event.getSceneX(), event.getSceneY(), event);
				//Alles anzeigen außer move, weil das nervt ansonsten nur auf der Oberfläche
				//nur s, w und sued-west für den resize die andere Seite flackert und ich weiß nicht wieso
				if(value != Cursor.MOVE)
				{
					//setNewInitialEventCoordinates(event);
					dragMouseComponent.getStage().getScene().setCursor(value);
				}
				
					
			}
		});
		
		dragMouseComponent.getThis().setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				dragMouseComponent.getStage().getScene().setCursor(Cursor.DEFAULT);
			}
			
		});
		
	}
	
	public static String getDateFormattedString(long dateTimeInMillis)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
		Date date = new Date(dateTimeInMillis);
		return dateFormat.format(date);
	}	

}
