package ui.dialogs.interfaces;

import javafx.scene.Node;
import javafx.stage.Stage;

public interface DragMouseComponent 
{
	
	public void setStartX(double startX);
	
	public void setStartY(double startY);
	
	public double getStartX();
	
	public double getStartY();
	
	/**
	 * sich selber also dort wo es implementiert ist
	 * @return
	 */
	public Node getThis();
	
	/**
	 * Die Stage auf den der Dialog abgelegt ist.
	 * @return
	 */
	public Stage getStage();
	
	
	

}
