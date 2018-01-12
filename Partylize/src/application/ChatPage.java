package application;

import application.party.Party;
/**
 * Author:	Felix Stanlein
 * created: 22.11.2017
 * last edited: 6.12.2017
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChatPage
{
	static Stage chatPageWindow;
	static boolean isOpen=false;

	/**
	 *
	 * @param chatpartnername
	 * @return
	 *Halllo i bims der Schulbus
	 * @editor Daniel Hecker : 28.12.2017
	 */
	public static GridPane startChat(Profile chatpartnername)
	{
		/* declaration and initialization */
		GridPane gridPane = new GridPane(); // container for chat objects
		ListView<String> chatListView = new ListView<String>(); // Shows the chat
		ObservableList<String> chatTexts = FXCollections.observableArrayList(chatpartnername+":\tHallo", "Du:\tHi"); // chat history
		TextArea chatSendTextArea = new TextArea(); // Here you write the text, you want to send
		Label chatlbl = new Label("Chatparner: " + chatpartnername.toString()); // chatpartnerview
		Button btnSend = new Button("Send");

		/* style and size */
		chatlbl.setStyle("-fx-font: 30px Verdana;-fx-font-weight: bold;-fx-text-fill: #0B610B;");
		btnSend.setStyle("-fx-font: 20px Verdana");

		chatListView.setItems(chatTexts);

		Party.setQuadSize(chatListView, 4, 3);

		Party.setQuadSize(chatSendTextArea, 4, 1);

		btnSend.setPrefSize(75, 30);
		btnSend.setMaxWidth(100);
		gridPane.setVgap(20); // padding between the rows

		gridPane.setPadding(new Insets(20));

		/* GridPaneLayout */
		gridPane.getChildren().add(chatlbl);
		GridPane.setConstraints(chatlbl, 0, 0, 1, 1);
		gridPane.getChildren().add(chatListView);
		GridPane.setConstraints(chatListView, 0, 1, 1, 1);
		gridPane.getChildren().add(chatSendTextArea);
		GridPane.setConstraints(chatSendTextArea, 0, 2, 1, 1);
		gridPane.getChildren().add(btnSend);
		GridPane.setConstraints(btnSend, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER);

		GridPane center = new GridPane();
		center.add(gridPane, 0, 0);
		center.setAlignment(Pos.CENTER);

		return center;
	}
}




















