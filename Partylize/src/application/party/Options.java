package application.party;

import java.util.LinkedList;

import application.PartyManipulation;
import application.PartyType;
import application.Profile;
import application.ProfileManipulation;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class Options
{
	private static Button btnCreatePreview;
	private static Button btnOptions;
	private static Button btnEdit;

	public static HBox create()
	{
		HBox compartment = Compartment.newCompartment(new Compartment<HBox>() {

			@Override
			public HBox addCompartment()
			{
				HBox hbxButtonsPane = new HBox();

				hbxButtonsPane.setSpacing(10);

				//Button Preview
				btnCreatePreview = new Button("Vorschau");

				//Button Stop
				btnOptions = new Button();

				//Button Edit
				btnEdit = new Button("Ändern");

				setButtonStyle(new Button[]{btnCreatePreview, btnEdit, btnOptions});

				ExtendedFX.setNodeVisible(btnOptions, true);

				if(Party.getOpenParty().getOwner() == ProfileManipulation.getUserId(true)){
					if(Party.getOpenParty().getId() == -1){
						btnOptions.setText("Abbrechen");
						ExtendedFX.setNodeVisible(btnCreatePreview, true);
					}else{
						btnOptions.setText("Party löschen");
						ExtendedFX.setNodeVisible(btnEdit, true);
					}
				}else if((Party.getOpenParty().getPartyType() == PartyType.PUBLIC && PartyManipulation.isUserInPublicParty(true))
						|| Party.getOpenParty().getPartyType() == PartyType.PRIVATE){
					btnOptions.setText("Verlassen");
				}else if(Party.getOpenParty().getPartyType() == PartyType.PUBLIC && !PartyManipulation.isUserInPublicParty(true)){
					btnOptions.setText("Beitreten");
				}

				//Add Preview / Stop to Pane
				hbxButtonsPane.getChildren().addAll(btnCreatePreview, btnOptions, btnEdit);

				return hbxButtonsPane;
			}
		});

		compartment.setAlignment(Pos.CENTER);


//Create / Preview Button
	btnCreatePreview.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				createButton();
			}
		});

//Stop Button
	btnOptions.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				optionButton();
			}
		});

//Edit Button
	btnEdit.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				//Edit Buttons visible
				ExtendedFX.setNodeVisible(GeneralInformation.getEditButtons()[0], true);
				ExtendedFX.setNodeVisible(GeneralInformation.getEditButtons()[1], true);
				ExtendedFX.setNodeVisible(GeneralInformation.getEditButtons()[2], true);
				ExtendedFX.setNodeVisible(btnCreatePreview, true);

				//Create -> Preview
				btnCreatePreview.setText("Vorschau");
				btnOptions.setText("Abbrechen");
				//Make Edit invisible
				ExtendedFX.setNodeVisible(btnEdit, false);
			}
		});

		return compartment;
	}

	private static void setButtonStyle(Button[] buttons)
	{
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].setPrefSize(200, 30);
			ExtendedFX.setNodeVisible(buttons[i], false);
		}
	}

	/**
	 * This method is executed when someone presses the create button.
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	private static void createButton()
	{
		//If the Button is the Preview Button
		if(btnCreatePreview.getText().equals("Vorschau"))
		{
			//Edit Buttons invisible
			ExtendedFX.setNodeVisible(GeneralInformation.getEditButtons()[0], false);
			ExtendedFX.setNodeVisible(GeneralInformation.getEditButtons()[1], false);
			ExtendedFX.setNodeVisible(GeneralInformation.getEditButtons()[2], false);

			//ComboBoxs and TextFields -> Labels
			GeneralInformation.convert();

			//Preview -> Create
			if(Party.getOpenParty().getId() == -1){
				btnCreatePreview.setText("Erstellen");
			}else{
				btnCreatePreview.setText("Speichern");
			}
			//Make Edit visible
			ExtendedFX.setNodeVisible(btnEdit, true);
		}
		//If the Button is the Create Button
		else if(btnCreatePreview.getText().equals("Erstellen"))
		{
			String[] information = GeneralInformation.getReturnStrings();

			//Insert
			Party party = new Party(
					Party.getOpenParty().getId(),
					information[0],
					information[1],
					information[2],
					information[3],
					information[4],
					Party.getOpenParty().getOwner(),
					Party.getOpenParty().getPartyType()
				);

			int id = PartyManipulation.createParty(party);

			if(Party.getOpenParty().getPartyType() == PartyType.PRIVATE){
				LinkedList<Profile> selected = Participants.getSelectedParticipants();

				if(!selected.isEmpty())
					PartyManipulation.addFriendsToParty(selected, new Party(
							id,
							party.getLocation()[0],
							party.getOwner(),
							party.getPartyType()
						), true);
			}

			Party.loadPartys();

			Party.loadOpenParty(new Party(
					id,
					party.getLocation()[0],
					party.getOwner(),
					party.getPartyType()
				));
		}
		else if(btnCreatePreview.getText().equals("Speichern"))
		{
			String[] information = GeneralInformation.getReturnStrings();

			//Update
			Party party = new Party(
					Party.getOpenParty().getId(),
					information[0],
					information[1],
					information[2],
					information[3],
					information[4],
					Party.getOpenParty().getOwner(),
					null
				);

			PartyManipulation.updatePartyInformation(party);

			Party.loadPartys();

			Party.loadOpenParty();
		}
	}

	/**
	 * This method is executed when someone presses the stop button.
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	private static void optionButton()
	{
		if(btnOptions.getText().equals("Verlassen"))
		{
			LinkedList<Profile> list = new LinkedList<Profile>();
			list.add(new Profile(ProfileManipulation.getUserId(true), null));

			PartyManipulation.removeParticipantsFromParty(list, Party.getOpenParty(), true);

			if(Party.getOpenParty().getPartyType() == PartyType.PRIVATE){
				Party.loadPartys();
				Party.clearOpenParty();
			}else if(Party.getOpenParty().getPartyType() == PartyType.PUBLIC){
				Party.loadOpenParty();
			}
		}
		else if(btnOptions.getText().equals("Beitreten"))
		{
			LinkedList<Profile> list = new LinkedList<Profile>();
			list.add(new Profile(ProfileManipulation.getUserId(true), null));

			PartyManipulation.addFriendsToParty(list, Party.getOpenParty(), true);

			Party.loadOpenParty();
		}
		else if(btnOptions.getText().equals("Party löschen"))
		{
			PartyManipulation.deleteParty(Party.getOpenParty(), true);

			Party.loadPartys();
			Party.clearOpenParty();
		}
		else if(btnOptions.getText().equals("Abbrechen"))
		{
			if(Party.getOpenParty().getId() == -1){
				Party.clearOpenParty();
			}else{
				Party.loadOpenParty();
			}
		}
	}
}














