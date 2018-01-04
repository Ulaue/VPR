package application.party;

import java.util.LinkedList;

import application.PartyManipulation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class contains the general variables and methods to create a party.
 * It also has methods to get needed information for later use.
 *
 * @author Daniel Hecker : 16.12.2017
 */
public class GeneralInformation
{
//---Styles---
	//First column Label width
	private static int firstColumnWidth = 60;
	//Location TextField width
	private static int locationLblWidth = 130;
	//Edit Button width
	private static int editButtonWidth = 100;
	//Edit Button Margin
	private static Insets editButtonMargin = new Insets(0, 0, 0, 10);
//---Styles---

	//Date
	private static ComboBox<Integer> cbxDay;
	private static ComboBox<String> cbxMonth;
	private static ComboBox<Integer> cbxYear;
	private static Label lblDateReturn;
	private static Button btnDateEdit;

	//Time
	private static ComboBox<String> cbxHour;
	private static ComboBox<String> cbxMinute;
	private static Label lblTimeReturn;
	private static Button btnTimeEdit;

	//Location
	private static TextField tfdPartyName;
	private static TextField tfdStreetAndNumber;
	private static TextField tfdPostcodeAndTown;
	private static Label lblLocationPartyNameReturn;
	private static Label lblLocationStreetAndNumberReturn;
	private static Label lblLocationPostcodeAndTownReturn;
	private static Button btnLocationEdit;

	private static boolean[] wasEdited = new boolean[3];

