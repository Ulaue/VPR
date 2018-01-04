package application.party;

import java.util.LinkedList;

import application.MainScreen;
import application.PartyManipulation;
import application.PartyType;
import application.Profile;
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
 * Provides the list of participants
 *
 * @author Daniel Hecker : 28.12.2017
 */
public class Participants
{
	private static ListView<GridPane> lvwParticipants;

	private static Button btnAddFriend;
	private static Button btnRemoveParticipant;

	private static LinkedList<Profile> friendList;
	private static String mode = "nothing";

	/**
	 * Creates the friendlist + layout
	 *
	 * @return VBox
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static VBox create()
	{
		LinkedList<HBox> compartments = new LinkedList<HBox>();
		friendList = new LinkedList<Profile>();

		VBox vbxMainPane = new VBox();
		vbxMainPane.setPadding(new Insets(20));

		compartments.add(Compartment.newCompartment(new Compartment<HBox>()
		{
			@Override
			public HBox addCompartment()
			{
				HBox hbxFriendsPane = new HBox();

			//ListView with all friends
				lvwParticipants = new ListView<GridPane>();
				Party.setQuadSize(lvwParticipants);

				if(Party.getOpenParty().getPartyType() == PartyType.PRIVATE && Party.getOpenParty().getId() == -1){
					friendList = ProfileManipulation.getFriends(true);
					loadList(true);
				}else{
					friendList = PartyManipulation.getParticipantsInParty(Party.getOpenParty(), false, true);
					loadList(false);
				}

				//Add Friends to Pane
				hbxFriendsPane.getChildren().addAll(lvwParticipants);

				return hbxFriendsPane;
			}
		}));

	//Buttons
		compartments.add(Compartment.newCompartment(new Compartment<HBox>()
		{
			@Override
			public HBox addCompartment()
			{
				HBox hbxButtonPane = new HBox();

				btnAddFriend = new Button("Hinzufügen");
				btnAddFriend.setPrefWidth(MainScreen.getQuadWidth());

				btnRemoveParticipant = new Button("Entfernen");
				btnRemoveParticipant.setPrefWidth(MainScreen.getQuadWidth());

				hbxButtonPane.getChildren().addAll(btnAddFriend, btnRemoveParticipant);

				return hbxButtonPane;
			}
		}));

	//Layout changes depending on the party
		if(Party.getOpenParty().getPartyType() == PartyType.PUBLIC && Party.getOpenParty().getId() == -1){
			ExtendedFX.setNodeVisible(lvwParticipants, false);
			ExtendedFX.setNodeVisible(btnAddFriend, false);
			ExtendedFX.setNodeVisible(btnRemoveParticipant, false);
		}else if((Party.getOpenParty().getPartyType() == PartyType.PUBLIC)
				|| (Party.getOpenParty().getPartyType() == PartyType.PRIVATE && Party.getOpenParty().getId() == -1)){
			ExtendedFX.setNodeVisible(btnAddFriend, false);
			ExtendedFX.setNodeVisible(btnRemoveParticipant, false);
		}else if(Party.getOpenParty().getOwner() != ProfileManipulation.getUserId(true) && Party.getOpenParty().getPartyType() == PartyType.PRIVATE){
			ExtendedFX.setNodeVisible(btnRemoveParticipant, false);
		}

		btnAddFriend.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				//When pressing the add button
				if(mode.equals("nothing"))
				{
					btnRemoveParticipant.setText("Abbrechen");

					ExtendedFX.setNodeVisible(btnRemoveParticipant, true);

					mode = "add";

					friendList = PartyManipulation.getFriendsNotInParty(Party.getOpenParty());

					loadList(true);
				}
				//When you have selected the people you want to add
				else if(mode.equals("add"))
				{
					btnRemoveParticipant.setText("Entfernen");

					if(Party.getOpenParty().getOwner() != ProfileManipulation.getUserId(true)){
						ExtendedFX.setNodeVisible(btnRemoveParticipant, false);
					}

					mode = "nothing";

					LinkedList<Profile> selected = getSelectedParticipants();

					if(!selected.isEmpty())
						PartyManipulation.addFriendsToParty(selected, Party.getOpenParty(), true);

					friendList = PartyManipulation.getParticipantsInParty(Party.getOpenParty(), false, true);

					loadList(false);
				}
				//When you have selected the people you want to remove
				else if(mode.equals("remove"))
				{
					btnAddFriend.setText("Hinzufügen");
					btnRemoveParticipant.setText("Entfernen");

					if(Party.getOpenParty().getOwner() != ProfileManipulation.getUserId(true)){
						ExtendedFX.setNodeVisible(btnRemoveParticipant, false);
					}

					mode = "nothing";

					LinkedList<Profile> selected = getSelectedParticipants();

					if(!selected.isEmpty())
						PartyManipulation.removeParticipantsFromParty(selected, Party.getOpenParty(), true);

					friendList = PartyManipulation.getParticipantsInParty(Party.getOpenParty(), false, true);

					loadList(false);
					Party.loadGifts();
				}
			}
		});

		btnRemoveParticipant.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				//When you press the remove button
				if(mode.equals("nothing"))
				{
					btnAddFriend.setText("Entfernen");
					btnRemoveParticipant.setText("Abbrechen");

					mode = "remove";

					friendList = PartyManipulation.getParticipantsInParty(Party.getOpenParty(), true, true);

					loadList(true);
				}
				//When you press the stop button
				else if(mode.equals("add") || mode.equals("remove"))
				{
					btnAddFriend.setText("Hinzufügen");
					btnRemoveParticipant.setText("Entfernen");

					if(Party.getOpenParty().getOwner() != ProfileManipulation.getUserId(true)){
						ExtendedFX.setNodeVisible(btnRemoveParticipant, false);
					}

					mode = "nothing";

					friendList = PartyManipulation.getParticipantsInParty(Party.getOpenParty(), false, true);

					loadList(false);
				}
			}
		});

		vbxMainPane.getChildren().addAll(compartments);

		return vbxMainPane;
	}

	/**
	 * Loades/Reloades the list of participants, that are currently shown
	 *
	 * @param editable Is the list supposed to have checkboxes?
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void loadList(boolean editable)
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

		lvwParticipants.getItems().clear();

	//Adds all friends with name and a CheckBox to a GridPane and then adds the GridPane to the ListView
		for (int i = 0; i < friendList.size(); i++)
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

			Label lbl = new Label(friendList.get(i).toString());
			lbl.setUserData(friendList.get(i));

			gp.add(lbl, 1, 0);

			lvwParticipants.getItems().add(gp);
		}
	}

	/**
	 * Get all selected Profiles
	 *
	 * @return List of Profiles
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static LinkedList<Profile> getSelectedParticipants()
	{
		LinkedList<Profile> participants = new LinkedList<Profile>();

		for (int i = 0; i < lvwParticipants.getItems().size(); i++)
		{
			if(((CheckBox)lvwParticipants.getItems().get(i).getChildren().get(0)).isSelected()){
				participants.add((Profile) ((Label)lvwParticipants.getItems().get(i).getChildren().get(1)).getUserData());
			}
		}

		return participants;
	}
}


































