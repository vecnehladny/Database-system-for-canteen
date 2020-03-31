package ui.user;

import application.Food;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ItemDetailScreenController {

	@FXML Label nameLabel;
	@FXML Label priceLabel;
		
	//Nacita detaily do novo otvoreneho okna
	public void loadDetails(Food food)
	{
		System.out.println("LOADING DETAILS");
		nameLabel.setText(food.getName());
		priceLabel.setText(food.getPrice().toString());
	}
	
}