	/**
	 * This is the primary method of every party.
	 * The method will contain all information about the party.
	 *
	 * @return VBox that contains the created party type
	 *
	 * @author Daniel Hecker : 16.12.2017<br>
	 * Leo Volkmann (btnEdit) : 16.12.2017
	 */
	public static VBox create(boolean editable)
	{
		if(Party.getOpenParty().getId() == -1){
			for (int i = 0; i < wasEdited.length; i++)
			{
				wasEdited[i] = true;
			}
		}else{
			for (int i = 0; i < wasEdited.length; i++)
			{
				wasEdited[i] = false;
			}
		}

		//These are the compartments off the layout
		LinkedList<HBox> compartments = new LinkedList<HBox>();

		int[] currentDateAndTime = ExtendedFX.currentTimeAndDate();

		//Generate MainPane
		VBox vbxMainPane = new VBox();
		vbxMainPane.setSpacing(10);
		vbxMainPane.setPadding(new Insets(20));

//---Date---
		compartments.add(Compartment.newCompartment(new Compartment<HBox>()
		{
			@Override
			public HBox addCompartment()
			{
				HBox hbxDatePane = new HBox();

				//Label
				Label lblDate = new Label("Tag: ");
				lblDate.setPrefWidth(firstColumnWidth);

			//Days
				ObservableList<Integer> daysArray = FXCollections.observableArrayList();
				//Generate days
				for (int i = 1; i < 32; i++) {
					daysArray.add(i);
				}
				cbxDay = new ComboBox<Integer>(daysArray);
				//Preselected current day
				cbxDay.getSelectionModel().select(currentDateAndTime[2]-1);

			//Month
				ObservableList<String> monthArray = FXCollections.observableArrayList();
				//Generate month
				monthArray.addAll("Januar", "Februar", "M‰rz",
						"April", "Mai", "Juni",
						"Juli", "August", "September",
						"Oktober", "November", "Dezember");
				cbxMonth = new ComboBox<String>(monthArray);
				//Preselected current month
				cbxMonth.getSelectionModel().select(currentDateAndTime[1]-1);

			//Year
				ObservableList<Integer> yearArray = FXCollections.observableArrayList();
				//Generate month
				for (int i = 0; i < 3; i++) {
					yearArray.add(currentDateAndTime[0]+i);
				}
				cbxYear = new ComboBox<Integer>(yearArray);
				//Preselected current year
				cbxYear.getSelectionModel().selectFirst();

			//Label and change button
				lblDateReturn = new Label();
				btnDateEdit = new Button("bearbeiten");

				//Add Date to Pane
				hbxDatePane.getChildren().addAll(lblDate, cbxDay, cbxMonth, cbxYear, lblDateReturn, btnDateEdit);

				return hbxDatePane;
			}
		}));

//---Time---
		compartments.add(Compartment.newCompartment(new Compartment<HBox>()
		{
			@Override
			public HBox addCompartment()
			{
				HBox hbxTimePane = new HBox();

				//Label
				Label lblTime = new Label("Uhrzeit: ");
				lblTime.setPrefWidth(firstColumnWidth);

			//Hour and preselect current hour
				ObservableList<String> hoursArray = FXCollections.observableArrayList();
				//Generate hour
				for (int i = 0; i < 24; i++) {
					hoursArray.add(((i < 10) ? "0" : "")+i);
				}
				cbxHour = new ComboBox<String>(hoursArray);
				//Preselected current hour
				cbxHour.getSelectionModel().select(currentDateAndTime[3]);

			//Minute and preselect current minute
				ObservableList<String> minuteArray = FXCollections.observableArrayList();
				//Generate hour
				for (int i = 0; i < 60; i++) {
					minuteArray.add(((i < 10) ? "0" : "")+i);
				}
				cbxMinute = new ComboBox<String>(minuteArray);
				//Preselected current hour
				cbxMinute.getSelectionModel().select(currentDateAndTime[4]);

			//Label and change button
				lblTimeReturn = new Label();
				btnTimeEdit = new Button("bearbeiten");

				//Add Time to Pane
				hbxTimePane.getChildren().addAll(lblTime, cbxHour, cbxMinute, lblTimeReturn, btnTimeEdit);

				return hbxTimePane;
			}
		}));

//---Location---
		compartments.add(Compartment.newCompartment(new Compartment<HBox>()
		{
			@Override
			public HBox addCompartment()
			{
				HBox hbxLocationPane = new HBox();

				//Column for Rows
				VBox vbxLocationParameters = new VBox();

				//Rows for Parameters
				HBox vbxRowName = new HBox();
				HBox vbxRowStreet = new HBox();
				HBox vbxRowTown = new HBox();

				//Label
				Label lblLocation = new Label("Ort: ");
				lblLocation.setPrefWidth(firstColumnWidth);

			//Party Name
				Label lblName = new Label("Veranstaltungsname: ");
				lblName.setPrefWidth(locationLblWidth);

				tfdPartyName = new TextField();

				lblLocationPartyNameReturn = new Label();

			//Street and housenumber
				Label lblStreetAndNumber = new Label("Straﬂe & Hausnummer: ");
				lblStreetAndNumber.setPrefWidth(locationLblWidth);

				tfdStreetAndNumber = new TextField();

				lblLocationStreetAndNumberReturn = new Label();

			//Postcode and town
				Label lblPostcodeAndTown = new Label("Postleitzahl & Stadt: ");
				lblPostcodeAndTown.setPrefWidth(locationLblWidth);

				tfdPostcodeAndTown = new TextField();

				lblLocationPostcodeAndTownReturn = new Label();

				//Add Location to Pane
				vbxRowName.getChildren().addAll(lblName, tfdPartyName, lblLocationPartyNameReturn);
				vbxRowStreet.getChildren().addAll(lblStreetAndNumber, tfdStreetAndNumber, lblLocationStreetAndNumberReturn);
				vbxRowTown.getChildren().addAll(lblPostcodeAndTown, tfdPostcodeAndTown, lblLocationPostcodeAndTownReturn);

			//Change button
				btnLocationEdit = new Button("bearbeiten");

				//Add Rows to Parameters
				vbxLocationParameters.getChildren().addAll(vbxRowName, vbxRowStreet, vbxRowTown);

				//Add Location to Pane
				hbxLocationPane.getChildren().addAll(lblLocation, vbxLocationParameters, btnLocationEdit);

				return hbxLocationPane;
			}
		}));

//---Add all Panes to MainPane---
		vbxMainPane.getChildren().addAll(compartments);

		//Sets Properties for Edit-Buttons and their Labels
		editButtonsAndLabels(new Label[]{lblDateReturn, lblTimeReturn,
					lblLocationPartyNameReturn, lblLocationStreetAndNumberReturn, lblLocationPostcodeAndTownReturn},
				new Button[]{btnDateEdit, btnTimeEdit, btnLocationEdit});

		if(!editable){
			isEditable();
		}

//---Functions---
	//Date Edit Button
		btnDateEdit.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				labelToDate();

				wasEdited[0] = true;

				//Use after copying text from Label to whatever
				onEditClick(
					new Node[]{cbxDay, cbxMonth, cbxYear},
					new Label[]{lblDateReturn},
					new Node[]{lblDateReturn, btnDateEdit});
			}
		});

	//Time Edit Button
		btnTimeEdit.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				labelToTime();

				wasEdited[1] = true;

				//Use after copying text from Label to whatever
				onEditClick(
					new Node[]{cbxHour, cbxMinute},
					new Label[]{lblTimeReturn},
					new Node[]{lblTimeReturn, btnTimeEdit});
			}
		});

	//Location Edit Button
		btnLocationEdit.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				labelToLocation();

				wasEdited[2] = true;

				//Use after copying text from Label to whatever
				onEditClick(
					new Node[]{tfdPartyName, tfdStreetAndNumber, tfdPostcodeAndTown},
					new Label[]{lblLocationPartyNameReturn, lblLocationStreetAndNumberReturn, lblLocationPostcodeAndTownReturn},
					new Node[]{lblLocationPartyNameReturn, lblLocationStreetAndNumberReturn, lblLocationPostcodeAndTownReturn, btnLocationEdit});
			}
		});

		return vbxMainPane;
	}

	/**
	 * Gives a Node a setOnMouseClick event, that sets Nodes visible, Labels to a certain text and other Nodes invisible.
	 *
	 * @param editButton A Node, the setOnMouseClick should be put on
	 * @param visible Node-array, that shall be set visible
	 * @param resetText Label-array, that shall be put to a certain text
	 * @param invisible Node-array, that shall be put invisible
	 *
	 * @author Daniel Hecker : 16.12.2017<br>
	 * Leo Volkmann : 16.12.2017
	 */
	private static void onEditClick(Node[] visible, Label[] resetText, Node[] invisible)
	{
		//Set visible
		for (int i = 0; i < visible.length; i++)
		{
			ExtendedFX.setNodeVisible(visible[i], true);
		}

		//Reset text
		for (int i = 0; i < resetText.length; i++)
		{
			resetText[i].setText("");
		}

		//Set invisible
		for (int i = 0; i < invisible.length; i++)
		{
			ExtendedFX.setNodeVisible(invisible[i], false);
		}
	}

	/**
	 * Puts very specific options on Return-Labels and Edit-Buttons
	 *
	 * @param label Return-Label-array
	 * @param button Edit-Button-array
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	private static void editButtonsAndLabels(Label[] label, Button[] button)
	{
		for (int i = 0; i < label.length; i++)
		{
			ExtendedFX.setNodeVisible(label[i], false);
		}

		for (int i = 0; i < button.length; i++)
		{
			button[i].setPrefWidth(editButtonWidth);
			HBox.setMargin(button[i], editButtonMargin);
			ExtendedFX.setNodeVisible(button[i], false);
		}
	}

	/**
	 * Get edit buttons<br>
	 * 0: date, 1: time, 2: location
	 *
	 * @return Buttons
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static Button[] getEditButtons()
	{
		return new Button[]{btnDateEdit, btnTimeEdit, btnLocationEdit};
	}

	/**
	 * Get return labels String<br>
	 * 0: date, 1: time, 2: name, 3: street, 4: town
	 *
	 * @return Strings
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static String[] getReturnStrings()
	{
		return new String[]{lblDateReturn.getText(), lblTimeReturn.getText(),
				lblLocationPartyNameReturn.getText(), lblLocationStreetAndNumberReturn.getText(), lblLocationPostcodeAndTownReturn.getText()};
	}

	/**
	 * When you open an already existing Party, this will be applied
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	private static void isEditable()
	{
		//all entry fields
		Node[] nodes = new Node[]
				{	cbxDay, cbxMonth, cbxYear,	//Date
					cbxHour, cbxMinute,		//Time
					tfdPartyName, tfdStreetAndNumber, tfdPostcodeAndTown};	//Location

		//can't be accessed
		for (int i = 0; i < nodes.length; i++)
		{
			ExtendedFX.setNodeVisible(nodes[i], false);
		}

		//get information on current party
		Party p = PartyManipulation.getPartyInformation(Party.getOpenParty());

		//apply information
		lblDateReturn.setText(p.getDateAndTime()[0]);
		lblTimeReturn.setText(p.getDateAndTime()[1]);
		lblLocationPartyNameReturn.setText(p.getLocation()[0]);
		lblLocationStreetAndNumberReturn.setText(p.getLocation()[1]);
		lblLocationPostcodeAndTownReturn.setText(p.getLocation()[2]);

		//all output fields
		nodes = new Node[]
				{	lblDateReturn,	//Date
					lblTimeReturn, 	//Time
					lblLocationPartyNameReturn, lblLocationStreetAndNumberReturn, lblLocationPostcodeAndTownReturn};	//Location

		//set information visible
		for (int i = 0; i < nodes.length; i++)
		{
			ExtendedFX.setNodeVisible(nodes[i], true);
		}
	}

	/**
	 * Transforms editable text to fixed text, if it was edited
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static void convert()
	{
		if(wasEdited[0]){
			dateToLabel();
		}

		if(wasEdited[1]){
			timeToLabel();
		}

		if(wasEdited[2]){
			locationToLabel();
		}

		for (int i = 0; i < wasEdited.length; i++)
		{
			wasEdited[i] = false;
		}
	}

	/**
	 * Transforms the date to label
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	private static void dateToLabel()
	{
		//Set ComboBoxs invisible
		ExtendedFX.setNodeVisible(cbxDay, false);
		ExtendedFX.setNodeVisible(cbxMonth, false);
		ExtendedFX.setNodeVisible(cbxYear, false);

		//ComboBoxs -> Label
		lblDateReturn.setText(cbxDay.getValue().toString()+". "
				+cbxMonth.getValue()+" "+cbxYear.getValue().toString());

		//Set Label visible
		ExtendedFX.setNodeVisible(lblDateReturn, true);
	}

	/**
	 * Transforms the time to label
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	private static void timeToLabel()
	{
		//Set TextFields invisible
		ExtendedFX.setNodeVisible(cbxHour, false);
		ExtendedFX.setNodeVisible(cbxMinute, false);

		//TextFields -> Label
		lblTimeReturn.setText(cbxHour.getValue()+":"+cbxMinute.getValue());

		//Set Label visible
		ExtendedFX.setNodeVisible(lblTimeReturn, true);
	}

	/**
	 * Transforms the location to label
	 */
	private static void locationToLabel()
	{
		//Set TextFields invisible
		ExtendedFX.setNodeVisible(tfdPartyName, false);
		ExtendedFX.setNodeVisible(tfdStreetAndNumber, false);
		ExtendedFX.setNodeVisible(tfdPostcodeAndTown, false);

		//TextFields -> Label
		lblLocationPartyNameReturn.setText(tfdPartyName.getText());
		lblLocationStreetAndNumberReturn.setText(tfdStreetAndNumber.getText());
		lblLocationPostcodeAndTownReturn.setText(tfdPostcodeAndTown.getText());

		//Set Label visible
		ExtendedFX.setNodeVisible(lblLocationPartyNameReturn, true);
		ExtendedFX.setNodeVisible(lblLocationStreetAndNumberReturn, true);
		ExtendedFX.setNodeVisible(lblLocationPostcodeAndTownReturn, true);
	}

	/**
	 * Transforms the label to date
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	private static void labelToDate()
	{
		Integer day = Integer.parseInt(lblDateReturn.getText().split(". ")[0]);
		String month = lblDateReturn.getText().split(" ")[1];
		Integer year = Integer.parseInt(lblDateReturn.getText().split(" ")[2]);

		cbxDay.getSelectionModel().select(day);
		cbxMonth.getSelectionModel().select(month);
		cbxYear.getSelectionModel().select(year);
	}

	/**
	 * Transforms the label to time
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	private static void labelToTime()
	{
		String[] time = lblTimeReturn.getText().split(":");

		cbxHour.getSelectionModel().select(time[0]);
		cbxMinute.getSelectionModel().select(time[1]);
	}

	/**
	 * Transforms the label to location
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	private static void labelToLocation()
	{
		tfdPartyName.setText(lblLocationPartyNameReturn.getText());
		tfdStreetAndNumber.setText(lblLocationStreetAndNumberReturn.getText());
		tfdPostcodeAndTown.setText(lblLocationPostcodeAndTownReturn.getText());
	}
}










































