
package application;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Team2, Robert
 * class to create objects of every recipes with its name, ingredients and amount
 * this objects are used to import them into the tableView in the RecipeSearchWindow
 */
public class Recipes
{
    private SimpleStringProperty recipeName;
    private SimpleStringProperty ingredient;
    private SimpleStringProperty amount;
    private SimpleStringProperty description;
 
    public Recipes(String name, String ingredient, String amount, String description)
    {
        this.recipeName = new SimpleStringProperty(name);
        this.ingredient = new SimpleStringProperty(ingredient);
        this.amount = new SimpleStringProperty(amount);
        this.description = new SimpleStringProperty(description);
    }
    
    public String toString()
    {
		return recipeName.get();   	
    }

	public String getRecipeName()
    {
        return recipeName.get();
    }
    public void setRecipeName(String nameR)
    {
    	recipeName.set(nameR);
    }
        
    public String getIngredient()
    {
        return ingredient.get();
    }
    public void setIngredient(String ingredientR)
    {
    	ingredient.set(ingredientR);
    }
    
    public String getAmount()
    {
        return amount.get();
    }
    public void setAmount(String amountR)
    {
    	amount.set(amountR);
    }
    public String getDescription()
    {
        return description.get();
    }
    public void setDescription(String descriptionR)
    {
    	description.set(descriptionR);
    } 
}
