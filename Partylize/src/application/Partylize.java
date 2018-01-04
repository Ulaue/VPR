package application;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Starts the login screen and provides methods to methods to open other stages
 *
 * @editor Daniel Hecker : 24.12.2017
 */
public class Partylize extends Application {

	private static String username;
	private static Stage primaryStage;

	public static void main(String[] args)
	{
		username = "";
		launch(args);
	}

	/**
	 * @editor Daniel Hecker : 24.12.2017
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		primaryStage = stage;
		primaryStage.setTitle("Partylize");
		primaryStage.getIcons().add(new Image("file:pictures/Logo.png"));
		primaryStage.setOnCloseRequest(event ->
			{
				ProfileManipulation.updateLoginStatus(Partylize.getUsername(), false);
				Partylize.setUsername("");
			});
		showLoginScene();
	}

	/**
	 * Opens Main Screen
	 *
	 * Logs User Out upon hitting close-button
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	public static void showMainScene()
	{
		primaryStage = MainScreen.startMainScene(primaryStage);
		showStage();
	}

	/**
	 * Opens Login Screen
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	public static void showLoginScene()
	{
		primaryStage = LoginScreen.startLoginScreen(primaryStage);
		showStage();
	}

	/**
	 * Opens Logout Screen
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	public static void showLogoutScene()
	{
		primaryStage = LogoutScreen.startLogoutScreen(primaryStage);
		showStage();
	}

	/**
	 * Opens Verification Screen
	 *
	 * @editor Daniel Hecker : 24.12.2017
	 */
	public static void showVerification()
	{
		primaryStage = Verification.startVerficationScreen(primaryStage);
		showStage();
	}

	/**
	 * static get
	 * @return username
	 */
	public static String getUsername()
	{
		return username;
	}

	/**
	 * static set
	 * @param username new username
	 */
	public static void setUsername(String username)
	{
		Partylize.username = username;
	}

	/**
	 * Shows and centers the primaryStage on screen
	 *
	 * @author Daniel Hecker : 24.12.2017
	 */
	private static void showStage()
	{
		primaryStage.show();
		primaryStage.centerOnScreen();
	}
}





















