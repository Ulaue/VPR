/**
 *	@author Team2, Robert
 *	Layout "MainScreen"
 */

package application;

import application.party.Compartment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainScreen
{
	private static AnchorPane notifications;
	private static Button btnFriendList;

	private static BorderPane window;

	/**
	 *
	 * @param primaryStage stage
	 * @return stage
	 *
	 * @author Robert
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	public static Stage startMainScene(Stage primaryStage)
	{
		// Window is the full scene in Partylise
		window = new BorderPane();

		// Navigation is the top dark bar, where the user can click on buttons for the different pages
		BorderPane navigation = Navigation.startNavigation(primaryStage, window);
		navigation.setMinHeight(200);
		navigation.setMaxHeight(200);

		// Bottom is the bottom dark bar, for the notification and friend list
		// @editor Daniel Hecker : 24.12.2017
		AnchorPane bottom = Compartment.newCompartment(new Compartment<AnchorPane>()
		{
			@Override
			public AnchorPane addCompartment()
			{
				AnchorPane apBottom = new AnchorPane();
				notifications = NotificationBar.createBenachrichtigungsleiste();
				btnFriendList = new Button("Freundesliste");
				apBottom.getChildren().addAll(notifications, btnFriendList);
				apBottom.setId("bottom");
				apBottom.setMinHeight(100);
				apBottom.setMaxHeight(100);

				// Setting anchors for the bottom pane in the main window pane
				AnchorPane.setLeftAnchor(notifications, 35.0);
				AnchorPane.setRightAnchor(btnFriendList, 35.0);
				AnchorPane.setTopAnchor(btnFriendList, 35.0);

				return apBottom;
			}
		});

		window.setCenter(ProfilePage.startProfile(ProfileManipulation.getUserId(true)));	// TODO if a real start screen exists, change this to that
		window.setTop(navigation);
		window.setBottom(bottom);
		window.setId("main");

		btnFriendList.setOnAction(new EventHandler<ActionEvent>()	// TODO bad solution
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(window.getRight() == null)
				{
					window.setRight(FriendList.startFriendList(window));
					window.setCenter(null);
				}
				else
					window.setRight(null);
			}
		});

		// Getting css layout for Partylize
		window.getStylesheets().add(Partylize.class.getResource("application.css").toExternalForm());

		Scene scene = new Scene(window, 1600, 900);	// TODO set back to 1920 : 1080
		primaryStage.setScene(scene);

		return primaryStage;
	}

	/**
	 * Width of window
	 *
	 * @return Width in float
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static float getQuadWidth()
	{
		float quarterWidth = (float)(window.getWidth() / 4);

		return quarterWidth;
	}

	/**
	 * Height of center part of window
	 *
	 * @return Height in float
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static float getQuadHeight()
	{
		float quarterHeight = (float)(window.getHeight() - 300);

		return quarterHeight;
	}
}




























