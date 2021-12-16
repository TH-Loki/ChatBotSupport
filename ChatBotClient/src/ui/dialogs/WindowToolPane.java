package ui.dialogs;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * Drei Buttons für minimieren, maximieren und close
 * @author m.goerlich
 *
 */
public class WindowToolPane extends HBox
{
	/**
	 * Entscheidet ob über den Close Button die komplette Anwendung geschlossen wird.
	 */
	private boolean isExitable = false;
	
	private boolean isMaximized = true;
	
	/**
	 * Da muss beim switch das Icon nocht getauscht werden
	 */
	private Button maximize;
	

	/**
	 * Sicherung der Abmaße
	 */
	private Rectangle2D savedWindowBounds = null;
	
	/**
	 * Diese Stage wird beendet und ist über den Konstruktor zu übergeben
	 */
	private Stage stageToExit;
	
	private Button close;
	
	
	public WindowToolPane(Stage stageToExit, boolean isExitable, boolean isPaddingUse, boolean isDialogMaximized)
	{
		this.stageToExit = stageToExit;
		this.isExitable = isExitable;
		this.isMaximized = isDialogMaximized;
		
		if(isPaddingUse)
		{
			this.setPadding(new Insets(5,5,5,5));
			
		}
		
		
		this.setAlignment(Pos.BASELINE_RIGHT);
		this.setSpacing(5);
		
		this.getChildren().addAll(getMinimizeButton(), getMaximizeButton(), getCloseButton());
	}

	private Node getCloseButton() 
	{
		close = new Button("");
		close.setId("roundButton");
		GlyphsDude.setIcon(close, MaterialDesignIcon.WINDOW_CLOSE);
		close.setFocusTraversable(false);
		close.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				// Kann der Dialog die Applikation beenden?
				if (isExitable)
				{
					// je nach Einstellung wird die Anwendung komplett beendet
					// oder die PrimaryStage wird unsichtbar
					UIPresenter.getInstance().hideOrClosePrimaryStage();

				}
				else
				{
					// Im Normalfall wird dann nur die Stage beendet (Anwendung
					// bei einzel stehende Dialoge)
					stageToExit.close();
				}

			}

		});
		
		return close;
	}

	private Node getMaximizeButton() 
	{
		maximize = new Button("");
		maximize.setId("roundButton");
		GlyphsDude.setIcon(maximize, MaterialDesignIcon.WINDOW_MAXIMIZE);
		maximize.setFocusTraversable(false);
		maximize.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent arg0)
			{
				toogleMaximizedForScreen();
			}
			
		});
		return maximize;
	}

	protected void toogleMaximizedForScreen()
	{
		
		double stageX = stageToExit.getX();
		if(stageX < 0)
		{
			stageX = 0;
		}
		double stageY = stageToExit.getY();
		if(stageY < 0)
		{
			stageY = 0;
		}
		
		final Screen screen = Screen.getScreensForRectangle(stageX, stageY, 1, 1).get(0);
		if(isMaximized)
		{
			isMaximized = false;
			GlyphsDude.setIcon(maximize, MaterialDesignIcon.WINDOW_MAXIMIZE);
			if(savedWindowBounds != null)
			{
				stageToExit.setX(savedWindowBounds.getMinX());
				stageToExit.setY(savedWindowBounds.getMinY());
				stageToExit.setWidth(savedWindowBounds.getWidth());
				stageToExit.setHeight(savedWindowBounds.getHeight());
			}
		}
		else
		{
			GlyphsDude.setIcon(maximize, MaterialDesignIcon.CONTENT_COPY);
			//Hier der Bereich für die Maximierung des Screens
			isMaximized = true;
			savedWindowBounds = new Rectangle2D(stageToExit.getX(), stageToExit.getY(), stageToExit.getWidth(), stageToExit.getHeight());
			stageToExit.setX(screen.getVisualBounds().getMinX());
			stageToExit.setY(screen.getVisualBounds().getMinY());
			stageToExit.setWidth(screen.getVisualBounds().getWidth());
			stageToExit.setHeight(screen.getVisualBounds().getHeight());
		}
	}

	private Node getMinimizeButton() 
	{
		Button minimize = new Button("");
		minimize.setId("roundButton");
		GlyphsDude.setIcon(minimize, MaterialDesignIcon.WINDOW_MINIMIZE);
		minimize.setFocusTraversable(false);
		minimize.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				stageToExit.setIconified(true);
			}
			
		});
		return minimize;
	}
	
	/**
	 * Eine andere Alternative. Diese ist dann zu wählen, wenn vom aufrufenden Dialog noch ein wenig mehr zum Abschluss getan werden muss.
	 * @param closingEvent
	 */
	public void setClosingEvent(EventHandler<ActionEvent> closingEvent)
	{
		close.setOnAction(closingEvent);
	}
	//TODO Rückgabe von initialisierten close button ermöglichen
	
	/**
	 * Hier wird davon ausgegangen, dass alles korrekt initialisiert wurde!
	 * <br>Ist die Methode für das Lauschen auf die Tastaturkommandos und dem Lostreten des Fire Event.
	 * @return
	 */
	public Button getCloseButtonForKeyListener()
	{
		return close;
	}
	

}
