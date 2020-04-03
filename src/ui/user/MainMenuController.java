package ui.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.FoodItem;
import data.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuController {

	@FXML Button profileButton;
	@FXML Button foodButton;
	@FXML Button logOutButton;
	@FXML VBox contentVBox;
	
	//Food VBox UI
	Button foodFilterBtn,foodNextBtn,foodPreviousBtn;	
	TableView<FoodItem> foodTableView;
	//Checkout VBox UI
	TableView<FoodItem> orderTableView;
	Label priceLabel;
	Button createOrderButton;	
	//Profile VBox UI
	TextField nameField,addressField,passField1,passField2;
	Button saveChangesButton;
	
	String LOGIN_STRING = "Login screen";
	//Toto bude nas obsah kosiku.
	List<FoodItem> orderItems = new ArrayList<>();
	
	//Zavola sa pri prepnuti sceny
	public void initialize()
	{
		loadFoodVBox();
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
	
	public void loadCheckoutVBox()
	{
		try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/user/CheckoutVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

			//Nastavenie UI referencii
			for (Node obj : contentVBox.getChildren()) {
				if(obj.getId().equals("orderTableView")){
					orderTableView = (TableView<FoodItem>) obj;
				}
				if(obj.getId().equals("createOrderButton")){
					createOrderButton = (Button) obj;
				}
				if(obj.getId().equals("priceLabel")){
					priceLabel = (Label) obj;
				}
			}
			
			createOrderButton.setOnAction(e->{
				System.out.println("Order created");
				loadFoodVBox();
			});
			
			//Set up table
			orderTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
			orderTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
			
			orderTableView.setRowFactory(e -> {
			    TableRow<FoodItem> tRow = new TableRow<>();
			    tRow.setOnMouseClicked(event -> {
			        if (event.getClickCount() == 2 && !tRow.isEmpty() ) {
			        	FoodItem food = tRow.getItem();
			            System.out.println("ID OF CLICKED food "+food.getId());
			        }
			    });
			    return tRow ;
			});
			
			//Debug
			for(int i=1;i<12;i++)
			{
				orderItems.add(new FoodItem(i, "Gulas"+i, i, "Jozko Mrkvicka",null));
			}
			orderTableView.getItems().addAll(orderItems);
			
			int sum=0;
			for (FoodItem foodItem : orderItems) {
				sum+=Integer.valueOf(foodItem.getPrice());
			}
			priceLabel.setText(String.valueOf(sum)+ " E");
			

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
				if(obj.getId().equals("foodTableView")){
					foodTableView = (TableView<FoodItem>) obj;
				}
				if(obj.getId().equals("foodFilterBtn")){
					foodFilterBtn = (Button) obj;
				}
				if(obj.getId().equals("foodNextBtn")){
					foodNextBtn = (Button) obj;
				}
				if(obj.getId().equals("foodPreviousBtn")){
					foodPreviousBtn = (Button) obj;
				}
			}
								
			foodFilterBtn.setOnAction(e->{
				openFilterMenu();
			});	
			
			//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
			foodNextBtn.setOnAction(e->{
				System.out.println("Next btn pressed");
			});
			foodPreviousBtn.setOnAction(e->{
				System.out.println("Previous btn pressed");
			});
			
			//Nacitanie tabulky
			foodTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
			foodTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
			foodTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("chef"));
			
			foodTableView.setRowFactory(e -> {
			    TableRow<FoodItem> tRow = new TableRow<>();
			    tRow.setOnMouseClicked(event -> {
			        if (event.getClickCount() == 2 && !tRow.isEmpty() ) {
			        	FoodItem food = tRow.getItem();
			            System.out.println("ID OF CLICKED food "+food.getId());
			        }
			    });
			    return tRow ;
			});
			
			//Debug
			for(int i=1;i<1000;i++)
			{
				foodTableView.getItems().add(new FoodItem(i, "Gulas"+i, 25, "Jozko Mrkvicka",null));
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openFilterMenu()
	{
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("/ui/FilterScreen.fxml"));
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
