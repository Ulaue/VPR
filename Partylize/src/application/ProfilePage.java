package application;

import application.party.ExtendedFX;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Joscha Ziegeroski
 *
 */
public class ProfilePage {
	private static PasswordField txtPassword = new PasswordField();
	private static TextField txtEmail = new TextField();

	/**
	 *
	 * @param mainWindow
	 * @return
	 *
	 * @author Joscha Ziegeroski
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static GridPane startProfile(int userId) {
		AnchorPane profile = new AnchorPane();

		// Profile-Pic-Placeholder
		Rectangle picBox = new Rectangle(); // TODO sql profile pic + java
											// profile pic big
		picBox.setHeight(150.0);
		picBox.setWidth(150.0);
		picBox.setFill(Color.WHITE);
		picBox.setStroke(Color.BLACK);
		picBox.setStrokeWidth(2.0);

		// Labels
		Label lblName = new Label(ProfileManipulation.getUserName(userId, true));
		Label lblPassword = new Label("***");
		Label lblEmail = new Label(ProfileManipulation.getUserData(userId)[0]);

		// Button for stopping any changes
		Button btnStop = new Button("Abbrechen");
		btnStop.setMinSize(200, 25);
		ExtendedFX.setNodeVisible(btnStop, false);

		// Button for changing infos
		Button btnInfos = new Button("Informationen bearbeiten");
		btnInfos.setMinSize(200, 25);
		ExtendedFX.setNodeVisible(btnInfos, false);

		if(ProfileManipulation.getUserId(Partylize.getUsername(), true) == userId){
			ExtendedFX.setNodeVisible(btnInfos, true);
			changeInfos(btnInfos, btnStop, lblPassword, lblEmail, profile, userId);
		}

		// Event-Table
		Label lblRecentEvents = new Label("Veranstaltungen:");

		TableView eventTable = new TableView<String>(); // TODO sql and java add old-party

			TableColumn prevEvent = new TableColumn("Veranstaltung");
			prevEvent.setMinWidth(150);
			prevEvent.prefWidthProperty().bind(eventTable.widthProperty().divide(2));

			TableColumn ratingEvents = new TableColumn("Bewertung");
			ratingEvents.setMinWidth(150);
			ratingEvents.prefWidthProperty().bind(eventTable.widthProperty().divide(2));

		eventTable.setEditable(true);
		eventTable.getColumns().addAll(prevEvent, ratingEvents);

		VBox eventTableVbox = new VBox();
		eventTableVbox.setSpacing(10);
		eventTableVbox.setPadding(new Insets(10, 0, 0, 10));
		eventTableVbox.setMaxSize(450, 200);
		eventTableVbox.getChildren().addAll(lblRecentEvents, eventTable);

		// Recipe-Table
		Label lblRecipes = new Label("Rezepte:");

		TableView recipeTable = new TableView<String>(); // TODO sql and java add recipes

			TableColumn recipes = new TableColumn("Rezept");
			recipes.setMinWidth(150);
			recipes.prefWidthProperty().bind(eventTable.widthProperty().divide(2));

			TableColumn ratingRecipes = new TableColumn("Bewertung");
			ratingRecipes.setMinWidth(150);
			ratingRecipes.prefWidthProperty().bind(eventTable.widthProperty().divide(2));

			recipeTable.setEditable(true);
			recipeTable.getColumns().addAll(recipes, ratingRecipes);

		VBox recipeTableVbox = new VBox();
		recipeTableVbox.setSpacing(10);
		recipeTableVbox.setPadding(new Insets(10, 0, 0, 10));
		recipeTableVbox.setMaxSize(450, 200);
		recipeTableVbox.getChildren().addAll(lblRecipes, recipeTable);

		// Positioning
		AnchorPane.setLeftAnchor(picBox, 0.0);
		AnchorPane.setTopAnchor(picBox, 0.0);

		AnchorPane.setLeftAnchor(lblName, 205.0);
		AnchorPane.setTopAnchor(lblName, 0.0);

		AnchorPane.setLeftAnchor(lblPassword, 205.0);
		AnchorPane.setTopAnchor(lblPassword, 30.0);

		AnchorPane.setLeftAnchor(lblEmail, 205.0);
		AnchorPane.setTopAnchor(lblEmail, 60.0);

		AnchorPane.setLeftAnchor(btnInfos, 205.0);
		AnchorPane.setTopAnchor(btnInfos, 160.0);

		AnchorPane.setLeftAnchor(btnStop, 425.0);
		AnchorPane.setTopAnchor(btnStop, 160.0);

		AnchorPane.setLeftAnchor(eventTableVbox, 200.0);
		AnchorPane.setTopAnchor(eventTableVbox, 250.0);

		AnchorPane.setLeftAnchor(recipeTableVbox, 230.0 + eventTableVbox.getMaxWidth());
		AnchorPane.setTopAnchor(recipeTableVbox, 250.0);

		// Adding elements to pane
		profile.getChildren().addAll(picBox, lblName, lblPassword, lblEmail, btnInfos, btnStop, eventTableVbox, recipeTableVbox);

		GridPane gp = new GridPane();

		gp.add(profile, 0, 0);
		gp.setMinHeight(400);
		gp.setPrefHeight(600);
		gp.setMaxHeight(800);
		gp.setAlignment(Pos.CENTER);

		return gp;
	}

	/**
	 *
	 * @param btnInfos
	 * @param lblName
	 * @param lblInfo
	 * @param lblBirth
	 * @param profile
	 *
	 * @editor Daniel Hecker : 26.12.2017
	 */
	// Method for changing informations
	private static void changeInfos(Button btnInfos, Button btnStop, Label lblPassword, Label lblEmail, AnchorPane profile, int userId) {
		btnInfos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e)
			{
				if (btnInfos.getText() == "Informationen bearbeiten") {
					txtPassword.setText("");
					AnchorPane.setLeftAnchor(txtPassword, 305.0);
					AnchorPane.setTopAnchor(txtPassword, 30.0);
					lblPassword.setText("Password: ");

					txtEmail.setText(lblEmail.getText());
					AnchorPane.setLeftAnchor(txtEmail, 305.0);
					AnchorPane.setTopAnchor(txtEmail, 60.0);
					lblEmail.setText("E-Mail: ");

					profile.getChildren().addAll(txtPassword, txtEmail);

					ExtendedFX.setNodeVisible(btnStop, true);

					btnInfos.setText("Speichern");
				} else if (btnInfos.getText() == "Speichern") {
					//Update
					ProfileManipulation.changeEMail(txtEmail.getText());
					ProfileManipulation.changePassword(txtPassword.getText());

					//GUI update
					lblPassword.setText("Passwort wurde geändert");
					lblEmail.setText(ProfileManipulation.getUserData(userId)[0]);

					profile.getChildren().removeAll(txtPassword, txtEmail);

					ExtendedFX.setNodeVisible(btnStop, false);

					btnInfos.setText("Informationen bearbeiten");
				}
			}
		});

		btnStop.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e)
			{
				lblPassword.setText("***");
				lblEmail.setText(ProfileManipulation.getUserData(userId)[0]);

				profile.getChildren().removeAll(txtPassword, txtEmail);

				ExtendedFX.setNodeVisible(btnStop, false);

				btnInfos.setText("Informationen bearbeiten");
			}
		});
	}
}







