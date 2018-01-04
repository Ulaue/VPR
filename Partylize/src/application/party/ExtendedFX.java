package application.party;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.Node;

public interface ExtendedFX
{
	/**
	 * Gets the current time and date.<br>
	 * 0: year<br>
	 * 1: month<br>
	 * 2: day<br>
	 * 3: hour<br>
	 * 4: minute<br>
	 * 5: second
	 *
	 * @return Int-array that contains date and time<br>
	 * The specific indices are listed above
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	public static int[] currentTimeAndDate()
	{
		//Timeformat
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");

		//Current Time and Date
		LocalDateTime now = LocalDateTime.now();

		//Applying Timeformat on Current Time and Date
		String[] currentDateAndTimeString = dtf.format(now).split("/");

		//String -> int
		int[] currentDateAndTime = new int[currentDateAndTimeString.length];

		for (int i = 0; i < currentDateAndTimeString.length; i++) {
			currentDateAndTime[i] = Integer.parseInt(currentDateAndTimeString[i]);
		}

		return currentDateAndTime;
	}

	/**
	 * Makes a Node completely visible or invisible and the space it takes is reduced to nothing or to full size.
	 *
	 * @param node The Node the the actions shall be used on
	 * @param value true or false, depending if set visible or invisible
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	public static void setNodeVisible(Node node, boolean value)
	{
		node.setVisible(value);
		node.setManaged(value);
	}
}


























