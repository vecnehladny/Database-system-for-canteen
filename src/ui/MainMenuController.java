package ui;

import java.awt.Event;

import application.Food;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainMenuController {

	@FXML Button filterButton;
	@FXML Button testButton;
	@FXML Button logOutButton;
	@FXML ListView<Food> menuListView; 
	
	String LOGIN_STRING = "Login screen";
	
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
	
	public void logOut(ActionEvent event)
	{
		try {
			Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/ui/LoginScreen.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle(LOGIN_STRING);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
