package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class RecipeManipulation extends DatabaseAccess {

	/**
	 * This is needed for RecipeSearch
	 *
	 * @param keyword
	 * @return
	 */
	public static ArrayList<Recipes> searchRecipeByIngriedientsArray(String keyword)
	{
		LinkedList<String> recipes = new LinkedList<String>();
		ArrayList<Recipes> returnRecipeList = new ArrayList<Recipes>();
		String[] keywords = keyword.split(", "); // split keywords into single searches

		con = getInstance();
		if (con != null)
		{
			Statement query;
			try
			{
				query = con.createStatement();
				// search for all recipes, if there is no keyword
				if(keyword.equals(""))
				{
					ResultSet resultRecipe = query.executeQuery("SELECT DISTINCT Recipes.recipeName FROM Recipes INNER JOIN IngredientInRecipe ON Recipes.id = IngredientInRecipe.recipeid INNER JOIN Ingredients ON Ingredients.id = IngredientInRecipe.ingredientid;");
					while(resultRecipe.next())
						recipes.add(resultRecipe.getString(1));
				}
				else
				{
					boolean firstSearch = true;
					for(int i = 0;  i < keywords.length; i++) // search for each keyword in ingredients
					{
						if(!keyword.equals(""))
						{
							ResultSet resultRecipe = query.executeQuery("SELECT Recipes.recipeName FROM Recipes INNER JOIN IngredientInRecipe ON Recipes.id = IngredientInRecipe.recipeid INNER JOIN Ingredients ON Ingredients.id = IngredientInRecipe.ingredientid WHERE LOWER(Ingredients.name) LIKE LOWER('%"+ keywords[i] +"%')");

							if(firstSearch) // if it's the first search, the recipes will be saved in the recipe list
								while(resultRecipe.next())
									recipes.add(resultRecipe.getString(1));
							else // for any other search, check if recipes are matching
							{
								LinkedList<String> tmp = new LinkedList<String>(); // save all found recipes temporary
								while(resultRecipe.next())
									for(int j = 0; j < recipes.size(); j++)
										if(resultRecipe.getString(1).equals(recipes.get(j)))
										{
											tmp.add(resultRecipe.getString(1));
											break;
										}

								recipes = tmp;
							}
							if(recipes.size() != 0)
								firstSearch = false;
						}
					}

					if(recipes.size() == 0) //if recipes are empty, add keywords as recipe names
					{
						for(int i = 0;  i < keywords.length; i++)
						{
							if(!keyword.equals(""))
							{
								recipes.add(keywords[i]);
							}
						}
					}
				}

				// Search for information about each recipe
				for(int i = 0; i < recipes.size(); i++)
				{
					ResultSet resultIngredient = query.executeQuery("SELECT Recipes.recipeName, Ingredients.name, IngredientInRecipe.Amount, Recipes.description  FROM Recipes INNER JOIN IngredientInRecipe ON Recipes.id = IngredientInRecipe.recipeid INNER JOIN Ingredients ON Ingredients.id = IngredientInRecipe.ingredientid WHERE Recipes.recipeName LIKE '%" + recipes.get(i) + "%'");

					String name = "";
					String ingredients = "";
					String amount = "";
					String description = "";
					while(resultIngredient.next())
					{
						name = resultIngredient.getString(1);
						ingredients += resultIngredient.getString(2) + "\n";
						amount += resultIngredient.getString(3) + "\n";
						description = resultIngredient.getString(4);
					}

					if(ingredients.equals(""))
						break;

					returnRecipeList.add(new Recipes(name, ingredients, amount, description));
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		endConnection();
		return returnRecipeList;
	}

	public static LinkedList<String> searchRecipeByIngriedients(String keyword)
	{
		LinkedList<String> recipes = new LinkedList<String>();

		con = getInstance();
		if (con != null)
		{
			Statement query;
			try
			{
				query = con.createStatement();

				// Passende Rezepte auf Suchanfrage auslesen und speichern

				// Suchanfrage an die Datenbank generieren
				String sqlRecipe = "";
				// Wenn kein Suchbegriff eingegeben wurde, werden alle Rezepte angezeigt
				if(keyword.equals(""))
				{
					sqlRecipe = "SELECT DISTINCT Recipes.recipeName FROM Recipes INNER JOIN IngredientInRecipe ON Recipes.id = IngredientInRecipe.recipeid INNER JOIN Ingredients ON Ingredients.id = IngredientInRecipe.ingredientid;";
					ResultSet resultRecipe = query.executeQuery(sqlRecipe);
					while(resultRecipe.next())
						recipes.add(resultRecipe.getString(1));
				}
				// Bei Suche nach Zutaten werden zutreffende Rezepte angezeigt
				else
				{
					String[] keywords = keyword.split(", "); // Suchbegriffe werden getrennt für einzelne Suchanfragen
					boolean first = true;
					for(int i = 0;  i < keywords.length; i++)
					{
						if(!keyword.equals(""))
						{
							sqlRecipe = "SELECT Recipes.recipeName FROM Recipes INNER JOIN IngredientInRecipe ON Recipes.id = IngredientInRecipe.recipeid INNER JOIN Ingredients ON Ingredients.id = IngredientInRecipe.ingredientid WHERE LOWER(Ingredients.name) LIKE LOWER('%"+ keywords[i] +"%');";
							ResultSet resultRecipe = query.executeQuery(sqlRecipe); // Ergebnis der Datenbank

							LinkedList<String> tmp = new LinkedList<String>(); // Liste für die gefunden Rezepte zur aktuellen Zutat
							while(resultRecipe.next()) // Füllen der Rezeptliste und überprüfen ob diese auf die Zutat der vorherigen Suche passen
							{
								if(first)
									recipes.add(resultRecipe.getString(1));
								else
									for(int j = 0; j < recipes.size(); j++)
										if(resultRecipe.getString(1).equals(recipes.get(j)))
											tmp.add(resultRecipe.getString(1));
							}
							if(!first)
								recipes = tmp;
							first = false;
						}
					}
				}

				// doppelte Einträge löschen
				LinkedHashSet<String> hashSet = new LinkedHashSet<String>(recipes);
				recipes.clear();
				recipes.addAll(hashSet);

				// Zutaten für gefundene Rezepte auslesen und dazu speichern
				for(int i = 0; i < recipes.size(); i++)
				{
					String sqlIngredient = "SELECT Ingredients.name, IngredientInRecipe.Amount FROM Recipes INNER JOIN IngredientInRecipe ON Recipes.id = IngredientInRecipe.recipeid INNER JOIN Ingredients ON Ingredients.id = IngredientInRecipe.ingredientid WHERE Recipes.recipeName LIKE '" + recipes.get(i) + "'";
					ResultSet resultIngredient = query.executeQuery(sqlIngredient);  // Ergebnis der Datenbank

					while(resultIngredient.next())
						recipes.set(i, recipes.get(i) + ";" + resultIngredient.getString(1) + ":" + resultIngredient.getString(2));
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		endConnection();
		return recipes;
	}

	/**
	 * Method returns a String Array containing the name and description of the Recipe with the given name.
	 *
	 * @author Team 3, Jakob, Nils (08.11.2017) : SearchRecipeByName(String name)
	 * @author Team 3: Jessica (19.12.2017): searchRecipeByName(String name)
	 */
	public static String[] searchRecipeByName(String name)
	{
		//new UserDatabaseManipulation();
		con = getInstance();
		String[] recipe = new String[2];

		if (con != null)
		{
			Statement query;
			try
			{
				query = con.createStatement();

				String sql = "SELECT recipeName, description FROM Recipes WHERE recipeName LIKE '" + name + "';";

				ResultSet result = query.executeQuery(sql);

				result.next();

				recipe[0] = result.getString("recipeName");
				recipe[1] = result.getString("description");

			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		endConnection();
		return recipe;
	}

	/**
	 * Method returns an ArrayList containing all existing Recipe Names (as Strings).
	 *
	 *  @author Team 3: Jessica (19.12.2017): getRecipeNames()
	 */
	public static ArrayList<String> getRecipeNames()
	{
		//new UserDatabaseManipulation();
		con = getInstance();
		ArrayList<String> recipes = new ArrayList<String>();

		if (con != null)
		{
			Statement query;
			try
			{
				query = con.createStatement();

				String sql = "SELECT recipeName FROM Recipes;";

				ResultSet result = query.executeQuery(sql);

				while(result.next()){
					recipes.add(result.getString("recipeName"));
				}

			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		endConnection();
		return recipes;
	}
}
