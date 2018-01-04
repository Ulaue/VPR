/**
* This class handles every function concerning a party.
* @author Team3 (22.12.2017): Functionality private Party
*/

package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import application.party.Party;

public class PartyManipulation extends DatabaseAccess
{
	/**
	 * Method to create a party and add the owner to it.
	 *
	 * @author Team3, Sven (19.11.2017)
	 * @author Team3, Viola (19.11.2017)
	 * @editor Daniel Hecker : 27.12.2017
	 */
	public static int createParty(Party party)
	{
		con = getInstance();

		int id = -1;

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				id = DatabaseAccess.getHighestId("Party", false);

				//Create party
				String sql = "INSERT INTO Party (id, date, time, name, street, town, owner) "
						+ "VALUES("+id+", "
						+ "'"+party.getDateAndTime()[0]+"', "
						+ "'"+party.getDateAndTime()[1]+"', "
						+ "'"+party.getLocation()[0]+"', "
						+ "'"+party.getLocation()[1]+"', "
						+ "'"+party.getLocation()[2]+"', "
						+ party.getOwner()+");";

				query.execute(sql);

				//Add owner to his own party
				if(party.getPartyType() == PartyType.PUBLIC){
					sql = "INSERT INTO ProfileInPublicParty (profileId, partyId) "
							+ "VALUES("+party.getOwner()+", "+id+");";
				}else if(party.getPartyType() == PartyType.PRIVATE){
					sql = "INSERT INTO ProfileInPrivateParty (profileId, partyId) "
							+ "VALUES("+party.getOwner()+", "+id+");";
				}

				query.execute(sql);


			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		endConnection();

		return id;
	}

