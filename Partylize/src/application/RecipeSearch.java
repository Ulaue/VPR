package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import java.util.ArrayList;

/**
 *	@author Team 2, Robert
 *  + method in "RecipeManipulation" to search recipes
 *	JavaFx pane to show the result of the recipe search
 */
public class RecipeSearch
{
	private static ArrayList<Recipes> recipeArrayList;

	@SuppressWarnings("unchecked")
	public static HBox startRecipeSearch(BorderPane mainWindow)
	{

		// HBox order all child panes horizontal
    	HBox hBox = new HBox();
    		// SearchBar is the first Pane on the left side of the recipeSearch, RecipeDroppingPanes will follow on the right
        	Pane searchBar = new Pane();
				Label lblSearch = new Label("Suche nach:				(mit \", \" trennen)");
					lblSearch.setMinWidth(300);
					lblSearch.setAlignment(Pos.CENTER);
				Button btnStartSearch = new Button("Suche starten");
					btnStartSearch.setMinWidth(305);
				TextField txfSearch= new TextField();
					txfSearch.setMinWidth(308);

				// Table to show the found recipes with their ingredients
				TableView<Recipes> tbvRecipes = new TableView<>();
					TableColumn<Recipes, String> tbcRecipes = new TableColumn<Recipes, String>("Cocktails");
						tbcRecipes.setMinWidth(150);
						tbcRecipes.setResizable(false);
						tbcRecipes.getStyleClass().add("tableColumnRecipes");
						tbcRecipes.setStyle("-fx-border-width:2px;-fx-border-color:black;-fx-border-style:hidden dotted solid hidden;");
				    TableColumn<Recipes, String> tbcIngredients = new TableColumn<Recipes, String>("Zutaten");
				    	tbcIngredients.setMinWidth(150);
				    	tbcIngredients.setResizable(false);
				    	tbcIngredients.setSortable(false);
				    	tbcIngredients.getStyleClass().add("tableColumnRecipes");
				    	tbcIngredients.setStyle("-fx-border-width:2px;fx-border-color:black;-fx-border-style:hidden hidden solid hidden;");
				tbvRecipes.getColumns().addAll(tbcRecipes, tbcIngredients);
			    tbvRecipes.setPlaceholder(new Label("Suche nach einem Rezeptname oder Zutaten."));
			    tbvRecipes.setMinHeight(mainWindow.getHeight()-420);
			    tbvRecipes.setMaxHeight(mainWindow.getHeight()-420);
			    tbvRecipes.setId("tableViewRecipes");
			    tbvRecipes.setStyle("-fx-border-width:2px;-fx-border-color:black;");
			    tbvRecipes.setMinWidth(300);
			searchBar.getChildren().addAll(lblSearch, btnStartSearch, txfSearch, tbvRecipes);
			searchBar.setMinWidth(310);
		hBox.getChildren().add(searchBar);
		hBox.setPadding(new Insets(10));
		hBox.setMinHeight(mainWindow.getHeight()-300);
		hBox.setMaxHeight(mainWindow.getHeight()-300);
		hBox.setSpacing(10);

		// EventHandler that reacts if recipes start dragging from the tableview
		tbvRecipes.setOnDragDetected(event ->
		{
			if(!tbvRecipes.getSelectionModel().isEmpty())
			{
		        String tmp = tbvRecipes.getSelectionModel().getSelectedItem().toString();
		        Dragboard db = tbvRecipes.startDragAndDrop(TransferMode.MOVE);
		        ClipboardContent content = new ClipboardContent();
		        content.putString(tmp);
		        db.setContent(content);
			}
		    event.consume();
	    });

		// EventHandler that reacts if recipes are draggedOver the recipe panes to enable droppable
		EventHandler<DragEvent> dragOver = new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent event)
			{
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			    event.consume();
			}
		};

		// EventHandler that reacts if recipes are dropped on the recipe panes
		EventHandler<DragEvent> dragDropped = new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent event)
			{
				Dragboard db = event.getDragboard();

				Recipes recipe = null;
				if(recipeArrayList != null && db.getString() != "")
					for (int i = 0; i < recipeArrayList.size(); i++)
						if(recipeArrayList.get(i).getRecipeName().equals(db.getString()))
						{
							recipe = recipeArrayList.get(i);
							break;
						}

				if(recipe != null)	//TODO doesn't work
				{
					// Writing content in labels and set them visible
					((Label) ((Pane) event.getSource()).getChildren().get(0)).setText(recipe.getRecipeName());
					((Label) ((Pane) event.getSource()).getChildren().get(2)).setText(recipe.getIngredient());
					((Label) ((Pane) event.getSource()).getChildren().get(3)).setText(recipe.getAmount());
					((Label) ((Pane) event.getSource()).getChildren().get(5)).setText(recipe.getDescription());
					((Label) ((Pane) event.getSource()).getChildren().get(1)).setVisible(true);
					((Label) ((Pane) event.getSource()).getChildren().get(2)).setVisible(true);
					((Label) ((Pane) event.getSource()).getChildren().get(3)).setVisible(true);
					((Label) ((Pane) event.getSource()).getChildren().get(4)).setVisible(true);
					((Label) ((Pane) event.getSource()).getChildren().get(5)).setVisible(true);
				}
			    event.setDropCompleted(true);
			    event.consume();
			}
	    };

	    // Number of Panes for Recipes change with the resolution width of the primary Window
	    int recipePanes = 3;
