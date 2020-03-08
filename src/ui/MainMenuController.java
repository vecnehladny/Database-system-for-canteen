package ui;

import application.Food;
import application.SQLConnector;
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
			
		//Len na testovanie zatial
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		
		if(connector.isConnectedToDB())
		{
			connector.addUserToDB("D.M.", "Bla bla BA", "Ahoj123", "dm@gmail.com");		
			int id = connector.getUserInDB("dm@gmail.com", "Ahoj123");		
			if(id>0)
			{
				System.out.println("User found");
			}
			else 
			{
				System.out.println("User not found");
			}
			connector.closeConnection();
		}
		else 
		{
			System.out.println("Not connected");
		}
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