	/**
	 * Updates ALL information of a party except the owner and the id
	 *
	 * @param party Party with the information the party shall be changed to
	 *
	 * @author Team 3, Leo (13.12.2017)
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static void updatePartyInformation(Party party)
	{
		con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				sql = "UPDATE Party SET "
						+ "date = '"+party.getDateAndTime()[0]+"', "
						+ "time = '"+party.getDateAndTime()[1]+"', "
						+ "name = '"+party.getLocation()[0]+"', "
						+ "street = '"+party.getLocation()[1]+"', "
						+ "town = '"+party.getLocation()[2]+"' "
						+ "WHERE id = "+party.getId()+";";

				query.execute(sql);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		endConnection();
	}

	/**
	 * Get all participants in a party
	 *
	 * @param party which party
	 * @param exceptOwner shall the owner be listed? (true -> no owner, false -> with owner)
	 * @param connect need extra db connection?
	 * @return List with Profiles
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static LinkedList<Profile> getParticipantsInParty(Party party, boolean exceptOwner, boolean connect)
	{
		if(connect)
			con = getInstance();

		LinkedList<Profile> list = new LinkedList<Profile>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				//Differences bewtween Public and Private Party
				if(party.getPartyType() == PartyType.PRIVATE){
					sql = "SELECT profileId FROM ProfileInPrivateParty ";
				}else if(party.getPartyType() == PartyType.PUBLIC){
					sql = "SELECT profileId FROM ProfileInPublicParty ";
				}

				//String for both
				sql += "WHERE partyId = " + party.getId();

				//If the owner shall be listed or not
				if(exceptOwner){
					sql += " AND profileId != "+party.getOwner()+";";
				}else{
					sql += ";";
				}

				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					list.add(new Profile(results.getInt("profileId"), ProfileManipulation.getUserName(results.getInt("profileId"), false)));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return list;
	}

	/**
	 * Gets all partys of a type
	 *
	 * @param pt PartyType
	 * @return List with Partys
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static LinkedList<Party> getPartys(PartyType pt)
	{
		con = getInstance();

		LinkedList<Party> list = new LinkedList<Party>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				if(pt == PartyType.PRIVATE){
					sql = "SELECT Party.id, Party.name, Party.owner FROM Party "
							+"INNER JOIN ProfileInPrivateParty ON Party.id = ProfileInPrivateParty.partyId "
							+"INNER JOIN Profile ON ProfileInPrivateParty.profileId = Profile.id WHERE Profile.id = " + ProfileManipulation.getUserId(false) + ";";
				}else if(pt == PartyType.PUBLIC){
					sql = "SELECT DISTINCT Party.id, Party.name, Party.owner FROM Party "
							+"INNER JOIN ProfileInPublicParty ON Party.id = ProfileInPublicParty.partyId;";
				}

				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					list.add(new Party(
							results.getInt("id"),
							results.getString("name"),
							results.getInt("owner"),
							pt
						));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		endConnection();

		return list;
	}

	/**
	 * Get every information on a party
	 *
	 * @param party A less complete party object
	 * @return A complete party object
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static Party getPartyInformation(Party party)
	{
		con = getInstance();

		Party p = null;

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				sql = "SELECT id, date, time, name, street, town, owner FROM Party "
						+"WHERE id = " + party.getId() + ";";

				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					p = new Party(
							results.getInt("id"),
							results.getString("date"),
							results.getString("time"),
							results.getString("name"),
							results.getString("street"),
							results.getString("town"),
							results.getInt("owner"),
							party.getPartyType()
						);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		endConnection();

		return p;
	}

	/**
	 * Is the user in the public party that is open
	 *
	 * @param connect Need extra db connection?
	 * @return true if he is, false if he isn't
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static boolean isUserInPublicParty(boolean connect)
	{
		if(connect)
			con = getInstance();

		boolean isUser = false;

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				sql = "SELECT * FROM ProfileInPublicParty "
						+"WHERE profileId = " + ProfileManipulation.getUserId(false) + " "
						+"AND partyId = "+Party.getOpenParty().getId()+";";

				ResultSet results = query.executeQuery(sql);

				if (results.next()){
					isUser = true;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return isUser;
	}

	/**
	 * Get all friends that aren't in the privateParty
	 *
	 * @param party Party to check
	 * @return List of Friends (Person)
	 *
	 * @author Daniel Hecker : 26.12.2017
	 */
	public static LinkedList<Profile> getFriendsNotInParty(Party party)
	{
		con = getInstance();

		LinkedList<Profile> list = new LinkedList<Profile>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				//Get participants
				LinkedList<Profile> participants = PartyManipulation.getParticipantsInParty(party, false, false);

				String finder = "";
				for (int i = 0; i < participants.size(); i++)
				{
					finder += participants.get(i).getId();

					if(i < participants.size()-1){
						finder += ", ";
					}
				}

				if(participants.isEmpty())
					finder = "0";

				//Gets all friends that aren't in this party
				String sql = "SELECT friendprofilId FROM isFriendsWith "
						+"WHERE profilId = " + ProfileManipulation.getUserId(false) + " "
						+"AND friendprofilId NOT IN (" + finder + ");";

				ResultSet results = query.executeQuery(sql);

				//Adds friends as Persons to list
				while (results.next())
				{
					list.add(new Profile(results.getInt("friendprofilId"), ProfileManipulation.getUserName(results.getInt("friendprofilId"), false)));
				}


			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		endConnection();

		return list;
	}

