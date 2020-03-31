package ui.user;

import java.io.IOException;

import application.Food;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuController {

	@FXML Button profileButton;
	@FXML Button foodButton;
	@FXML Button logOutButton;
	@FXML VBox contentVBox;
	
	//Food VBox UI
	ListView<Food> menuListView; 
	Button filterButton,nextButton,previousButton;	
	//Checkout VBox UI
	ListView<Food> orderListView;
	Label priceLabel;
	Button createOrderButton;	
	//Profile VBox UI
	TextField nameField,addressField,passField1,passField2;
	Button saveChangesButton;
	
	String LOGIN_STRING = "Login screen";
	
	//Zavola sa pri prepnuti sceny
	public void initialize()
	{
		loadFoodVBox();
	}	
	
	//Debug function - prida nahodne veci do listu
	public void addItemsToList(ListView<Food> menuListView)
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
			Stage primaryStage2 = (Stage) ((Node)event.getSource()).getScene().getWindow();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/ui/LoginScreen.fxml"));
			Scene scene = new Scene(root);
			primaryStage2.close();
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle(LOGIN_STRING);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadCheckoutVBox()
	{
		try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/user/CheckoutVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

			//Nastavenie UI referencii-
			for (Node obj : contentVBox.getChildren()) {
				if(obj.getId().equals("orderListView")){
					orderListView = (ListView<Food>) obj;
				}
				if(obj.getId().equals("createOrderButton")){
					createOrderButton = (Button) obj;
				}
				if(obj.getId().equals("priceLabel")){
					priceLabel = (Label) obj;
				}
			}
			
			//Nacitanie listu - DEBUG
			orderListView.setCellFactory(l ->new FoodItemCell());
			addItemsToList(orderListView);
			
			createOrderButton.setOnAction(e->{
				System.out.println("Order created");
				loadFoodVBox();
			});
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadProfileVBox()
	{
		try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/user/ProfileVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());
			
			//Nastavenie UI referencii-
			for (Node obj : contentVBox.getChildren()) {
				if(obj.getId().equals("nameField")){
					nameField = (TextField) obj;
				}
				if(obj.getId().equals("addressField")){
					addressField = (TextField) obj;
				}
				if(obj.getId().equals("passField1")){
					passField1 = (TextField) obj;
				}
				if(obj.getId().equals("passField2")){
					passField2 = (TextField) obj;
				}
				if(obj.getId().equals("saveChangesButton")){
					saveChangesButton = (Button) obj;
				}
			}
			
			saveChangesButton.setOnAction(e->{
				System.out.println("Saved changes in profile!");
				loadFoodVBox();
			});
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadFoodVBox()
	{
		
		try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/user/FoodVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

			//Nastavenie UI referencii-
			for (Node obj : contentVBox.getChildren()) {
				if(obj.getId().equals("menuListView")){
					menuListView = (ListView<Food>) obj;
				}
				if(obj.getId().equals("filterButton")){
					filterButton = (Button) obj;
				}
				if(obj.getId().equals("nextButton")){
					nextButton = (Button) obj;
				}
				if(obj.getId().equals("previousButton")){
					previousButton = (Button) obj;
				}
			}
			
			//Nacitanie listu - DEBUG
			menuListView.setCellFactory(l ->new FoodItemCell());
			addItemsToList(menuListView);
						
			filterButton.setOnAction(e->{
				openFilterMenu();
			});	
			
			//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
			nextButton.setOnAction(e->{
				System.out.println("Next btn pressed");
			});
			previousButton.setOnAction(e->{
				System.out.println("Previous btn pressed");
			});


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openFilterMenu()
	{
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("/ui/user/FilterScreen.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Filter");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
