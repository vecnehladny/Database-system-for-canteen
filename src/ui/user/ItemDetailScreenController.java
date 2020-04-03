package ui.user;

import data.FoodItem;
import data.Ingredients;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ItemDetailScreenController {

	@FXML Label nameLabel;
	@FXML Label priceLabel;
	@FXML Label ingredientsLabel;
	@FXML Label chefLabel;
		
	//Nacita detaily do novo otvoreneho okna
	public void loadDetails(FoodItem food)
	{
		System.out.println("LOADING FOOD DETAILS");
		
		nameLabel.setText(food.getName());
		priceLabel.setText(food.getPrice()+" E");
		chefLabel.setText(food.getChef());
		String ingredientsString = "Ingredients:\n";
		
		if(food.getIngredients() != null)
		{
			for (Ingredients ing: food.getIngredients()) {
				ingredientsString += ing.getName()+"\n";
			}
		}
		ingredientsLabel.setText(ingredientsString);
		
		
	}
	
}
