package ui.user;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import application.MD5Hashing;
import application.SQLConnector;
import data.FoodItem;
import data.Ingredient;
import data.Order;
import data.User;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn.SortType;
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
	User user;
	SQLConnector connector = new SQLConnector();

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
	TextField nameField,addressField;
	PasswordField passField1,passField2;
	Button saveChangesButton;
	
	String LOGIN_STRING = "Login screen";
	
	//Toto bude nas obsah kosiku.
	List<FoodItem> orderItems = new ArrayList<>();
	float price;
	
	//Zavola sa pri prepnuti sceny
	public void initialize()
	{
		//loadFoodVBox();
	}	
	
	public void setUser(User user) {
		this.user = user;
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
				if(orderItems.size() <=0) {	return;}
				Order order = new Order();
				order.setCreatedTime(new Date());
				order.setUser(user);
				connector.addOrderToDB(order,orderItems,price);
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
			
			orderTableView.getItems().addAll(orderItems);
			
			CalculateCheckoutPrice();
			
			//Nastavenie poradia tabulky podla mena zostupne
			orderTableView.getSortOrder().add(orderTableView.getColumns().get(0));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void CalculateCheckoutPrice()
	{
		//Pocitanie ceny na zaklade produktov
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		float sum=0;
		
		for (FoodItem foodItem : orderItems) {
			sum+=Float.valueOf(foodItem.getPrice());
		}
		priceLabel.setText(df.format(sum)+ " E");	
		price=sum;
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
					passField1 = (PasswordField) obj;
				}
				if(obj.getId().equals("passField2")){
					passField2 = (PasswordField) obj;
				}
				if(obj.getId().equals("saveChangesButton")){
					saveChangesButton = (Button) obj;
				}
			}
			
			nameField.setText(user.getName());
			addressField.setText(user.getAddress());
			
			saveChangesButton.setOnAction(e->{
				System.out.println("Saved changes in profile!");	
				
				User updatedUser = new User();
				updatedUser.setId(user.getId());
				updatedUser.setName(nameField.getText());
				updatedUser.setAddress(addressField.getText());
				updatedUser.setEmail(user.getEmail());
				updatedUser.setPriviledged(user.isPriviledged());
				updatedUser.setPassword(user.getPassword());
				
				if(!passField1.getText().isEmpty() && !passField2.getText().isEmpty()) {
					updatedUser.setPassword(MD5Hashing.getSecurePassword(passField1.getText()));
				}
				
				SQLConnector con = new SQLConnector();
				if(!validateInput() && con.updateUserDB(updatedUser,true)) {		
					user = updatedUser;
					loadFoodVBox();
				}
			});			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validateInput() {
		StringBuilder errorString = new StringBuilder();
		boolean error = false;
		
		List<TextField> textfields = new ArrayList<TextField>();
		textfields.add(passField1);
		textfields.add(passField2);
		textfields.add(addressField);
		textfields.add(nameField);
		
		for(TextField field: textfields) {
			field.setStyle(null);
		}
		
				
		if(nameField.getText().isEmpty() || !isValidName(nameField.getText())) {
			nameField.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Please enter correct full name!\n");
		}
		
		
		if(addressField.getText().length() < 10 || !isValidPostalAdress(addressField.getText())) {
			addressField.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Invalid postal adress!\n");
		}
		

		
		if(!passField1.getText().equals(passField2.getText()))
		{
			passField1.setStyle("-fx-background-color: red;");
			passField2.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Passwords do not match\n");
		}	
		else {
			if(!passField1.getText().isEmpty() && passField1.getText().length() < 8) {
				passField1.setStyle("-fx-background-color: red;");
				passField2.setStyle("-fx-background-color: red;");
				error = true;
				errorString.append("Password needs to be at least 8 characters long!\n");
			}
		}
		
		
		if(error) {
			showAlertBox(errorString.toString());
		}		
		
		return error;
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
			
			// Vytvorenie moznosti pri kliku praveho tlacidla
			MenuItem addMenu = new MenuItem("Add to checkout");
			ContextMenu menu = new ContextMenu();
			menu.getItems().addAll(addMenu);
			
			//Nacitanie tabulky
			foodTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
			foodTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
			foodTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("chef"));
			
			foodTableView.setRowFactory(e -> {
			    TableRow<FoodItem> tRow = new TableRow<>();
			    tRow.setOnMouseClicked(event -> {
					if (tRow.isEmpty())
						return;
					FoodItem food = tRow.getItem();
					
					// Vyboratie moznosti add
					addMenu.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							System.out.println("Added to checkout " + food.getName());
							orderItems.add(food);
						}
					});
			    	
					tRow.setContextMenu(menu);

					if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty()) {
						System.out.println("ID OF CLICKED food " + food.getId());
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
		foodNextBtn.setDisable(false);
		foodPreviousBtn.setDisable(false);
		
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
		
		if(foodTableView.getItems().size() < paging.getResultsPerPage()) {
			foodNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			foodPreviousBtn.setDisable(true);
		}
	}

	
	
	public void changeFoodFilter(Filter f) {
		foodNextBtn.setDisable(false);
		foodPreviousBtn.setDisable(false);
		
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
		
		if(foodTableView.getItems().size() < paging.getResultsPerPage()) {
			foodNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			foodPreviousBtn.setDisable(true);
		}
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
	
	//Vytvara chybove hlasky pri prihlasovani
	private void showAlertBox(String problem)
	{
		Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(problem);
            alert.showAndWait();
        }
		);
	}
    
    //Skopirovane z https://stackoverflow.com/questions/35392798/regex-to-validate-full-name-having-atleast-four-characters
    public static boolean isValidName(String name) {
        String control = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){1,2}$"; 
        return name.matches(control);        
    }
    
    private boolean isValidPostalAdress(String adress) {
    	String control = "[A-Za-z0-9'.-/, ]+"; 
    	return adress.matches(control);
    }
	
}
