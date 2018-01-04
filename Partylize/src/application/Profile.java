package application;

public class Profile
{
	private int id;
	private String name;

	public Profile(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public String toString()
	{
		return name;
	}
}