package application;

import javafx.scene.control.Label;

//notifications work like labels, but contain a type and use a unique CSS style
public class Notification extends Label {
	int noteType; // TODO noteTypes are 0=friend request, 1=invitation, 2=private
					// message, 3=groupchat message

	public int getNoteType() {
		return noteType;
	}

	public void setNoteType(int noteType) {
		this.noteType = noteType;
	}

	public Notification() {
		this.getStyleClass().add("notification");
	}

	public Notification(int noteType) {
		this.noteType = noteType;
		this.getStyleClass().add("notification");
	}

	public Notification(int noteType, String text) {
		this.noteType = noteType;
		this.setText(text);
		this.getStyleClass().add("notification");
	}

}
