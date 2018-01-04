package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import application.PasswordStorage.CannotPerformOperationException;
import application.PasswordStorage.InvalidHashException;

public class ProfileManipulation extends DatabaseAccess {

	public static boolean findUsername(String usernameToFind) {

		boolean usernameFound = false;

		con = getInstance();

		if (con != null) {
			// Abfrage-Statement erzeugen.
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT COUNT(*) FROM Profile WHERE username LIKE '" + usernameToFind + "' ";
				ResultSet result = query.executeQuery(sql);

				while(result.next()){
					if(result.getInt("COUNT(*)") == 1){
						usernameFound = true;
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
		return usernameFound;
	}

	public static boolean checkPassword(String usernameToFind, String passwordToMatch) {
		boolean passwordMatched = false;
		con = getInstance();
		if (con != null) {
			// Abfrage-Statement erzeugen.
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT password FROM Profile WHERE username LIKE '" + usernameToFind + "' ";
				ResultSet result = query.executeQuery(sql);

				result.next();
				String hashPW = result.getString("password");

				if (PasswordStorage.verifyPassword(passwordToMatch, hashPW))
					passwordMatched = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (CannotPerformOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidHashException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		endConnection();
		return passwordMatched;
	}

	public static boolean checkLoginStatus(String usernameToFind) {

		boolean alreadyLoggedin = false;

		con = getInstance();

		if (con != null) {
			// Abfrage-Statement erzeugen.
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT loginStatus FROM Profile WHERE username LIKE '" + usernameToFind + "' ";
				ResultSet result = query.executeQuery(sql);

				result.next();

				if (result.getInt("loginStatus") == 1)
					alreadyLoggedin = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
		return alreadyLoggedin;
	}

	public static void updateLoginStatus(String userToChange, boolean newLoginStatus) {
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				query.execute("START TRANSACTION;");
				query.execute("LOCK TABLES Profile WRITE;");

				if (newLoginStatus)
					sql = "UPDATE Profile SET loginStatus = 1 WHERE username LIKE '" + userToChange + "';";
				else
					sql = "UPDATE Profile SET loginStatus = 0 WHERE username LIKE '" + userToChange + "';";

				query.execute(sql);
				query.execute("COMMIT;");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
	}

	/**
	 *
	 * @return 0: E-Mail, 1: Password
	 */
	public static String[] getUserData(int userId) {

		String[] userData = new String[2];
		con = getInstance();

		if (con != null) {
			// Abfrage-Statement erzeugen.
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT `eMail`, `password` FROM `Profile` WHERE id = " + userId+";";
				ResultSet result = query.executeQuery(sql);

				while(result.next()){
					userData[0] = result.getString("eMail");
					userData[1] = result.getString("password");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
		return userData;
	}

	public static void changeEMail(String newEMail) {
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				query.execute("START TRANSACTION;");
				query.execute("LOCK TABLES Profile WRITE;");

				sql = "UPDATE Profile SET eMail = '" + newEMail + "' WHERE username LIKE '" + Partylize.getUsername()
						+ "';";

				query.execute(sql);
				query.execute("COMMIT;");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
	}

	public static void changePassword(String newPassword){
		con = getInstance();
		if (con != null) {
			Statement query;
			try {
				String hashedNewPassword = PasswordStorage.createHash(newPassword);

				query = con.createStatement();

				String sql = "";

				query.execute("START TRANSACTION;");
				query.execute("LOCK TABLES Profile WRITE;");

				sql = "UPDATE Profile SET password = '" + hashedNewPassword + "' WHERE username LIKE '"
						+ Partylize.getUsername() + "';";

				query.execute(sql);
				query.execute("COMMIT;");

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (CannotPerformOperationException e) {
				e.printStackTrace();
			}
		}
		endConnection();
	}

	/**
	 * Created by Tobias Goldlücke On 08.11.2017 Last Edited by Tobias Goldlücke
	 * On 14.11.017
	 *
	 * @param id
	 * @param usernameToRegister
	 * @param emailToRegister
	 * @param passwordToRegister
	 * @throws CannotPerformOperationException
	 *
	 * @editor Daniel Hecker : 26.12.2017
	 */
	public static void registerUser(int id, String usernameToRegister, String emailToRegister, String passwordToRegister){
		con = getInstance();
		if (con != null) {
			Statement query;
			try {
				String hashedPassword = PasswordStorage.createHash(passwordToRegister);

				query = con.createStatement();

				String sql = "";

				query.execute("START TRANSACTION;");
				query.execute("LOCK TABLES Profile WRITE;");
				sql = "INSERT INTO Profile VALUES(" + id + ",'" + usernameToRegister + "','" + emailToRegister + "','"
						+ hashedPassword + "','',0,0,'',0)";
				query.execute(sql);
				query.execute("COMMIT;");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (CannotPerformOperationException e){
				e.printStackTrace();
			}
		}
		endConnection();
	}

	/**
	 * Created by Tobias Goldlücke On 08.11.2017 Last Edited by Tobias Goldlücke
	 * On 14.11.017
	 */
	public static void updateVerfication(String userToChange, boolean isVerfied) {
		if (isVerfied == true) {
			con = getInstance();

			if (con != null) {
				Statement query;
				try {
					query = con.createStatement();

					String sql = "";

					query.execute("START TRANSACTION;");
					query.execute("LOCK TABLES Profile WRITE;");

					sql = "UPDATE Profile SET isVerified = 1 WHERE username LIKE '" + userToChange + "';";

					query.execute(sql);
					query.execute("COMMIT;");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			endConnection();
		} else {
			System.out.println("User is not verfied");
		}
	}

	/**
	 * Created by Tobias Goldlücke On 08.11.2017 Last Edited by Tobias Goldlücke
	 * On 14.11.017
	 */
	public static boolean checkKey(String usernameToFind, String keyToCheck) {
		boolean verfied = false;
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				sql = "SELECT verifyKey FROM Profile WHERE username LIKE '" + usernameToFind + "' ";
				ResultSet result = query.executeQuery(sql);
				result.next();
				String verficationString = result.getString(1);
				if (keyToCheck.matches(verficationString))
					verfied = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
		return verfied;
	}

	/**
	 * Created by Tobias Goldlücke On 08.11.2017 Last Edited by Tobias Goldlücke
	 * On 14.11.017
	 */
	public static boolean isVerfied(String usernameToFind) {
		boolean isVerfied = false;
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				sql = "SELECT isVerified FROM Profile WHERE username LIKE '" + usernameToFind + "' ";
				ResultSet result = query.executeQuery(sql);
				result.next();
				String verficationString = result.getString(1);
				int verficationInt = Integer.parseInt(verficationString);
				if (verficationInt == 1) {
					isVerfied = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
		return isVerfied;
	}

	// ---------------------------------------------------------- remove Friend
	// (Chris) -------------------------------------

	// -------------------------------------------------------------------------
	// Remove Friend (Alex) --------------------------------------

	public static void declineFriend(String usernameToDecline) {

		int ownID = getUserId(true);

		int declinedProfilID = getUserId(usernameToDecline, true);

		con = getInstance();

		if (con != null) {
			Statement query;
			try {

				query = con.createStatement();

				String sql = "";

				sql = "DELETE FROM isFriendsWith WHERE ProfilID LIKE " + declinedProfilID + " AND FriendProfilID = "
						+ ownID + " AND requestConfirmed = 0;";

				query.execute(sql);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();

	}

	public static void acceptFriend(String usernameToAccept) {

		int ownID = getUserId(true);

		int acceptedProfilID = getUserId(usernameToAccept, true);

		con = getInstance();

		if (con != null) {
			Statement query;
			try {

				query = con.createStatement();

				String sql = "";

				sql = "UPDATE isFriendsWith SET requestConfirmed = 1 WHERE ProfilID =" + acceptedProfilID;

				query.execute(sql);

				sql = "INSERT INTO isFriendsWith VALUES (" + ownID + ", " + acceptedProfilID + ", 1)";
				query.execute(sql);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
	}

	/**
	 * Created by Alexander Gevla On 21.11.2017 Last Edited by Alexander Gevla
	 * On 21.11.2017
	 */
	public static void removeFriend(String usernameToRemove) {

		int ownID = getUserId(true);

		int userToRemoveID = getUserId(usernameToRemove, true);

		con = getInstance();
		String sql = "";
		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				sql = "SELECT * FROM isFriendsWith WHERE FriendProfilID = " + userToRemoveID + " AND ProfilID = "
						+ ownID + " AND requestConfirmed = 1;";

				ResultSet result = query.executeQuery(sql);

				if (result.next()) {

					query.execute("START TRANSACTION;");
					query.execute("LOCK TABLES isFriendsWith WRITE;");
					query.execute("DELETE FROM isFriendsWith WHERE FriendProfilID LIKE " + userToRemoveID
							+ " AND ProfilID = " + ownID + " AND requestConfirmed = 1;");

					query.execute("DELETE FROM isFriendsWith WHERE FriendProfilID LIKE " + ownID + " AND ProfilID = "
							+ userToRemoveID + " AND requestConfirmed = 1;");

					query.execute("COMMIT;");

				} else {
					// Fehler freund nicht vorhanden
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			endConnection();
		}
	}

	/**
	 * @author Levin Averkamp (21.11.2017)
	 * @author Levin Averkamp (05.12.2017)
	 */
	public static boolean checkFriendStatus(String usernameToFind) {
		boolean alreadyFriends = false;
		int usernameToFindID = getUserId(usernameToFind, true);
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT * FROM isFriendsWith WHERE FriendProfilID LIKE('" + usernameToFindID
						+ "') OR ProfilID LIKE('" + usernameToFindID + "');";
				ResultSet result = query.executeQuery(sql);

				result.next();

				if (result.next())
					alreadyFriends = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
		return alreadyFriends;
	}

	/**
	 * @author Levin Averkamp (21.11.2017)
	 * @author Levin Averkamp (05.12.2017)
	 */
	public static void addFriend(String usernameToAdd) {
		boolean friendStatus = checkFriendStatus(usernameToAdd);

		int ownID = getUserId(true);
		int userToAddID = getUserId(usernameToAdd, true);

		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				if (!friendStatus) {
					query = con.createStatement();

					query.execute("START TRANSACTION;");
					query.execute("LOCK TABLES isFriendsWith WRITE;");
					query.execute("INSERT INTO isFriendsWith (ProfilID, FriendProfilID) VALUES ('" + ownID + "', '"
							+ userToAddID + "');");
					query.execute("COMMIT;");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
	}

	/**
	 * Method to update option "sound on/off" in database for logged-in user.
	 *
	 * @param loggedInUsername
	 *            userName for whom sound preferences are about to be updated
	 * @param sound
	 *            sound option, can be turned on (true) or off (false)
	 * @author Team3, Leo (08.11.2017)
	 **/
	public static void updateSoundPreference(String loggedInUsername, boolean sound) {
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				query.execute("START TRANSACTION;");
				query.execute("LOCK TABLES Profile WRITE;");
				if (sound)
					sql = "UPDATE Profile Set soundStatus = 1 WHERE username like '" + loggedInUsername + "';";
				else {
					sql = "UPDATE Profile Set soundStatus = 0 WHERE username like '" + loggedInUsername + "';";
				}
				query.execute(sql);
				query.execute("COMMIT;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		endConnection();
	}

	/**
	 * Method to get current user's Id.
	 *
	 * @return current user's id
	 * @author Team3, Viola (05.12.2017)
	 *
	 * @editor Daniel Hecker : 26.12.2017
	 */
	public static int getUserId(boolean connect) {
		int userId = -1;

		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT id FROM Profile WHERE LOWER(username) LIKE LOWER('" + Partylize.getUsername() + "')";
				ResultSet result = query.executeQuery(sql);

				while(result.next()){
					userId = result.getInt("id");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return userId;
	}

	/**
	 * Method to find the corresponding userId to an userName.
	 *
	 * @param userName
	 *            userName whose id is to be found
	 * @return userId of given userName
	 * @author Team3, Viola (06.12.2017)
	 */
	public static int getUserId(String userName, boolean connect) {
		int userId = -1;
		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT id FROM Profile WHERE LOWER(username) LIKE LOWER('" + userName + "');";
				ResultSet result = query.executeQuery(sql);

				while(result.next()){
					userId = result.getInt("id");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return userId;
	}

	/**
	 * Method to find the corresponding userName to an userId.
	 *
	 * @param userId
	 *            userId whose name is to be found
	 * @return userName of given userId
	 * @author Team3, Viola (06.12.2017) //FROM Profile <- denk da bitte dran (ÜBERALL)
	 */
	public static String getUserName(int userId, boolean connect) {
		String userName = "";

		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT username FROM Profile WHERE id = " + userId + ";";
				ResultSet result = query.executeQuery(sql);

				while(result.next()){
					userName = result.getString("username");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(connect)
			endConnection();

		return userName;
	}

	/**
	 *
	 * <b>checkLogin</b>
	 * <p>
	 * <code>public static boolean checkLogin({@link String} username, {@link String} password)</code>
	 * <p>
	 * Checks if the passed username and password from {@link LoginScreen} are valid
	 * and if the user is currently logged in.
	 * <p>
	 * Each check is based on the one before,
	 * so that the procedure is sequential and no check is done unnecessary.
	 * <ul>
	 * <li>First {@link UserDatabaseManipulation#findUsername(String)} is called and checked, if it is true.
	 * <li>Then {@link UserDatabaseManipulation#checkPassword(String, String)} is called and checked, if it is true.
	 * <li>Lastly {@link UserDatabaseManipulation#checkLoginStatus(String) }is called and checked, if it is true.
	 * </ul>
	 * When all checks return true the boolean acceptable will be set as true,
	 * indicating that the login was a success.
	 * This method will return immediately in all cases even when one check passes as false.
	 * <p>
	 * If one of the checks fails all other checks which,
	 * would come afterwards will not be executed and the boolean stays as false
	 * and the login is unsuccessful.
	 * In addition to that a corresponding error message will be printed on the console,
	 * to help the user find the error in his or her input on the {@link LoginScreen}.
	 *
	 * @see UserDataBaseManipulation
	 * @see LoginScreen
	 * @param username the username to find in the database
	 * @param password the corresponding password to the username in the database
	 * @return <b>acceptable</b> if the login data is valid true if not false
	 * */
	public static boolean checkLogin(String username, String password)
	{
		boolean acceptable = false;
		// ----------------------------------------Datenbankabfrage---------------------

		if(findUsername(username))
			if(checkPassword(username, password))
				if(!checkLoginStatus(username))
					acceptable = true;
				else
					System.out.println("Dieser Nutzer ist bereits angemeldet!");
			else
				System.out.println("Benutzername oder Passwort inkorrekt!");
		else
			System.out.println("Benutzername oder Passwort inkorrekt!");

		return acceptable;
	}

	/**
	 * Gets all friends of the current user
	 *
	 * @param connect Need an extra connection to the database?
	 * @return LinkedList with Profiles
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static LinkedList<Profile> getFriends(boolean connect)
	{
		if(connect)
			con = getInstance();

		LinkedList<Profile> list = new LinkedList<Profile>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT friendProfilId FROM isFriendsWith "
						+"WHERE profilId = " + ProfileManipulation.getUserId(false) + " "
						+"AND requestConfirmed = 1;";


				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					list.add(new Profile(
							results.getInt("friendProfilId"),
							ProfileManipulation.getUserName(results.getInt("friendProfilId"), false))
						);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return list;
	}
}























