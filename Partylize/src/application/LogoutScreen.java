/**
 *	@author Team3, Jakob, Mareike & Viola
 *	Layout "Abmeldescreen"
 */

package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LogoutScreen
{
	public static Stage startLogoutScreen(Stage stage)
	{
		// creating labels
		Label logoutLabel = new Label("Abmelden?");

		// creating button
		Button logoutBtn = new Button("Bestätigen");
		Button logoutCancelBtn = new Button("Zurück");

		/**
		 * Logout-Button Eventhandler
		 */
		logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ProfileManipulation.updateLoginStatus(Partylize.getUsername(), false);
				Partylize.setUsername("");
				Partylize.showLoginScene();
			}
		});

		/**
		 * Logout-Cancel-Button Eventhandler
		 */
		logoutCancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Partylize.showMainScene();
			}
		});

		/**
		 * Setting style for components
		 */
		// texts
		logoutLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		logoutLabel.setTextFill(Color.web("#ffffff"));
		// buttons
		logoutBtn.setStyle("-fx-font: 14 Verdana; -fx-base: #1c6738; -fx-text-fill: #ffffff;");
		logoutCancelBtn.setStyle("-fx-font: 14 Verdana; -fx-base: #1c6738; -fx-text-fill: #ffffff;");

		/**
		 * Creating gridPane, editing its settings and adding components
		 */
		// Creating gridPane
		GridPane gridPane = new GridPane();
		// editing settings
		// minimum size
		gridPane.setMinSize(400, 200);
		// padding
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		// vertical and horizontal gaps between the columns
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		// alignment
		gridPane.setAlignment(Pos.CENTER);
		// setting style
		gridPane.setStyle("-fx-background-color: #1f945c");

		// adding components "Abmelden" and positioning label & button
		gridPane.add(logoutLabel, 0, 0);
		GridPane.setConstraints(logoutLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		gridPane.add(logoutBtn, 0, 0);
		GridPane.setConstraints(logoutBtn, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		gridPane.add(logoutCancelBtn, 0, 0);
		GridPane.setConstraints(logoutCancelBtn, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);

		/**
		 * creating scene with background-color and adding to stage
		 */
		// Scene scene = new Scene(gridPane);
		Scene scene = new Scene(gridPane, Color.GREEN);
		stage.setScene(scene);

		return stage;
	}
}






























