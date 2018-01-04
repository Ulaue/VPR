package application;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import application.party.Party;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Navigation {
	private static BorderPane center;
	private static Stage primaryStage;

	/**
	 * Authors: Marina Schmidt, Lukas Hornischer
	 *
	 * @param primaryStage
	 * @param center
	 * @return
	 */
	public static BorderPane startNavigation(Stage primaryStage, BorderPane center) {
		Navigation.center = center;
		Navigation.primaryStage = primaryStage;
		BorderPane navigation = new BorderPane();
		HBox navigationPointBar = new HBox(), partylizeIdentity = new HBox();
		AnchorPane toolbarTop = new AnchorPane();

		// placeholder for profilepic
		ImageView imgVwAppLogo = new ImageView();
		imgVwAppLogo.setImage(new Image("file:pictures/Logo.png"));
		imgVwAppLogo.setFitWidth(150);
		imgVwAppLogo.setPreserveRatio(true);
		imgVwAppLogo.setSmooth(true);
		imgVwAppLogo.setCache(true);
		imgVwAppLogo.setTranslateY(-20);
		///////////////////////////////////////

		// name of the app
		Label txtTitle = new Label("Partylize");
		txtTitle.getStyleClass().add("navigationTitle");
		txtTitle.setTranslateY(-10);
		///////////////////////////////////////

		////////////// LABEL INIT///////////////
		String[] labelText = { "PROFIL", "REZEPTSUCHE", "PARTY", "OPTIONEN" };
		String[] labelFunction = { "profile", "recipeSearch", "party", "options" };
		List<Label> lblNavigation = generateMenu(labelFunction, labelText);
		navigationPointBar.getChildren().addAll(lblNavigation);

		// label "emailadresse"
		Label lblUser = new Label(Partylize.getUsername());
		lblUser.getStyleClass().add("navigationHeader");
		lblUser.setTranslateY(-10);
		lblUser.setTranslateX(100);
		///////////////////////////////////////

		// label "lblLogout"
		Label lblLogout = new Label("ABMELDEN");
		lblLogout.getStyleClass().add("navigationHeader");
		lblLogout.setTranslateY(-10);
		lblLogout.setTranslateX(150);
		///////////////////////////////////////

		// configure navigation and set positions of everything
		navigation.setMinHeight(120);
		navigation.setMaxHeight(200);
		navigation.getStyleClass().add("navigation");

		navigation.setPadding(new Insets(25));
		AnchorPane.setRightAnchor(lblLogout, 160.0);
		AnchorPane.setRightAnchor(lblUser, 250.0);

		AnchorPane.setRightAnchor(toolbarTop, 0.0);
		AnchorPane.setLeftAnchor(txtTitle, 125.);
		AnchorPane.setTopAnchor(txtTitle, -20.);

		///////////////////////////////////////

		toolbarTop.getChildren().addAll(lblUser, lblLogout);

		navigation.setMinWidth(0.0);

		partylizeIdentity.getChildren().addAll(imgVwAppLogo, txtTitle);
		navigation.setTop(toolbarTop);
		navigation.setCenter(navigationPointBar);
		navigation.setLeft(partylizeIdentity);

		// lblLogout
		lblLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				showScene("logout");
			}
		});

		//lblUser
//		lblUser.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() // TODO disable
//		{
//			@Override
//			public void handle(MouseEvent event) {
//				showScene("profile");
//			}
//		});

		return navigation;
	}

	/**
	 *
	 * Generates a List of Labels that function as navigation points in the
	 * navigationPointBar each Label can be named individually aswell as recieve
	 * a custom function
	 *
	 * Author: Lukas Hornischer Date: 19.12.2017
	 *
	 * @param paneNames
	 *            Title of functions in the showScene Method. Is not visible to
	 *            the user;
	 * @param labelText
	 *            Text of the Label. Is visible to the user.
	 * @return
	 */
	private static List<Label> generateMenu(String[] paneNames, String[] labelText) {
		List<Label> navi = new LinkedList<Label>();
		if (paneNames.length == labelText.length) {
			for (int i = 0; i < paneNames.length; i++) {
				Label lblTmp = new Label(labelText[i]);
				lblTmp.getStyleClass().add("navigationHeader");
				String tmp = paneNames[i];

				EventHandler<MouseEvent> tmpEvent = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						showScene(tmp);
					}
				};
				lblTmp.addEventHandler(MouseEvent.MOUSE_CLICKED, tmpEvent);
				navi.add(lblTmp);
				HBox.setMargin(lblTmp, new Insets(50, 20, 0, 25));
			}
		}
		return navi;
	}

	/**
	 * Selection of the currently shown center pane
	 *
	 * Edit Notes: Add a string to labelFunctions in the startNavigation Method.
	 * Add the same String to the Switch case selection and add your Pane to the
	 * static center pane;
	 *
	 * Author: Lukas Hornischer Date: 19.12.2017
	 *
	 * @param paneName
	 *            Input String for the selection of a center Pane;
	 */
	public static void showScene(String paneName) {
		switch (paneName) {
			case "logout": {
				Partylize.showLogoutScene();
				break;
			}
			case "profile": {
				center.setCenter(ProfilePage.startProfile(ProfileManipulation.getUserId(true)));
				center.setRight(null);
				break;
			}
			case "recipeSearch": {
				center.setCenter(RecipeSearch.startRecipeSearch(center));
				center.setRight(null);
				break;
			}
			case "party": {
				center.setCenter(Party.create());
				center.setRight(null);
				break;
			}
			case "options": {
				try {
					// center.getStylesheets().add((getClass().getResource("optionButtons.css")).toExternalForm());
					center.setCenter(OptionsScreen.startOption(primaryStage, center));
					center.setRight(null);
				} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
}