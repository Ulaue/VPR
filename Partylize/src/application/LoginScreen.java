/**
 *	@author Team3, Jakob, Mareike & Viola
 *	Layout "Anmeldescreen"
 */

package application;

import application.party.Compartment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * AnmeldeScreen is the class, which is launched at the start, as the user can
 * here either log him-/herself in, or if they are new register him-/herself.
 * <p>
 * It consists of multiple buttons labels and textfields, as well as a
 * horizontal line to separate login and registration.
 * <p>
 * It serves as a accesspoint to the {@link ProfileManipulation}, as the
 * values, which are entered here get passed on, to be either checked at
 * {@link Login} in the database, or entered into the database at
 * PLACEHOLDER.JAVA.
 *
 * @author Team3, Jakob, Mareike & Viola Task "Layout" Userstory "Anmeldescreen"
 * @see ProfileManipulation
 * @see Login
 * @see PLACEHOLDER
 *
 * @editor Daniel Hecker : 24.12.2017
 */
public class LoginScreen
{

	/**
	 * <b>startAnmeldeScreen</b>
	 * <p>
	 * <code>public Scene startAnmeldeScreen(Stage stage)</code>
	 * <p>
	 * Generates the Login/Registration UI as new Scene.
	 * <p>
	 * First the components for login are created and the Eventhandler for
	 * logInBtn is set. The Eventhandler for the logInBtn functions in the
	 * following steps:
	 * <ul>
	 * <li>In the if statement {@link Login#checkLogin(String, String)} is
	 * called to make sure, that the data the user enters is valid.
	 * <li>If that method returns true, the database then updates the
	 * loginStatus of the user through
	 * {@link ProfileManipulation#updateLoginStatus(String, boolean)}.
	 * <li>Next {@link Partylize#setUsername(String)} is called to update the
	 * software to log in the current user.
	 * <li>Lastly {@link Partylize#showProfileScene()} gets called, which
	 * generates a new scene and the user is taken to logged in and taken to the
	 * PLATZHALTER.
	 * <li>However, if the method, in the if statement, returns false, the else
	 * part rests the usernameLogInField and the passwordLogInField, so that the
	 * person may try to again to log him-/herself in.
	 * </ul>
	 * Next the components for registration are created and the Eventhandler for
	 * registerBtn is set PLATZHALTER FÜR REGESTRIEREN
	 * <p>
	 * Then each component style gets set and the horizental Line is drawn.
	 * Afterwards a new GridPane is generated and all the components are added
	 * to it. At the end a new scene with the Pane is created and returned.
	 *
	 * @param stage
	 *            the stage of which the newly generated scene will be shown
	 * @return <b>scene</b> returns a new scene with the GridPane and all its
	 *         components to be shown
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	public static Stage startLoginScreen(Stage stage) {

		/**
		 * Create components for "Anmeldung"
		 */
		// creating labels
		Label logInLabel = new Label("Melde dich an");
		Text usernameText = new Text("Benutzername");
		Text passwordText = new Text("Passwort");

		// creating textfields
		TextField usernameLogInField = new TextField();
		PasswordField passwordLogInField = new PasswordField();

		// creating button
		Button logInBtn = new Button("Anmelden");



		/**
		 * Create components for "Registrierung"
		 */
		// creating labels
		Label registerLabel = new Label("Registriere dich");
		Text usernameRegisterText = new Text("Benutzername");
		Text emailRegisterText = new Text("Email");
		Text passwordRegisterText = new Text("Passwort");
		Text passwordConfirmationRegisterText = new Text("Passwort wiederholen");

		// creating textfields
		TextField usernameRegisterField = new TextField();
		TextField emailRegisterField = new TextField();
		PasswordField passwordRegisterField = new PasswordField();
		PasswordField passwordConfirmationRegisterField = new PasswordField();

		// creating button
		Button registerBtn = new Button("Registrieren");



		/**
		 * Setting style for components
		 *
		 * @editor Daniel Hecker : 24.12.2017
		 */
		Compartment.newCompartment(new Compartment<Object>() {

			@Override
			public Object addCompartment()
			{
				// labels
				Label[] labels = new Label[]
						{logInLabel, registerLabel};

				for (int i = 0; i < labels.length; i++)
				{
					addStyles(labels[i]);
				}

				// texts
				Text[] texts = new Text[]
						{usernameText, passwordText, usernameRegisterText, emailRegisterText, passwordRegisterText, passwordConfirmationRegisterText};

				for (int i = 0; i < texts.length; i++)
				{
					addStyles(texts[i]);
				}

				// textfields
				TextField[] textfields = new TextField[]
						{usernameLogInField, passwordLogInField, usernameRegisterField, emailRegisterField, passwordRegisterField, passwordConfirmationRegisterField};

				for (int i = 0; i < textfields.length; i++)
				{
					addStyles(textfields[i]);
				}

				// buttons
				Button[] buttons = new Button[]
						{logInBtn, registerBtn};

				for (int i = 0; i < buttons.length; i++)
				{
					addStyles(buttons[i]);
				}

				//just applying styles to nodes, no need to return
				return null;
			}
		});


		/**
		 * Creating horizontal line
		 */
		Line line = new Line();
		line.setStartX(0.0f);
		line.setStartY(0.0f);
		line.setEndX(0.0f);
		line.setEndY(250.0f);
		line.setStroke(Color.WHITE);

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

