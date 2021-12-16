package ui;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.connection.Connection;
import ui.dialogs.UIPresenter;

public class ConfigurationApplication extends Application
{
	private Button maximize, minimize, close;
	private UIPresenter uiPresenter;
	private String ipAddress = "";

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setHeight(400);
		primaryStage.setWidth(300);

		uiPresenter = UIPresenter.createInstance(primaryStage);
		System.out.println(uiPresenter);

		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText("");
		dialog.setContentText("Tragen Sie bitte die IP Adresse Ihres Servers ein.");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
		{
			ipAddress = result.get();
		}
		
		Connection.connect(ipAddress);

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
	}

}
