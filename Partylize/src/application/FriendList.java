package application;
/**
 * Author:	Felix Stanlein
 * created: 22.11.2017
 * last edited: 6.12.2017
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FriendList
{
	static boolean isOpen=false;
	static Stage friendListWindow;

	public static AnchorPane startFriendList(BorderPane mainWindow)
	{
		/* declaration and initialization */
		ListView<Profile> friendList = new ListView<Profile>();	// ListView to show the friends
		AnchorPane aPane = new AnchorPane();
		aPane.setMinHeight(mainWindow.getHeight()-300);
		aPane.setMaxHeight(mainWindow.getHeight()-300);
		aPane.minWidth(mainWindow.getWidth());
		ObservableList<Profile> items = FXCollections.observableArrayList ();
		items.addAll(ProfileManipulation.getFriends(true));		// Friendlist Test		// TODO import real friendlist
		final ContextMenu contextMenu = new ContextMenu();		// Contextmenu for click on friendlist
		MenuItem chatItem = new MenuItem("Open Chat");		// Chatitem to open chat
		MenuItem profileItem = new MenuItem("Show Profile");	// ProfileItem to open profile

		/* Add items and Contextmenu */
		contextMenu.getItems().addAll(chatItem,profileItem);
		friendList.setContextMenu(contextMenu);
		friendList.setItems(items);
		friendList.setId("friendList");

		/* Events for the contextmenu */
		setActions(profileItem, chatItem, friendList, mainWindow);

		aPane.getChildren().add(friendList);
		return aPane;
	}

	public static void setActions(MenuItem pItem, MenuItem cItem, ListView<Profile> friendList, BorderPane mainWindow)
	{
		pItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Profile selectedItem = friendList.getSelectionModel().getSelectedItem();
				mainWindow.setCenter(ProfilePage.startProfile(selectedItem.getId()));
			}
		});

		cItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent t)
			{
				Profile selectedItem = friendList.getSelectionModel().getSelectedItem();
				mainWindow.setCenter(ChatPage.startChat(selectedItem));
			}
		});
	}
}
