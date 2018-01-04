/**
 * @author Team 3, Viola (07.11.2017): Layout "Rezeptsuche nach Namen"
 * @author Team 3, Jakob, Nils (08.11.2017) : Added test "SearchRecipeByName"
 * @author Team 3: Jessica (19.12.2017): Replaced TextField with AutoCompleteTextField
 **/

package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RecipeSearchWindow extends Application 
{
	// Creating public variable
	public static String recipeName;
	
	public Scene recipeSearchNames(Stage stage) 
	{
		/**
		 * Creating components
		 */
		stage.setTitle("Search recipe by name");
		/**
		 * Creating components
		 */
		Label recipeSearchNameLabel = new Label("Search recipe by name");
		Label recipeNameLabel = new Label("Recipename");
		Label recipeOutputTemp = new Label();
		
		AutoCompleteTextField recipeNameField = new AutoCompleteTextField();
		ArrayList<String> names = RecipeManipulation.getRecipeNames();
		for(int i=0; i<names.size(); i++)
			recipeNameField.getEntries().add(names.get(i));
		
		//TextField recipeNameField = new TextField();
		Button searchBtn = new Button();
		searchBtn.setText("Search");
		// Button style
		searchBtn.setStyle("-fx-font: 14 Verdana; -fx-base: #1c6738; -fx-text-fill: #ffffff;");

		/**
		 * Search-Button Eventhandler
		 */
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TO DO Funktionalität Rezeptsuche
				String recipename = recipeNameField.getText();
				System.out.println("suche " + recipename);
				recipeName = recipeNameField.getText();
				recipeNameField.setText("");
				
				// !! TEMPORARILY ADDED FOR TESTING PURPOSES !!
				RecipeManipulation.searchRecipeByName(recipeName);
			}
		});

		/**
		 * Setting style for components
		 */
		// label
		recipeSearchNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		recipeSearchNameLabel.setTextFill(Color.web("#ffffff"));
		recipeNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		recipeNameLabel.setTextFill(Color.web("#ffffff"));
		recipeOutputTemp.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		recipeOutputTemp.setTextFill(Color.web("#ffffff"));
		// textfield
		recipeNameField.setStyle("-fx-background-color: #1c6738; -fx-text-inner-color: #ffffff;");
		recipeNameField.setFont(Font.font("Verdana", 16));

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
		gridPane.setStyle("-fx-background-color: #008000");

		// adding components and positioning
		gridPane.add(recipeSearchNameLabel, 1, 0);
		GridPane.setConstraints(recipeSearchNameLabel, 1, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		gridPane.add(recipeNameLabel, 1, 2);
		gridPane.add(recipeNameField, 2, 2);
		gridPane.add(searchBtn, 5, 5);
		gridPane.add(recipeOutputTemp, 2, 3);

		/**
		 * creating scene with background-color and adding to stage
		 */
		Scene scene = new Scene(gridPane, Color.GREEN);
		stage.setScene(scene);

		/**
		 * displaying content of stage
		 */
		stage.show();

		return scene;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
	}
}
