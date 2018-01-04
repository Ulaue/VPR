package application;

/**
 * This represents can gift or a gift-connection to party and bringer
 *
 * @author Daniel Hecker : 28.12.2017
 */
public class Gift
{
	private int id;
	private int profileId;
	private int partyId;
	private int giftId = -1;
	private String giftName = null;
	private GiftType type;

	/**
	 * Connection
	 *
	 * @param id id of connection
	 * @param giftId id of gift
	 * @param type gifttype
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public Gift(int id, int giftId, GiftType type)
	{
		this.id = id;
		this.giftId = giftId;
		this.type = type;
	}

	/**
	 * Gift
	 *
	 * @param giftId id of gift
	 * @param giftName name of gift
	 * @param type gifttype
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public Gift(int giftId, String giftName, GiftType type)
	{
		this.giftId = giftId;
		this.giftName = giftName;
		this.type = type;
	}

	/**
	 * Connection and Gift
	 *
	 * @param id id of connection
	 * @param profileId id of profile
	 * @param partyId id of party
	 * @param giftId id of gift
	 * @param type gifttype
	 *
	 * @author Daniel Hecker : 28.12.2017
	 */
	public Gift(int id, int profileId, int partyId, int giftId, GiftType type)
	{
		this.id = id;
		this.profileId = profileId;
		this.partyId = partyId;
		this.giftId = giftId;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public int getProfileId() {
		return profileId;
	}

	public int getPartyId() {
		return partyId;
	}

	public int getGiftId()
	{
		return giftId;
	}

	public String getGiftName()
	{
		if(giftName != null)
			return giftName;
		else
			return PartyManipulation.getGiftName(giftId, true, type);
	}

	public GiftType getType() {
		return type;
	}

	public String toString()
	{
		return getGiftName()+" ("+ProfileManipulation.getUserName(profileId, true)+")";
	}
}
