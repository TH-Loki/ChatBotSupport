package ui.dialogs;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import bot.Ticket;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.css.CSSContainer;
import tools.css.ResourceLoader;
import ui.dialogs.helper.ActiveScene;
import ui.dialogs.interfaces.IMainContent;
import ui.dialogs.interfaces.ISceneChange;

public class UIPresenter implements ISceneChange
{
	private static UIPresenter uiPresenter = null;

	private Stage primaryStage;

	private Scene scene;

	private ActiveScene activeScene = ActiveScene.MAINVIEW;

	private MainViewDialog maindialog;

	List<IMainContent> viewList;

	private UIPresenter(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
	}

	public static UIPresenter createInstance(Stage primaryStage)
	{
		if (uiPresenter == null)
		{
			System.out.println("in if");
			uiPresenter = new UIPresenter(primaryStage);
		}
		return uiPresenter;
	}

	public static UIPresenter getInstance()
	{
		return uiPresenter;
	}

	public static void closeUI()
	{
		uiPresenter.primaryStage.close();
	}

	public static Stage getPrimaryStage()
	{
		return uiPresenter.primaryStage;
	}

	@Override
	public void setSceneOnPrimaryStage(Scene scene)
	{
		primaryStage.setScene(scene);
	}

	public void changeActiveScene(ActiveScene activeScene, int ticketID)
	{

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (viewList == null)
				{
					viewList = new ArrayList<IMainContent>();
				}
				UIPresenter.this.activeScene = activeScene;
				Scene scene = getCurrentScene(ticketID);
				if (scene != null)
				{
					primaryStage.setScene(scene);

					primaryStage.setOnShowing(new EventHandler<WindowEvent>()
					{
						@Override
						public void handle(WindowEvent event)
						{
							System.out.println("primaryStageonView");

						}
					});
				}

			}

		});
	}

	private Scene getCurrentScene(int ticketID)
	{
		CSSContainer cssContainer = null;
		try
		{
			ObservableList<CSSContainer> cssList = ResourceLoader.getGlobalCSSContainerList();
			if (cssList != null && cssList.size() > 0)
				cssContainer = cssList.get(0);

		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		switch (activeScene)
		{
			case MAINVIEW:
				if (maindialog == null)
				{
					maindialog = new MainViewDialog();
					viewList.add(maindialog);
					scene = new Scene(maindialog, 350, 500);
				}
				else
				{
					scene = maindialog.getScene();
				}
				break;
			case CHATVIEW:
				for (int i = 0; i < viewList.size(); i++)
				{
					if (viewList.get(i).getTicketID() == ticketID && viewList.get(i) instanceof ChatViewDialog)
					{
						if (((ChatViewDialog) viewList.get(i)).getScene() != null)
						{
							scene = ((ChatViewDialog) viewList.get(i)).getScene();
						}
						else
						{
							scene = new Scene((ChatViewDialog) viewList.get(i), 350, 500);
						}
					}
				}
		}
		if (cssContainer != null)
		{
			String value = cssContainer.getResourceFolder() + cssContainer.getFilename();
			File file = new File(value);
			try
			{
				scene.getStylesheets().add(file.toURI().toURL().toString());
				scene.setFill(Color.TRANSPARENT);
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return scene;
	}

	public void hideOrClosePrimaryStage()
	{
		UIPresenter.closeUI();
		System.exit(0);
	}

	public void updateTicket(Ticket ticket)
	{
		if (maindialog != null)
		{
			maindialog.updateTicket(ticket);
		}

		for (int i = 0; i < viewList.size(); i++)
		{
			if (ticket.getTicketID() == viewList.get(i).getTicketID())
			{
				viewList.get(i).updateTicket(ticket);
			}
		}
	}

	public static void changeChatView(boolean isNew, Ticket ticket)
	{
		if (isNew)
		{
			ChatViewDialog dialog = new ChatViewDialog(ticket);
			if (uiPresenter.viewList != null)
			{
				uiPresenter.viewList.add(dialog);
			}
		}
		else
		{
			uiPresenter.changeActiveScene(ActiveScene.CHATVIEW, ticket.getTicketID());
		}
	}

	public static void removeChatView(int ticketID)
	{
		for (int i = 0; i < uiPresenter.viewList.size(); i++)
		{
			if (uiPresenter.viewList.get(i).getTicketID() == ticketID)
			{
				uiPresenter.viewList.remove(i);
			}
		}
	}
}