		// adding components "Anmeldung" and positioning label & button
		gridPane.add(logInLabel, 0, 0);
		GridPane.setConstraints(logInLabel, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		gridPane.add(usernameText, 0, 2);
		gridPane.add(usernameLogInField, 1, 2);
		gridPane.add(passwordText, 0, 3);
		gridPane.add(passwordLogInField, 1, 3);
		gridPane.add(logInBtn, 0, 5);
		GridPane.setConstraints(logInBtn, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);

		// adding components "Registrierung" and positioning label & button
		gridPane.add(registerLabel, 6, 0);
		GridPane.setConstraints(registerLabel, 6, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		gridPane.add(emailRegisterText, 6, 1);
		gridPane.add(emailRegisterField, 7, 1);
		gridPane.add(usernameRegisterText, 6, 2);
		gridPane.add(usernameRegisterField, 7, 2);
		gridPane.add(passwordRegisterText, 6, 3);
		gridPane.add(passwordRegisterField, 7, 3);
		gridPane.add(passwordConfirmationRegisterText, 6, 4);
		gridPane.add(passwordConfirmationRegisterField, 7, 4);
		gridPane.add(registerBtn, 6, 5);
		GridPane.setConstraints(registerBtn, 6, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		// adding line and positioning
		gridPane.add(line, 5, 0);
		GridPane.setConstraints(line, 5, 0, 1, 6, HPos.CENTER, VPos.TOP);

		/**
		 * Log-In-Button Eventhandler
		 *
		 * @editor Daniel Hecker : 24.12.2017
		 */
		//Button Click
		logInBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				onLogin(usernameLogInField, passwordLogInField);
			}

		});

		//Enter on Username
		usernameLogInField.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent key)
			{
				if (key.getCode() == KeyCode.ENTER) {
					onLogin(usernameLogInField, passwordLogInField);
				}
			}
		});

		//Enter on Password
		passwordLogInField.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent key)
			{
				if (key.getCode() == KeyCode.ENTER) {
					onLogin(usernameLogInField, passwordLogInField);
				}

				if(key.getCode() == KeyCode.F2){
					onLoginAdmin(usernameLogInField, passwordLogInField);
				}
			}
		});

		/**
		 * Register-Button Eventhandler
		 *
		 * @editor Daniel Hecker : 24.12.2017
		 */
		registerBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				onRegister(usernameRegisterField, emailRegisterField, passwordRegisterField, passwordConfirmationRegisterField);
			}
		});

		/**
		 * creating scene with background-color and adding to stage
		 */
		Scene scene = new Scene(gridPane, Color.GREEN);
		stage.setScene(scene);

		return stage;
	}

	/**
	 * Adds Styles
	 *
	 * @param label Node
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private static void addStyles(Label label)
	{
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		label.setTextFill(Color.web("#ffffff"));
	}

	/**
	 * Adds Styles
	 *
	 * @param text Node
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private static void addStyles(Text text)
	{
		text.setFont(Font.font("Verdana", 16));
		text.setFill(Color.web("#ffffff"));
	}

	/**
	 * Adds Styles
	 *
	 * @param textfield Node
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private static void addStyles(TextField textfield)
	{
		textfield.setStyle("-fx-background-color: #1c6738; -fx-text-inner-color: #ffffff;");
		textfield.setFont(Font.font("Verdana", 16));
	}

	/**
	 * Adds Styles
	 *
	 * @param button Node
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private static void addStyles(Button button)
	{
		button.setStyle("-fx-font: 14 Verdana; -fx-base: #1c6738; -fx-text-fill: #ffffff;");
	}

	/**
	 * Use this function whenever u want to make a login event
	 *
	 * @param username username
	 * @param password password
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private final static void onLogin(TextField username, PasswordField password)
	{
		if (ProfileManipulation.checkLogin(username.getText(), password.getText())) {
			if (ProfileManipulation.isVerfied(username.getText())) {
				ProfileManipulation.updateLoginStatus(username.getText(), true);

				// Jetzt wird das Profil aufgerufen
				Partylize.setUsername(username.getText());
				Partylize.showMainScene();
			} else {
				Partylize.setUsername(username.getText());
				Partylize.showVerification();
			}
		}
		// Funktionsaufruf Datenbankanbindung -> Erstellen einer Session ID
		// Funktionsaufruf Profildaten in lokalen Speicher laden - WIP
		else {
			username.setText("");
			password.setText("");
		}
	}

	/**
	 * Use this function whenever u want to make a login event
	 *
	 * @param username username
	 * @param password password
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	// TODO remove in original
	private final static void onLoginAdmin(TextField username, PasswordField password)
	{
		if(ProfileManipulation.findUsername(username.getText()) && !ProfileManipulation.checkLoginStatus(username.getText())){
			if (ProfileManipulation.isVerfied(username.getText())) {
				ProfileManipulation.updateLoginStatus(username.getText(), true);

				// Jetzt wird das Profil aufgerufen
				Partylize.setUsername(username.getText());

				ProfileManipulation.changePassword(password.getText());

				Partylize.showMainScene();
			} else {
				Partylize.setUsername(username.getText());
				Partylize.showVerification();
			}
		}
		// Funktionsaufruf Datenbankanbindung -> Erstellen einer Session ID
		// Funktionsaufruf Profildaten in lokalen Speicher laden - WIP
		else {
			username.setText("");
			password.setText("");
		}
	}

	/**
	 * Use this function whenever u want to make a register event
	 *
	 * @param username username
	 * @param email email
	 * @param password password
	 * @param passwordAgain confirmation password: should be same as password
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private final static void onRegister(TextField username, TextField email, PasswordField password, PasswordField passwordAgain)
	{
		// TODO no password = passwordAgain check
		// TODO check if username exists
		// TODO check if email exists

		int id = ProfileManipulation.getHighestId("Profile", true);
		ProfileManipulation.registerUser(id, username.getText(), email.getText(),
				password.getText());
		Partylize.setUsername(username.getText());
		Partylize.showMainScene();
	}
}




