//	    if(mainWindow.getWidth() < 1801)	//TODO reactive + fix
//	    	recipePanes = 4;
//	    if(mainWindow.getWidth() < 1501)
//	    	recipePanes = 3;
//	    if(mainWindow.getWidth() < 1201)
//	    	recipePanes = 2;
//	    if(mainWindow.getWidth() < 901)
//	    	recipePanes = 1;

	    // Creating Panes to Show Recipes dragged into them from the tableView
		for (int i = 0; i < recipePanes; i++)
		{
			// Creating a pane with labels to show a formatted recipe with more information
			// and disabling not needed labels first
			AnchorPane recipePane = new AnchorPane();
				Label lblName = new Label("Rezept hierher ziehen");
					lblName.setMinWidth(300);
					lblName.setAlignment(Pos.CENTER);
					lblName.setFont(new Font(25));
					lblName.setUnderline(true);
					lblName.setStyle("-fx-text-fill:white;");
				Label lblHeader1 = new Label("Zutaten:");
					lblHeader1.setFont(new Font(20));
					lblHeader1.setVisible(false);
					lblHeader1.setUnderline(true);
					lblHeader1.setStyle("-fx-text-fill:white;");
				Label lblIngredients = new Label("Zutaten:");
					lblIngredients.setFont(new Font(15));
					lblIngredients.setAlignment(Pos.CENTER_RIGHT);
					lblIngredients.setVisible(false);
					lblIngredients.setStyle("-fx-text-fill:white;");
				Label lblAmount = new Label();
					lblAmount.setFont(new Font(15));
					lblAmount.setVisible(false);
					lblAmount.setStyle("-fx-text-fill:white;");
				Label lblHeader2 = new Label("Beschreibung:");
					lblHeader2.setFont(new Font(20));
					lblHeader2.setVisible(false);
					lblHeader2.setUnderline(true);
					lblHeader2.setStyle("-fx-text-fill:white;");
				Label lblDescription= new Label();
					lblDescription.setFont(new Font(15));
					lblDescription.setMaxWidth(300);
					lblDescription.setWrapText(true);
					lblDescription.setVisible(false);
					lblDescription.setStyle("-fx-text-fill:white;");

			recipePane.getChildren().addAll(lblName, lblHeader1, lblIngredients, lblAmount, lblHeader2, lblDescription);
			recipePane.setMinHeight(mainWindow.getHeight()-330);
			recipePane.setMaxHeight(mainWindow.getHeight()-330);
			recipePane.setMinWidth(315);
			recipePane.setId("recipePane");
			recipePane.setOnDragOver(dragOver);
			recipePane.setOnDragDropped(dragDropped);
			AnchorPane.setTopAnchor(lblName, 0.0);
			AnchorPane.setTopAnchor(lblHeader1, 30.0);
			AnchorPane.setLeftAnchor(lblHeader1, 10.0);
			AnchorPane.setTopAnchor(lblIngredients, 55.0);
			AnchorPane.setRightAnchor(lblIngredients, 115.0);
			AnchorPane.setLeftAnchor(lblIngredients, 10.0);
			AnchorPane.setTopAnchor(lblAmount, 55.0);
			AnchorPane.setLeftAnchor(lblAmount, 200.0);
			AnchorPane.setTopAnchor(lblHeader2, 200.0);
			AnchorPane.setLeftAnchor(lblHeader2, 10.0);
			AnchorPane.setTopAnchor(lblDescription, 230.0);
			AnchorPane.setLeftAnchor(lblDescription, 10.0);

			hBox.getChildren().add(recipePane);
		}

		// Start search for recipes in database by clicking on button, keywords are read from txfIngredient
		btnStartSearch.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				recipeArrayList = RecipeManipulation.searchRecipeByIngriedientsArray(txfSearch.getText());

				// Fill the table with recipes and all information.
				final ObservableList<Recipes> recipeObserver = FXCollections.observableArrayList(recipeArrayList);
		        tbvRecipes.setItems(recipeObserver);
		        tbcRecipes.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
		        tbcIngredients.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

		        // Tells the User if there are no recipes that matches the keywords
		        if(recipeObserver.size() == 0)
		        {
		        	Label placeholder = new Label("Es existieren keine Rezepte zu dieser Suche.");
		        	placeholder.setWrapText(true);
		        	tbvRecipes.setPlaceholder(placeholder);
		        }
		        event.consume();
			}
		});

		// Start search for recipes in database by pressing enter on textfield, keywords are read from this textfield
		txfSearch.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if (txfSearch.isFocused())
				{
					if(event.getCode() == KeyCode.ENTER)
					{
						recipeArrayList = RecipeManipulation.searchRecipeByIngriedientsArray(txfSearch.getText());

						// Fill the table with recipes and all information.
						final ObservableList<Recipes> recipeObserver = FXCollections.observableArrayList(recipeArrayList);
				        tbvRecipes.setItems(recipeObserver);
				        tbcRecipes.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
				        tbcIngredients.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

				        // Tells the User if there are no recipes that matches the keywords
				        if(recipeObserver.size() == 0)
				        {
				        	Label placeholder = new Label("Es existieren keine Rezepte zu dieser Suche.");
				        	placeholder.setWrapText(true);
				        	tbvRecipes.setPlaceholder(placeholder);
				        }
				        event.consume();
					}
				}
			}
		});

		// translate elements on the recipe search pane
		lblSearch.setTranslateY(30);
		btnStartSearch.setTranslateY(0);
		txfSearch.setTranslateY(60);
		txfSearch.setTranslateX(0);
		tbvRecipes.setTranslateY(90);

		return hBox;
	}
}