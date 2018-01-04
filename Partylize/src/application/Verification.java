package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * Created by Levin Averkamp & Tobias Goldlücke
 * On 15.11.2017
 * Last Edited by
 * On
 *
 * @editor Daniel Hecker : 24.12.2017
 */
public class Verification
{
	public static Stage startVerficationScreen(Stage stage)
	{
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

		Label keyInputLbl = new Label("Bitte gebe deinen Key ein");
		TextField keyInputField = new TextField();

		Button verificateBtn = new Button("verifizieren");

		//adding Content
		gridPane.add(keyInputLbl,0,0);
		gridPane.add(keyInputField, 1, 0);
		gridPane.add(verificateBtn,2,0);

		//On button press
		verificateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onVerifictaion(keyInputField);
			}
		});

		//On enter
		keyInputField.setOnKeyPressed(new EventHandler <KeyEvent>(){
			public void handle(KeyEvent key){
				if(key.getCode()== KeyCode.ENTER){
					onVerifictaion(keyInputField);
				}
			}
		});

		Scene scene = new Scene(gridPane, Color.GREEN);
		stage.setScene(scene);

		return stage;
	}

	/**
	 * Use this function whenever u want to make a verifictaion event
	 *
	 * @param key verification key
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private final static void onVerifictaion(TextField key)
	{
		if(ProfileManipulation.checkKey(Partylize.getUsername(), key.getText())){
			ProfileManipulation.updateVerfication(Partylize.getUsername(), true);
			Partylize.showMainScene();
		}
		else{
			System.out.println("Key ist falsch");
			Partylize.showLoginScene();
		}
	}
}
















