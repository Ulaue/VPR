package application.party;

import application.GiftType;
import application.MainScreen;
import application.PartyType;
import application.ProfileManipulation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Private and Public Party Screen
 *
 * @author Daniel Hecker : 26.12.2017
 */
public class Party
{
// Object class Party //

	private int id;
	private String[] dateAndTime = new String[2];	//0: date, 1: time
	private String[] location = new String[3];	//0: name, 1: street, 2: town
	private int owner;
	private PartyType type;

	public Party(int id, String name, int owner, PartyType type)
	{
		this.id = id;
		this.location[0] = name;
		this.owner = owner;
		this.type = type;
	}

	public Party(int id, String date, String time, String name, String street, String town, int owner, PartyType type)
	{
		this.id = id;
		this.dateAndTime[0] = date;
		this.dateAndTime[1] = time;
		this.location[0] = name;
		this.location[1] = street;
		this.location[2] = town;
		this.owner = owner;
		this.type = type;
	}

	public int getId()
	{
		return id;
	}

	public String[] getDateAndTime() {
		return dateAndTime;
	}

	public String[] getLocation() {
		return location;
	}

	public int getOwner()
	{
		return owner;
	}

	public PartyType getPartyType()
	{
		return type;
	}

	public String toString()
	{
		return location[0]+" ("+ProfileManipulation.getUserName(owner, true)+")";
	}



// Static class Party //

	private static Party openParty;
	private static AllPartys selectedList;
	private static BorderPane bdpParty = new BorderPane();

	private static AllPartys publicParty = new AllPartys();
	private static AllPartys privateParty = new AllPartys();

