package ui.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.SQLConnector;
import data.FoodItem;
import data.Ingredient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.Filter;
import ui.FoodFilterController;
import ui.Paging;

public class UserMenuController {

	public Paging paging = new Paging(); 

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
	
	//Zavretie aktualneho okna a otvorenie prihlasenia
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
				//TODO tu by malo byt odoslanie do databazy
				loadFoodVBox();
			});
			
			//Vytvorenie moznosti pri kliku praveho tlacidla
			MenuItem deleteMenu = new MenuItem("Delete");
			ContextMenu menu = new ContextMenu();
			menu.getItems().addAll(deleteMenu);
			
			//Nastavenie tabulky na spravne hodnoty
			orderTableView.getItems().clear();
			orderTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
			orderTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
			
			orderTableView.setRowFactory(e -> {
			    TableRow<FoodItem> tRow = new TableRow<>();
			    tRow.setOnMouseClicked(event -> {		    	
			    	if(tRow.isEmpty())
			    		return;	    	
			    	
			    	FoodItem food = tRow.getItem();		    	
		            //Vybratie moznosti delete
					deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							System.out.println("Deleting! "+food.getName());
							showConfirmBox(food);
						}
					});
					tRow.setContextMenu(menu);
					
			        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty() ) {
			            System.out.println("ID OF CLICKED chefs "+food.getId());	
			            openDetailMenu(food);
			        }
			    });
			    return tRow ;
			});
			
			//Debug
			orderItems.clear();
			for(int i=1;i<12;i++)
			{
				orderItems.add(new FoodItem(i, "Gulas"+i, i, "Jozko Mrkvicka",null));
			}
			orderTableView.getItems().addAll(orderItems);
			
			CalculateCheckoutPrice();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void CalculateCheckoutPrice()
	{
		//Pocitanie ceny na zaklade produktov
		int sum=0;
		for (FoodItem foodItem : orderItems) {
			sum+=Integer.valueOf(foodItem.getPrice());
		}
		priceLabel.setText(String.valueOf(sum)+ " E");	
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
			
			//TODO prednastavit aktualne hodnoty uzivatela
			
			saveChangesButton.setOnAction(e->{
				System.out.println("Saved changes in profile!");
				//TODO tu by mal byt update usera
				loadFoodVBox();
			});			
		} catch (IOException e) {
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
				if (paging.getPage() < paging.getTotalPages()) {
					paging.incrementPage();
					System.out.println("idem na stranu: " + paging.getPage());
					updateFoodList();
				}
			});
			foodPreviousBtn.setOnAction(e->{
				if (paging.getPage() > 1) {
					paging.decrementPage();
					System.out.println("idem na stranu: " + paging.getPage());
					updateFoodList();
				}
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
			            openDetailMenu(food);
			        }
			    });
			    return tRow ;
			});
			
			updateFoodList();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateFoodList() {
		foodTableView.getItems().clear();
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		if (connector.isConnectedToDB()) {
			ArrayList<FoodItem> foodList = connector.getFoodListFromDB(paging);

			for (FoodItem f : foodList) {
				foodTableView.getItems().add(f);
			}
		}
		connector.closeConnection();
	}
	
	public void changeFoodFilter(Filter f) {
		foodTableView.getItems().clear();
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		if (connector.isConnectedToDB()) {
			ArrayList<FoodItem> foodList = connector.getFoodListFromDB(paging,f);

			for (FoodItem fi : foodList) {
				foodTableView.getItems().add(fi);
			}
		}
		connector.closeConnection();
	}
	
	//Vytvara upozornenie pri mazani
	private void showConfirmBox(FoodItem food)
	{
		Platform.runLater(() -> {
            ButtonType okay = new ButtonType("Delete");
            ButtonType cancel = new ButtonType("Cancel");
            Alert alert = new Alert(Alert.AlertType.WARNING,"",okay,cancel);
            alert.setTitle("Continue?");
            alert.setHeaderText("Do you really want to remove "+food.getName()+" from your order?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if(result.orElse(cancel) == okay)
            {
            	System.out.println("Pressed delete");
            	orderItems.remove(food);
            	orderTableView.getItems().remove(food);
            	CalculateCheckoutPrice();
            }
        }
		);
	}
	
	//Otvori okno s filtrom
	public void openFilterMenu()
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FoodFilter.fxml"));
			VBox root = loader.load();
			Scene scene = new Scene(root);
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Filter");
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			primaryStage.initModality(Modality.APPLICATION_MODAL); 
			primaryStage.show();
			
			//Nastavenie referencie aby sa dal uplatnit filter
			FoodFilterController filterScreenController = (FoodFilterController)loader.getController();
			filterScreenController.setUserFoodFilter(this);		
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Vytvori nove okno s detailom o polozke
	private String DETAIL_TITLE = "Detail o polozke";
	private void openDetailMenu(FoodItem food)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ItemDetail.fxml"));	
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle(DETAIL_TITLE);
			
			Label detailLabel;
			detailLabel = (Label)root.getChildren().get(0);
			
			String textString = "Name of the food: "+food.getName()+"\nPrice: "+String.valueOf(food.getPrice())+
					"\nChef: "+food.getChef()+"\nIngredients:\n";
			
			if(food.getIngredients()!= null)
			{
				for (Ingredient ing: food.getIngredients()) {
					textString+=ing.getName()+"\n";
				}
			}
			
			detailLabel.setText(textString);
						
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 			
			stage.show();			
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
