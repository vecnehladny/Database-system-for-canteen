package ui;

import application.Food;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MainMenuController {

	@FXML Button filterButton;
	@FXML Button testButton;
	@FXML ListView<Food> menuListView; 
	
	//Mal by byt zavolany hned ako sa otvori mainMenu
	public void init()
	{
		//Nacitanie listu
		menuListView.setCellFactory(l ->new FoodItemCell());
		addItemsToList();		
	}
	
	//Debug function - prida nahodne veci do listu
	public void addItemsToList()
	{
		for(int i =0;i<10;i++)
		{
			Food food = new Food("Test"+i, i*1.2);
			menuListView.getItems().add(food);
		}
		Food food = new Food("Test1654094651064", 1.2);
		menuListView.getItems().add(food);
	}
	
}