	/**
	 * Contains the Party screen
	 *
	 * @param center A pane, in which center this pane will be
	 *
	 * @return BorderPane with Party layout
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static BorderPane create()
	{
		loadPartys();

		setEventHandlers();

		return bdpParty;
	}

	/**
	 * Sets the event handler, so that when the party lists are clicked the clicked party will be displayed
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	private static void setEventHandlers()
	{
		EventHandler<MouseEvent> onPartyClick = new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				try{
					openParty = selectedList.getLvwPartys().getSelectionModel().selectedItemProperty().get();

					loadOpenParty();
				}catch(NullPointerException e){
					// If user clicks on the void part of the list, nothing happens
				}
			}
		};

		publicParty.getLvwPartys().setOnMouseReleased(onPartyClick);
		privateParty.getLvwPartys().setOnMouseReleased(onPartyClick);

		publicParty.getLvwPartys().setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				selectedList = publicParty;
			}
		});

		privateParty.getLvwPartys().setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				selectedList = privateParty;
			}
		});
	}

	/**
	 * Builds the Left panel of the gridpane with party lists
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static void loadPartys()
	{
	//LEFT
	  	bdpParty.setLeft(Compartment.newCompartment(new Compartment<VBox>()
	  	{
	  		@Override
	  		public VBox addCompartment()
	  		{
	  			VBox left = new VBox();

		  			VBox top = new VBox();

			  			HBox publicP = new HBox(publicParty.create("Öffentliche", PartyType.PUBLIC));
			  			setQuadSize(publicP, 2);

			  			top.getChildren().add(publicP);

		  			VBox bottom = new VBox();

			  			HBox privateP = new HBox(privateParty.create("Private", PartyType.PRIVATE));
			  			setQuadSize(privateP, 2);

			  			bottom.getChildren().add(privateP);

		  		left.getChildren().addAll(top, bottom);

	  			return left;
	  		}
	  	}));

	  	setEventHandlers();
	}

	/**
	 * Loades / Reloades the main body
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void loadOpenParty()
	{
		loadParty();
	}

	/**
	 * Loades / Reloades the main body with a given party
	 *
	 * @param party Load this party
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void loadOpenParty(Party party)
	{
		Party.openParty = party;

		loadParty();
	}

	public static void clearOpenParty()
	{
		bdpParty.setTop(null);
		bdpParty.setCenter(null);
		bdpParty.setRight(null);
		bdpParty.setBottom(null);
	}

	/**
	 * Creates the main body of the party layout
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	private static void loadParty()
	{
	//TOP
		bdpParty.setTop(Compartment.newCompartment(new Compartment<HBox>()
	  	{
	  		@Override
	  		public HBox addCompartment()
	  		{
	  			return null;
	  		}
	  	}));

	//CENTER
	  	bdpParty.setCenter(Compartment.newCompartment(new Compartment<HBox>()
	  	{
	  		@Override
	  		public HBox addCompartment()
	  		{
	  			HBox center = new HBox();

		  			VBox leftCenter = new VBox();

		  				HBox info;
		  				if(openParty.getId() == -1){
		  					info = new HBox(GeneralInformation.create(true));
		  					setQuadSize(info, 4, 1);
		  				}else{
		  					info = new HBox(GeneralInformation.create(false));
		  					setQuadSize(info, 4, 1);
		  				}

			  			HBox friends = new HBox(Participants.create());
			  			setQuadSize(friends, 4, 3);

			  		leftCenter.getChildren().addAll(info, friends);

		  			VBox rightCenter;

		  				if(openParty.getId() != -1){
		  					rightCenter = Chat.create();
		  				}else{
		  					rightCenter = new VBox();
		  				}

		  			setQuadSize(rightCenter);

	  			center.getChildren().addAll(leftCenter, rightCenter);

	  			return center;
	  		}
	  	}));

	//BOTTOM
	  	bdpParty.setBottom(Compartment.newCompartment(new Compartment<HBox>()
	  	{
	  		@Override
	  		public HBox addCompartment()
	  		{
	  			HBox bottom = Options.create();

	  			return bottom;
	  		}
	  	}));

		loadGifts();
	}

	/**
	 * Creates the Gift section
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void loadGifts()
	{
	//RIGHT
	  	bdpParty.setRight(Compartment.newCompartment(new Compartment<VBox>()
	  	{
	  		@Override
	  		public VBox addCompartment()
	  		{
	  			VBox right = new VBox();

	  			if(openParty.getId() != -1 && openParty.getPartyType() == PartyType.PRIVATE)
	  			{
		  			VBox top = new VBox();

		  				IngredientsAndDrinks ingredientsClass = new IngredientsAndDrinks();
			  			HBox ingredients = new HBox(ingredientsClass.create(GiftType.INGREDIENT, openParty));
			  			setQuadSize(ingredients, 2);

			  			top.getChildren().add(ingredients);

		  			VBox bottom = new VBox();

		  				IngredientsAndDrinks snacksClass = new IngredientsAndDrinks();
		  				HBox snacks = new HBox(snacksClass.create(GiftType.SNACK, openParty));
		  				setQuadSize(snacks, 2);

		  				bottom.getChildren().add(snacks);

		  			right.getChildren().addAll(top, bottom);
	  			}
	  			else
	  			{
	  				right = new VBox();
	  				setQuadSize(right);
	  			}

	  			return right;
	  		}
	  	}));
	}

	/**
	 * Sets a pane of the center to 1/4 of window width and height
	 *
	 * @param pane Pane to size
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static void setQuadSize(Region pane)
	{
		pane.setMaxSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight());
		pane.setPrefSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight());

		ChangeListener<Number> onResize = new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				pane.setMaxSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight());
				pane.setPrefSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight());
			}
		};

		pane.widthProperty().addListener(onResize);
		pane.heightProperty().addListener(onResize);
	}

	/**
	 * Sets a pane of the center to 1/4 of window width and a part of height
	 *
	 * @param pane Pane to size
	 * @param height how often u want to split the height
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static void setQuadSize(Region pane, int height)
	{
		pane.setMaxSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height);
		pane.setPrefSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height);

		ChangeListener<Number> onResize = new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				pane.setMaxSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height);
				pane.setPrefSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height);
			}
		};

		pane.widthProperty().addListener(onResize);
		pane.heightProperty().addListener(onResize);
	}

	/**
	 * Sets a pane of the center to 1/4 of window width and a part of height times a certain multiplyer
	 *
	 * @param pane Pane to size
	 * @param height how often you want to split the height
	 * @param multiplyer how often you want to multiply the height
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static void setQuadSize(Region pane, int height, int multiplyer)
	{
		pane.setMaxSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height * multiplyer);
		pane.setPrefSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height * multiplyer);

		ChangeListener<Number> onResize = new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				pane.setMaxSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height * multiplyer);
				pane.setPrefSize(MainScreen.getQuadWidth(), MainScreen.getQuadHeight() / height * multiplyer);
			}
		};

		pane.widthProperty().addListener(onResize);
		pane.heightProperty().addListener(onResize);
	}

	/**
	 * Get the static open party object
	 *
	 * @return party
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static Party getOpenParty() {
		return openParty;
	}
}































