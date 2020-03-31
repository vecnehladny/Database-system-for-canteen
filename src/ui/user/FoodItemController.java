package ui.user;

import application.Food;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FoodItemController {

	private Food food;
	@FXML Label nameLabel;
	@FXML Label priceLabel;
	
	public void setFood(Food foodInc)
	{
		this.food = foodInc;
		
		if(food != null)
		{
			nameLabel.setText(food.getName());
			priceLabel.setText(food.getPrice().toString());
		}
		else 
		{
			nameLabel.setText(null);
			priceLabel.setText(null);
		}
	}
}
