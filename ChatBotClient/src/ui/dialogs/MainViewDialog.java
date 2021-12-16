package ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import bot.Ticket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import logic.connection.Connection;
import ui.dialogs.interfaces.IMainContent;
import ui.tools.ImageLoader;
import ui.tools.UITools;

public class MainViewDialog extends BorderPane implements IMainContent
{
	
	private class ChatPanel extends GridPane
	{
		private Button delete;
		private Label ticketTitle, lastText;
		private Ticket ticket;
		private GridPane gridPane;
		
		public ChatPanel(Ticket ticket)
		{
			this.ticket = ticket;
			gridPane = this;
			gridPane.setPadding(new Insets(5,5,5,5));
			gridPane.setHgap(5);
			
			ColumnConstraints columnText = new ColumnConstraints();
			ColumnConstraints columnButton = new ColumnConstraints();
			
			RowConstraints rowTitle = new RowConstraints();
			rowTitle.setValignment(VPos.TOP);
			
			RowConstraints rowText = new RowConstraints();
			rowText.setValignment(VPos.BOTTOM);
			
			ticketTitle = new Label();
			ticketTitle.setId("label_colorized_text");
			gridPane.setHalignment(ticketTitle, HPos.LEFT);
			gridPane.add(ticketTitle, 0, 0);
			
			lastText = new Label();
			lastText.setId("label_colorized_text");
			lastText.setText(ticket.getLastText());
			gridPane.setHalignment(lastText, HPos.LEFT);
			gridPane.add(lastText, 0, 1);
			
			delete = new Button();
			delete.setGraphic(new ImageView(ImageLoader.getIconImage("exitkl")));
			delete.setOnAction(new EventHandler<ActionEvent>()
			{
				
				@Override
				public void handle(ActionEvent event)
				{
					//TODO schließe Ticket bei Bot
					if(vBox.getChildren().contains(ChatPanel.this))
					{
						ticketList.remove(getTicket());
						chatList.remove(ChatPanel.this);
						vBox.getChildren().remove(ChatPanel.this);
						UIPresenter.removeChatView(ChatPanel.this.getTicket().getTicketID());
						Connection.deleteTicket(ChatPanel.this.getTicket().getTicketID());
					}
				}

			});
			gridPane.add(delete, 1, 0);
			gridPane.getColumnConstraints().addAll(columnText, columnButton);
			gridPane.getRowConstraints().addAll(rowTitle, rowText);
			
			gridPane.setStyle("-fx-border-width: 0.5;" + "-fx-border-color: #000000;");
			
			update(ticket);
			this.setOnMousePressed(new EventHandler<Event>()
			{

				@Override
				public void handle(Event event)
				{
					UIPresenter.changeChatView(false, ticket);
				}
			});
		}
		
		public Ticket getTicket()
		{
			return ticket;
		}
		
		public void update(Ticket ticket)
		{
			Platform.runLater(new Runnable()
			{
				
				@Override
				public void run()
				{
					int ticketID = ticket.getTicketID();
					StringBuilder sb = new StringBuilder();
					sb.append("Ticket #");
					if(ticketID < 10)
					{
						sb.append("000");
					}
					else if(ticketID < 100)
					{
						sb.append("00");
					}
					else if(ticketID < 1000)
					{
						sb.append("0");
					}
					sb.append(ticketID);
					ticketTitle.setText(sb.toString());
					lastText.setText(ticket.getLastText());
				}
			});
		}
		
	}
	
	private VBox vBox;
	private HBox addBox, topBox;
	private Button add;
	private ScrollPane scrollPane;
	private List<ChatPanel> chatList;
	private List<Ticket> ticketList;
	
	public MainViewDialog()
	{
		super();
		this.setId("default_scene");
		chatList = new ArrayList<ChatPanel>();
		ticketList = new ArrayList<Ticket>();
		HBox hBox = new HBox();
		//ein bisschen weier runter damit es ungefähr auf einer Höhe mit den WindowToolPane ist
		hBox.setPadding(new Insets(5, 0,0,0));
		
		WindowToolPane wtp = new WindowToolPane(UIPresenter.getPrimaryStage(), true, true, false);
		
		topBox = new HBox();
		topBox.setPadding(new Insets(5,5,5,5));
		topBox.getChildren().addAll(hBox, UITools.createHorizontalSpacer(), wtp);
		this.setTop(topBox);
		
		vBox = new VBox();
		vBox.setSpacing(3);
		vBox.setPadding(new Insets(5,5,5,5));
		vBox.setAlignment(Pos.CENTER);
		
		addBox = new HBox();
		add = new Button("");
		add.setGraphic(new ImageView(ImageLoader.getIconImage("baseline_add_black_18dp.png")));
		add.setId("roundButton");
		add.setOnAction(new EventHandler<ActionEvent>()
		{
			
			@Override
			public void handle(ActionEvent event)
			{
				Connection.addNewChat();
			}
			
		});
		
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		
		addBox.getChildren().addAll(UITools.createHorizontalSpacer(), add);
		vBox.getChildren().add(addBox);
		
		scrollPane = new ScrollPane();
		scrollPane.setContent(vBox);
		scrollPane.setFitToWidth(true);
		
		this.setCenter(scrollPane);
	}
	
	public void createNewTicket(Ticket ticket)
	{
		ChatPanel panel = new ChatPanel(ticket);
		chatList.add(panel);
		panel.update(ticket);
		vBox.getChildren().add(panel);
		UIPresenter.changeChatView(true, ticket);
	}

	@Override
	public void updateTicket(Ticket ticket)
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				boolean existing = false;
				for(int i = 0; i < chatList.size(); i++)
				{
					if(chatList.get(i).getTicket().getTicketID() == ticket.getTicketID())
					{
						chatList.get(i).update(ticket);
						existing = true;
					}
				}
				if(!existing)
				{
					createNewTicket(ticket);
				}
			}
			
		});
		
	}

	@Override
	public int getTicketID()
	{
		return 0;
	}
}