	/**
	 * Adds a List of friends to a party
	 *
	 * @param friends Friends that shall be added
	 * @param party The party they shall be added to
	 * @param connect Need extra db connection?
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void addFriendsToParty(LinkedList<Profile> friends, Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String partyType = "";
				if(party.getPartyType() == PartyType.PUBLIC){
					partyType = "ProfileInPublicParty";
				}else if(party.getPartyType() == PartyType.PRIVATE){
					partyType = "ProfileInPrivateParty";
				}

				String sql = "";

				for (int i = 0; i < friends.size(); i++)
				{
					sql = "INSERT INTO "+partyType+" (profileId, partyId) "
							+ "VALUES("+friends.get(i).getId()+", "+party.getId()+");";

					query.execute(sql);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();
	}

	/**
	 * Remove a List of participants from a party
	 *
	 * @param participants Participants that shall be removed
	 * @param party The party they shall be removed from
	 * @param connect Need extra db connection?
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void removeParticipantsFromParty(LinkedList<Profile> participants, Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String partyType = "";
				if(party.getPartyType() == PartyType.PUBLIC){
					partyType = "ProfileInPublicParty";
				}else if(party.getPartyType() == PartyType.PRIVATE){
					partyType = "ProfileInPrivateParty";
				}

				String sql = "";

				for (int i = 0; i < participants.size(); i++)
				{
					sql = "DELETE FROM "+partyType+" "
							+ "WHERE profileId = "+participants.get(i).getId()+" "
							+ "AND partyId = "+party.getId()+";";

					query.execute(sql);

					removeGiftsFromParty(getGiftsAdded(GiftType.INGREDIENT, participants.get(i).getId(), party, false), party, false);

					removeGiftsFromParty(getGiftsAdded(GiftType.SNACK, participants.get(i).getId(), party, false), party, false);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();
	}

	/**
	 * Gets all gifts of a type
	 *
	 * @param gt giftType
	 * @param party In what party to the gifts exist
	 * @return List of Gifts
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static LinkedList<Gift> getGiftsInParty(GiftType gt, Party party)
	{
		con = getInstance();

		LinkedList<Gift> list = new LinkedList<Gift>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				if(gt == GiftType.INGREDIENT){
					sql = "SELECT id, profileId, partyId, drinksId FROM BringerIngredients "
							+"WHERE partyId = " + party.getId() + ";";
				}else if(gt == GiftType.SNACK){
					sql = "SELECT id, profileId, partyId, snackId FROM BringerSnacks "
							+"WHERE partyId = " + party.getId() + ";";
				}

				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					list.add(new Gift(
							results.getInt("id"),
							results.getInt("profileId"),
							results.getInt("partyId"),
							((gt == GiftType.INGREDIENT) ? results.getInt("drinksId") : results.getInt("snackId")),
							gt)
						);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		endConnection();

		return list;
	}

	/**
	 * Gets a gift name by id
	 *
	 * @param id Gift id
	 * @param connect Needs a separate database connection?
	 * @param gt giftType
	 * @return Gift name
	 *
	 * @author Daniel Hecker : 27.12.2017
	 */
	public static String getGiftName(int id, boolean connect, GiftType gt)
	{
		String name = "";

		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				if(gt == GiftType.INGREDIENT){
					sql = "SELECT name FROM Ingredients WHERE id = " + id + ";";
				}else if(gt == GiftType.SNACK){
					sql = "SELECT name FROM Snack WHERE id = " + id + ";";
				}

				ResultSet result = query.executeQuery(sql);

				while(result.next())
				{
					name = result.getString("name");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return name;
	}

	/**
	 * Get all gifts that have been added to the party by the current user
	 *
	 * @param gt Type of gift
	 * @param connect Need extra db connection?
	 * @return List of Gifts
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static LinkedList<Gift> getGiftsAdded(GiftType gt, int userId, Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		LinkedList<Gift> list = new LinkedList<Gift>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				if(gt == GiftType.INGREDIENT){
					sql = "SELECT id, drinksId FROM BringerIngredients "
							+"WHERE profileId = "+userId+" "
							+"AND partyId = " + party.getId() + ";";
				}else if(gt == GiftType.SNACK){
					sql = "SELECT id, snackId FROM BringerSnacks "
							+"WHERE profileId = "+userId+" "
							+"AND partyId = " + party.getId() + ";";
				}

				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					list.add(new Gift(
							results.getInt("id"),
							((gt == GiftType.INGREDIENT) ? results.getInt("drinksId") : results.getInt("snackId")),
							gt)
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

	/**
	 * Get all gifts that haven't been added to the party by the current user
	 *
	 * @param gt Type of gift
	 * @param connect Need extra db connection?
	 * @return List of Gifts
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static LinkedList<Gift> getGiftsNotAdded(GiftType gt, int userId, Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		LinkedList<Gift> list = new LinkedList<Gift>();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();
				String sql = "";

				//Get added gifts
				LinkedList<Gift> addedGifts = getGiftsAdded(gt, userId, party, false);

				String finder = "";
				for (int i = 0; i < addedGifts.size(); i++)
				{
					finder += addedGifts.get(i).getId();

					if(i <= addedGifts.size() -2){
						finder += ", ";
					}
				}

				if(addedGifts.isEmpty()){
					finder = "0";
				}

				//Substrate added gifts from all gifts
				if(gt == GiftType.INGREDIENT){
					sql = "SELECT id, name FROM Ingredients "
							+"WHERE id NOT IN (" + finder + ");";
				}else if(gt == GiftType.SNACK){
					sql = "SELECT id, name FROM Snack "
							+"WHERE id NOT IN (" + finder + ");";
				}

				ResultSet results = query.executeQuery(sql);

				while (results.next())
				{
					list.add(new Gift(
							results.getInt("id"),
							results.getString("name"),
							gt)
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

	/**
	 * Adds a list of gifts to a party
	 *
	 * @param list Gifts
	 * @param party Party, Gifts shall be added to
	 * @param connect Need extra db connection?
	 *
	 * @author Team3, Mareike (20.12.2017)
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void addGiftsToParty(LinkedList<Gift> list, Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				int id;

				if(!list.isEmpty())
				{
					String table = "";
					String rowGift = "";
					if(list.get(0).getType() == GiftType.INGREDIENT){
						table = "BringerIngredients";
						rowGift = "drinksId";
					}else if(list.get(0).getType() == GiftType.SNACK){
						table = "BringerSnacks";
						rowGift = "snackId";
					}

					for (int i = 0; i < list.size(); i++)
					{
						id = DatabaseAccess.getHighestId(table, false);

						String sql = "INSERT INTO "+table+" (id, profileId, "+rowGift+", partyId) "
								+ "VALUES("+id+", "
								+ ProfileManipulation.getUserId(false)+", "
								+ list.get(i).getGiftId()+", "
								+ party.getId()+");";

						query.execute(sql);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();
	}

	/**
	 * Removes a list of gifts from a party
	 *
	 * @param list Gifts
	 * @param party Party, Gifts shall be removed from
	 * @param connect Need extra db connection?
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void removeGiftsFromParty(LinkedList<Gift> list, Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String table = "";
				if(!list.isEmpty())
				{
					if(list.get(0).getType() == GiftType.INGREDIENT){
						table = "BringerIngredients";
					}else if(list.get(0).getType() == GiftType.SNACK){
						table = "BringerSnacks";
					}

					for (int i = 0; i < list.size(); i++)
					{
						String sql = "DELETE FROM "+table+" WHERE id = "+list.get(i).getId()+";";

						query.execute(sql);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();
	}

	/**
	 * Deletes a party completely
	 *
	 * @param party Party to delete
	 * @param connect Need extra db connection?
	 *
	 * @author Sven (20.12.2017)
	 * @author Viola (22.12.2017)
	 * @author Daniel Hecker : 28.12.2017
	 */
	public static void deleteParty(Party party, boolean connect)
	{
		if(connect)
			con = getInstance();

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "";

				sql = "DELETE FROM Party WHERE id = "+party.getId()+";";
				query.execute(sql);

				sql = "";
				if(party.getPartyType() == PartyType.PRIVATE){
					sql = "DELETE FROM ProfileInPrivateParty WHERE partyId = "+party.getId()+";";
				}else if(party.getPartyType() == PartyType.PUBLIC){
					sql = "DELETE FROM ProfileInPublicParty WHERE partyId = "+party.getId()+";";
				}
				query.execute(sql);

				if(party.getPartyType() == PartyType.PRIVATE)
				{
					sql = "DELETE FROM BringerIngredients WHERE partyId = "+party.getId()+";";
					query.execute(sql);

					sql = "DELETE FROM BringerSnacks WHERE partyId = "+party.getId()+";";
					query.execute(sql);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();
	}


}




































