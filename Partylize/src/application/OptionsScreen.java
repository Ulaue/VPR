/**
 * @author Team 3, Dominik (23.11.2017): Screen for setting options
 * */

package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class OptionsScreen
{

	// Array to store tags for OptionTopics
	private static final String [] options= {"Sound Einstellungen","Layout"};

	// Array to store tags for SoundOptions
	private static final String [] soundOptions= {"Lautstärke","Freundschaftsanfragen","Partyeinladungen","Chat-Benachrichtigungen"};

	// Array to store tags for layoutOptions
	private static final String [] layoutOptions= {"Navigationsleiste","Hintergrund","Benachrichtigungsleiste","Buttons"};

	// Array to store the link between main and subOption (principally nothing less than an enumeration)
	private static final String [][] subOptions  = {soundOptions,layoutOptions};

	// Volume value for different sound initialized with default volumeleve 10
	public static int volume = 9;

	// Array wich will store if soundstatus are enabled or not
	static boolean [] soundSettings = new boolean [soundOptions.length];

	// Array wich will store colorsettings as jafx-css-command with hexadeximalending
	static String [] hexColorSet= new String[layoutOptions.length];

	// Paths for different sounds
	static String [] saveSoundPath={"cartoon003.wav"};

	// Array including all buttons, wich are alrdy set into the correct decibelrange
	static Clip [] btnSounds = new Clip [saveSoundPath.length];

	//Three Buttons for the User to make a final choice:
	static Button cancel;
	static Button saveChanges ;
	static Button setDefault ;

	public static void initMenu(Stage primaryStage, BorderPane center, AnchorPane pane, String[] options,String [][]subOptions) throws FileNotFoundException, IOException{

		//Get the center of the y-axis
		double stageWidth= primaryStage.getWidth();

		//Postitions for start placing elements
		double stageY = 30; // Starting a bit lower than the upper border
		double stageX = (stageWidth/2)-200; // Trying to start nealy centred

		//Create menu
		for(int i=0;i<options.length;i++){

			//This distance values may set an element placing-pattern
			int distMainToSub=30;
			int distSubToSub=50;
			int distSubToNextm=50;

			createMainMenu(pane, stageY, stageX, options[i]);
			stageY+=distMainToSub;

			// Create submenu
			for (int j=0; j<subOptions[i].length;j++){

				//Creates Label for mainmenu
				createSubMenu(pane, stageY, stageX, subOptions[i][j]);

				// SoundSettings
				if(options[i].equals("Sound Einstellungen")){
					final ToggleGroup group = new ToggleGroup();

					// A seperated iterator is needed, because for the following 'changed()'-method 'j' is unkown
					int soundIterator=j;

					//Submenu to set the volume
					if(subOptions[i][j].equals("Lautstärke")){

						// Create Slider
						generateSlider(pane, stageY, stageX, volume);
					}

					//Default options for sound Options
					else{
						generateChoice(pane, group, stageY, stageX, j);
					}
				}

				/** The default cause is used to create ColorPickers to customize the colors
				    of some elements */
				else{
					//Create a ColorPicker
					generateColorPicker(pane, stageY, stageX, j, center);
				}

				// Add distance to the next subOption
				stageY+=distSubToSub;
			}

			// Add distance to the next mainMenu
			stageY+=distSubToNextm;
		}

		// Create default buttons, for saving and discarding changes and for loading default values
		generateDefaultButtons(pane, stageY, stageX, center);
	}

	private static void generateDefaultButtons(AnchorPane pane, double stageY, double stageX, BorderPane center){
		// Initialize the three buttons with german tags
				cancel = new Button("Verwerfen");
				saveChanges = new Button("Speichern");
				setDefault = new Button("Zurücksetzen");

				// Add all three buttons
				pane.getChildren().addAll(saveChanges,cancel,setDefault);

				// Set positions
				pane.setTopAnchor(cancel, stageY);
				pane.setLeftAnchor(cancel, stageX);
				pane.setTopAnchor(setDefault, stageY);
				pane.setLeftAnchor(setDefault, stageX+360);
				pane.setTopAnchor(saveChanges, stageY+50);
				pane.setLeftAnchor(saveChanges, stageX+180);

				// Define event when clicked
				saveChanges.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent event) {
						try {
							saveSettings();
							btnSounds[0].start();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});

				cancel.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent event) {
						center.getStylesheets().add((getClass().getResource("application.css")).toExternalForm());

					}
				});
	}

	private static void generateColorPicker(AnchorPane pane, double stageY, double stageX, int pos, BorderPane center){
		// Instanziate a new ColorPicker
		ColorPicker cP = new ColorPicker();

		// Set ActionEvent for picking a color
		cP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	hexColorSet[pos]= "-fx-background-color: #"+Integer.toHexString(cP.getValue().hashCode());
            	center.setStyle(hexColorSet[1]);
                System.out.println(Arrays.toString(hexColorSet));
                System.out.println(Arrays.toString(soundSettings));
            }
        });

		// Add ColorPicker and set its position
		pane.getChildren().add(cP);
		pane.setTopAnchor(cP, stageY);
		pane.setLeftAnchor(cP, stageX+290);
	}

	private static void generateChoice(AnchorPane pane, ToggleGroup group, double stageY, double stageX, int pos){
		//Two Choices: Volume turned "On" or "Off".
		RadioButton btnOn = new RadioButton("On");
		btnOn.setUserData(true);
		btnOn.setToggleGroup(group);
		RadioButton btnOff= new RadioButton("Off");
		btnOff.setUserData(false);
		btnOff.setToggleGroup(group);

		//Check if the sound for this position should be dis- or enabled
		if(soundSettings[pos]==true)btnOn.setSelected(true);
		else btnOff.setSelected(true);

		// Add Buttons to the Pane and set its position
		pane.getChildren().addAll(btnOn,btnOff);
		pane.setTopAnchor(btnOn, stageY);
		pane.setLeftAnchor(btnOn, stageX+290);
		pane.setTopAnchor(btnOff, stageY);
		pane.setLeftAnchor(btnOff, stageX+370);

		/** Add buttons to a togglegroup to make sure that only one button can be toggled at once
		and make them to set their value if toggled. */
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		            if (group.getSelectedToggle() != null) {
		                final String test = group.getSelectedToggle().getUserData().toString();
		                soundSettings[pos]= (boolean)group.getSelectedToggle().getUserData();
		            }
		        }
			});
		}

	private static void createMainMenu(AnchorPane pane, Double stageY, Double stageX, String tag) {
		// TODO Auto-generated method stub
		// Create new sublabel
		Label mainLbl = new Label(tag);
		mainLbl.setFont(new Font("Arial",24));
		mainLbl.setTextFill(new Color(0.1, 0.1, 0.3, 0.7));

		// Add the submenu to the pane and set its position.
		pane.getChildren().addAll(mainLbl);
		pane.setTopAnchor(mainLbl, stageY);
		pane.setLeftAnchor(mainLbl, stageX);

	}


	private static void createSubMenu(AnchorPane pane, Double stageY, Double stageX, String tag) {
		// TODO Auto-generated method stub
		// Create new sublabel
		Label subLbl = new Label(tag);
		subLbl.setFont(new Font("Arial",20));
		subLbl.setTextFill(new Color(0.1, 0.7, 0.8, 1));

		// Add the submenu to the pane and set its position.
		pane.getChildren().addAll(subLbl);
		pane.setTopAnchor(subLbl, stageY);
		pane.setLeftAnchor(subLbl, stageX + 30);

	}

	private static void generateSlider(AnchorPane pane, double stageY, double stageX, double volume) {
		// TODO Auto-generated method stub
		//Create a slider to adjust the volume of all sounds
		Slider slider = new Slider(0, 10, volume);
		Label sliderValue = new Label(String.format("%.0f", slider.getValue()));
		sliderValue.setFont(new Font("Arial",20));
		sliderValue.setTextFill(new Color(1,1,1,0.9));
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(1);
		slider.setScaleX(1.2);
		slider.setBlockIncrement(1);

		// Set madding to the left
		int sliderMadding=290;

		//Add an eventlistener to slider for saving the chosen volume
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	sliderValue.setText(String.format("%.0f", new_val));
            	OptionsScreen.volume = Integer.parseInt(String.format("%.0f", new_val));
            }
        });

		//Add slider to the Pane and set its position
		pane.getChildren().addAll(slider,sliderValue);
		pane.setTopAnchor(slider, stageY);

		//slider madding left
		pane.setLeftAnchor(slider, stageX+sliderMadding);
	}

	static AnchorPane startOption(Stage primaryStage, BorderPane center) throws FileNotFoundException, IOException, UnsupportedAudioFileException, LineUnavailableException {
		// TODO Auto-generated constructor stub

		// Load sound-, volume- and layoutsetting
		getSoundSettings();
		getSounds(volume);
		getLayout(center, 0); //<-->not implemented yet

		// Take a new pane to work on it
		AnchorPane pane = new AnchorPane();

		// Initialize the mainMenu
		initMenu(primaryStage, center, pane, options, subOptions);

		// Give the pane back
		return pane;
	}

	private static void getSoundSettings() throws FileNotFoundException, IOException{

		//Create a BufferReader for reading the soundettings from a file
		try (BufferedReader br = new BufferedReader(new FileReader("soundSettings.conf"))){

			// Create Stringbuilder
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    //Getting volume value from the first line
		    volume = (Integer.parseInt(line.toString()));

		    // read next line to skip the volume in the configfile
		    line=br.readLine();

		    // From here on we put the rest of the file together, wich will look like an binary code
		    while (line != null) {

		    	//Append the line to the string, wich can only be 0 or 1
		        sb.append(line);
		        line = br.readLine();
		    }

		    // From here soundStatus looks like : '011010' depending on wich sounds are turned on or off
		    String soundStatus = sb.toString();

		    /* Go trough every entry in soundSettings[] and check in it the binary code
		       if the position is dis- or enabled */
		    for (int i=0;i<soundSettings.length;i++){

		    	//If its binary position is 0 the value will bis disabled
		    	if(Integer.parseInt(""+soundStatus.charAt(i))==0)
		    		soundSettings[i]= false;

		    	//In genereal there should be sound
		    	else soundSettings[i]=true;
		    }
		}
	}

	private static void saveSettings() throws IOException
	  {

		//Create FileWriter and BufferedWriter
	    FileWriter fw = new FileWriter("soundSettings.conf");
	    BufferedWriter bw = new BufferedWriter(fw);

	    //Write the Volume  into the first Line
	    bw.write(""+volume);
	    bw.newLine();

	    /* Start writing the the soundSetting line after line:
	       1 stands for true and means that the sound is enabled,
  	       0 stands for true and means that the sound is disbaled */
	    for (int i=0; i< soundSettings.length;i++){

	    	/* Writing soundstatus:
	    	   0  stands for disabled,
	    	   1 stands for enabled */
	    	if(soundSettings[i]==false)bw.write("0");
	    	else bw.write("1");

	    	//Write a new line after each entry
	    	bw.newLine();
	    }

	    //Close the BufferedWriter
	    bw.close();
	  }

	private static void getSounds(float volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException{

		//reducings decibels for all sounds
		for(int i = 0; i<btnSounds.length;i++){

			// Load the ausiInputStram from file
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
			    new File("cartoon003.wav"));

			// Save InputStream as Clip
			btnSounds[i] = AudioSystem.getClip();
			btnSounds[i].open(audioInputStream);

			// Get FloatControl ti gave
			FloatControl gainControl =
			    (FloatControl) btnSounds[i].getControl(FloatControl.Type.MASTER_GAIN);

			/* Setting the decibels for Soundoutput between -40 and 0 max and min value
			   for decibels when playing a sound lie between -80.0f and 6.03f */
			gainControl.setValue(-40.0f+(volume*4));
		}
	}

	private static void getLayout (BorderPane center, int i){
		switch (i){
		case 0:

	/*	case 1:
			center.getStylesheets().add((getClass().getResource("default.css")).toExternalForm());
		case 2: ()reload scene */
		}

	}


}
