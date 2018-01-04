package application.party;

import java.util.LinkedList;

import application.Gift;
import application.GiftType;
import application.MainScreen;
import application.PartyManipulation;
import application.ProfileManipulation;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Gives an overview over a list of gifts
 *
 * @author Daniel Hecker : 27.12.2017
 */
public class IngredientsAndDrinks
{
	private ListView<GridPane> lvwGifts;

	private Button btnAddGifts;
	private Button btnRemoveGifts;

	private LinkedList<Gift> giftList;

	private String mode = "nothing";

	/**
	 * Gives an overview over a list of gift from a specific gifttype
	 *
	 * @param giftType The sting how it shall be named
	 * @param gt What kind of gift shall be listed
	 * @return An HBox with the list
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public VBox create(GiftType gt, Party party)
	{
		Label lblGift = new Label();
		if(gt == GiftType.INGREDIENT){
			lblGift.setText("Zutaten:");
		}else if(gt == GiftType.SNACK){
			lblGift.setText("Speisen:");
		}

		lvwGifts = new ListView<GridPane>();
		Party.setQuadSize(lvwGifts);

		giftList = PartyManipulation.getGiftsInParty(gt, party);
		loadList(false);

		VBox vbxGifts = new VBox();
		vbxGifts.getChildren().addAll(lblGift, lvwGifts);
		vbxGifts.setPadding(new Insets(20));

		HBox hbxButtons = Compartment.newCompartment(new Compartment<HBox>()
		{
			@Override
			public HBox addCompartment()
			{
				HBox tmp = new HBox();

				btnAddGifts = new Button("Hinzufügen");
				btnAddGifts.setPrefWidth(MainScreen.getQuadWidth()/2);

				btnRemoveGifts = new Button("Entfernen");
				btnRemoveGifts.setPrefWidth(MainScreen.getQuadWidth()/2);

				tmp.getChildren().addAll(btnAddGifts, btnRemoveGifts);

				return tmp;
			}
		});

		vbxGifts.getChildren().add(hbxButtons);

		if(gt == GiftType.INGREDIENT){
			Button btnToRecipe = new Button("Zu Rezepten kombinieren");
			btnToRecipe.setPrefWidth(MainScreen.getQuadWidth());
			btnToRecipe.setMaxWidth(MainScreen.getQuadWidth());

			btnToRecipe.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					// TODO To Recipe Search (Robert)
				}
			});

			vbxGifts.getChildren().add(btnToRecipe);
		}

		btnAddGifts.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				//When pressing the add button
				if(mode.equals("nothing"))
				{
					btnRemoveGifts.setText("Abbrechen");

					mode = "add";

					giftList = PartyManipulation.getGiftsNotAdded(gt, ProfileManipulation.getUserId(true), Party.getOpenParty(), true);

					loadList(true);
				}
				//When you have selected the people you want to add
				else if(mode.equals("add"))
				{
					btnRemoveGifts.setText("Entfernen");

					mode = "nothing";

					LinkedList<Gift> selected = getSelectedGifts();

					if(!selected.isEmpty())
						PartyManipulation.addGiftsToParty(selected, Party.getOpenParty(), true);

					giftList = PartyManipulation.getGiftsInParty(gt, Party.getOpenParty());

					loadList(false);
				}
				//When you have selected the people you want to remove
				else if(mode.equals("remove"))
				{
					btnAddGifts.setText("Hinzufügen");
					btnRemoveGifts.setText("Entfernen");

					mode = "nothing";

					LinkedList<Gift> selected = getSelectedGifts();

					if(!selected.isEmpty())
						PartyManipulation.removeGiftsFromParty(selected, Party.getOpenParty(), true);

					giftList = PartyManipulation.getGiftsInParty(gt, Party.getOpenParty());

					loadList(false);
				}
			}
		});

		btnRemoveGifts.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				//When you press the remove button
				if(mode.equals("nothing"))
				{
					btnAddGifts.setText("Entfernen");
					btnRemoveGifts.setText("Abbrechen");

					mode = "remove";

					giftList = PartyManipulation.getGiftsAdded(gt, ProfileManipulation.getUserId(true), Party.getOpenParty(), true);

					loadList(true);
				}
				//When you press the stop button
				else if(mode.equals("add") || mode.equals("remove"))
				{
					btnAddGifts.setText("Hinzufügen");
					btnRemoveGifts.setText("Entfernen");

					mode = "nothing";

					giftList = PartyManipulation.getGiftsInParty(gt, Party.getOpenParty());

					loadList(false);
				}
			}
		});

		return vbxGifts;
	}

	/**
	 * Loades/Reloades the list of gifts, that are currently shown
	 *
	 * @param editable Is the list supposed to have checkboxes?
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public void loadList(boolean editable)
	{
	//Gets added to the GridPanes inside the ListView
		EventHandler<MouseEvent> checkboxEvent = new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					if(((CheckBox)((GridPane)event.getSource()).getChildren().get(0)).isSelected()){
						((CheckBox)((GridPane)event.getSource()).getChildren().get(0)).setSelected(false);
					}else{
						((CheckBox)((GridPane)event.getSource()).getChildren().get(0)).setSelected(true);
					}
				}
			};

		lvwGifts.getItems().clear();

	//Adds all friends with name and a CheckBox to a GridPane and then adds the GridPane to the ListView
		for (int i = 0; i < giftList.size(); i++)
		{
			//Creating
			GridPane gp = new GridPane();

			//if the list shall be editable, Combobox's will appear in front of the participants
			if(editable)
			{
				CheckBox cbx = new CheckBox();

				gp.add(cbx, 0, 0);

				gp.setOnMouseClicked(checkboxEvent);
			}

			Label lbl = new Label();
			if(editable){
				lbl.setText(giftList.get(i).getGiftName());
			}else{
				lbl.setText(giftList.get(i).toString());
			}

			lbl.setUserData(giftList.get(i));

			gp.add(lbl, 1, 0);

			lvwGifts.getItems().add(gp);
		}
	}

	/**
	 * Get all selected Gifts
	 *
	 * @return List of Gifts
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public LinkedList<Gift> getSelectedGifts()
	{
		LinkedList<Gift> participants = new LinkedList<Gift>();

		for (int i = 0; i < lvwGifts.getItems().size(); i++)
		{
			if(((CheckBox)lvwGifts.getItems().get(i).getChildren().get(0)).isSelected()){
				participants.add((Gift) ((Label)lvwGifts.getItems().get(i).getChildren().get(1)).getUserData());
			}
		}

		return participants;
	}
}





















