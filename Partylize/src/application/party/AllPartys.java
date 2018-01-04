package application.party;

import application.PartyManipulation;
import application.PartyType;
import application.ProfileManipulation;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * Gives an overview over a list of partys
 *
 * @author Daniel Hecker : 27.12.2017
 */
public class AllPartys
{
	private ListView<Party> lvwPartys;

	/**
	 * Gives an overview over a list of partys from a specific partytype
	 *
	 * @param partyType The sting how it shall be named (partytype+" Veranstaltungen:")
	 * @param pt What kind of party shall be listed
	 * @return An HBox with the list
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public VBox create(String partyType, PartyType pt)
	{
		Label lblParty = new Label(partyType+" Veranstaltungen:");

		lvwPartys = new ListView<Party>();
		lvwPartys.getItems().add(new Party(-1, "+ NEU", ProfileManipulation.getUserId(true), pt));
		lvwPartys.getItems().addAll(PartyManipulation.getPartys(pt));

		Party.setQuadSize(lvwPartys);

		VBox vbxPartys = new VBox();
		vbxPartys.getChildren().addAll(lblParty, lvwPartys);
		vbxPartys.setPadding(new Insets(20));

		return vbxPartys;
	}

	public ListView<Party> getLvwPartys() {
		return lvwPartys;
	}
}