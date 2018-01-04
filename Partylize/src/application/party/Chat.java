package application.party;

import application.ChatPage;
import application.Profile;
import javafx.scene.layout.VBox;

public class Chat
{
	public static VBox create()
	{
		VBox chat = new VBox(ChatPage.startChat(new Profile(-1, "")));	//Insert party id here if chat ever exists
		Party.setQuadSize(chat);
		return chat;
	}
}