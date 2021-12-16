package ui.dialogs;

import java.awt.event.KeyEvent;
import java.util.List;

import bot.Ticket;
import bot.enums.EChatSender;
import bot.helper.GenericPair;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import logic.connection.Connection;
import ui.dialogs.helper.ActiveScene;
import ui.dialogs.interfaces.IMainContent;
import ui.tools.ImageLoader;
import ui.tools.UITools;

public class ChatViewDialog extends BorderPane implements IMainContent
{
	private class ChatMessage extends Label
	{
		private Label label;

		public ChatMessage(String text)
		{
			label = this;
			label.setWrapText(true);
			label.setStyle("-fx-border-color: #000000;" + "-fx-text-fill: #2a4e89;" + "-fx-border-width: 0.5;");
			label.setText(text);
		}

	}

	private ScrollPane scrollPane;
	private VBox vBox;
	private HBox topBox, headerBox, bottomBox;
	private GridPane gridPane;
	private TextField textField;
	private Button send, back;
	private Label ticketID;
	private Ticket ticket;
	private int rowCounter = 0;

	public ChatViewDialog(Ticket ticket)
	{
		super();
		this.setId("default_scene");
		this.ticket = ticket;
		
		HBox hBox = new HBox();
		// ein bisschen weier runter damit es ungefähr auf einer Höhe mit den
		// WindowToolPane ist
		hBox.setPadding(new Insets(5, 0, 0, 0));

		WindowToolPane wtp = new WindowToolPane(UIPresenter.getPrimaryStage(), true, true, false);
		topBox = new HBox();
		topBox.setPadding(new Insets(5, 5, 5, 5));
		topBox.getChildren().addAll(hBox, UITools.createHorizontalSpacer(), wtp);
		this.setTop(topBox);

		vBox = new VBox();
		vBox.setSpacing(3);
		vBox.setPadding(new Insets(5, 5, 5, 5));
		vBox.setAlignment(Pos.CENTER);

		headerBox = new HBox();
		headerBox.setSpacing(3);
		headerBox.setPadding(new Insets(5, 5, 5, 5));

		back = new Button();
		back.setId("roundButton");
		ImageLoader.setIconImage(back, "baseline_arrow_back_ios_black_18dp.png");
		
		back.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				UIPresenter.getInstance().changeActiveScene(ActiveScene.MAINVIEW, 0);
			}
		});

		ticketID = new Label();
		ticketID.setId("label_colorized_text");
		ticketID.setText(getHeaderText());

		headerBox.getChildren().addAll(back, ticketID, UITools.createHorizontalSpacer());

		gridPane = new GridPane();
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.setHgap(5);

		ColumnConstraints columnServer = new ColumnConstraints();
		columnServer.setMaxWidth(175);
		ColumnConstraints columnClient = new ColumnConstraints();
		columnClient.setMaxWidth(175);

		List<GenericPair<EChatSender, String>> chatHistory = ticket.getChatHistory();
		if (chatHistory.size() > 0)
		{
			for (int i = 0; i < chatHistory.size(); i++)
			{
				ChatMessage message = new ChatMessage(chatHistory.get(i).getRight());
				switch (chatHistory.get(i).getLeft())
				{
					case CLIENT:
						gridPane.add(message, 1, rowCounter);
						break;
					case SERVER:
						gridPane.add(message, 0, rowCounter);
						break;
				}
				rowCounter++;
			}
		}
		gridPane.getColumnConstraints().addAll(columnServer, columnClient);

		bottomBox = new HBox();
		bottomBox.setSpacing(3);
		bottomBox.setPadding(new Insets(5, 5, 5, 5));

		textField = new TextField();

		send = new Button();
		send.setId("roundButton");
		ImageLoader.setIconImage(send, "baseline_send_black_18dp.png");
		send.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Connection.sendMessage(textField.getText(), ticket.getTicketID());
			}
		});
		
		HBox.setHgrow(textField, Priority.ALWAYS);
		bottomBox.getChildren().addAll(textField, send);

		vBox.getChildren().addAll(headerBox, gridPane);

		scrollPane = new ScrollPane();
		scrollPane.setContent(vBox);
		scrollPane.setFitToWidth(true);

		this.setCenter(scrollPane);
		
		this.setBottom(bottomBox);
	}

	private String getHeaderText()
	{
		int ticketID = ticket.getTicketID();
		StringBuilder sb = new StringBuilder();
		sb.append("Ticket #");
		if (ticketID < 10)
		{
			sb.append("000");
		}
		else if (ticketID < 100)
		{
			sb.append("00");
		}
		else if (ticketID < 1000)
		{
			sb.append("0");
		}
		sb.append(ticketID);
		return sb.toString();
	}

	@Override
	public void updateTicket(Ticket ticket)
	{
		this.ticket = ticket;
		Platform.runLater(new Runnable()
		{

			@Override
			public void run()
			{
				System.out.println("run");
				gridPane.getChildren().clear();
				List<GenericPair<EChatSender, String>> chatHistory = ticket.getChatHistory();
				System.out.println(chatHistory.size());
				if (chatHistory.size() > 0)
				{
					for (int i = 0; i < chatHistory.size(); i++)
					{
						ChatMessage message = new ChatMessage(chatHistory.get(i).getRight());
						switch (chatHistory.get(i).getLeft())
						{
							case CLIENT:
								gridPane.add(message, 1, rowCounter);
								break;
							case SERVER:
								gridPane.add(message, 0, rowCounter);
								break;
						}
						rowCounter++;
					}
				}
			}
		});

	}

	@Override
	public int getTicketID()
	{
		return ticket.getTicketID();
	}

}
