package ui.admin;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuController {

	@FXML Button profileButton;
	@FXML Button ingredientButton;
	@FXML Button orderButton;
	@FXML Button usersButton;
	@FXML Button chefsButton;
	@FXML Button logOutButton;
	@FXML VBox contentVBox;
	
	String LOGIN_STRING = "Login screen";
	
	//Zavola sa pri prepnuti sceny
	public void initialize()
	{
		//loadFoodVBox();
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
		
	public void loadOrdersVBox(){
		loadVBox("/ui/admin/OrdersVBox.fxml");
	}
	
	public void loadIngredientsVBox(){
		loadVBox("/ui/admin/IngredientsVBox.fxml");
	}
	
	public void loadChefsVBox(){
		loadVBox("/ui/admin/ChefsVBox.fxml");
	}
	
	public void loadFoodVBox(){
		loadVBox("/ui/admin/FoodVBox.fxml");
	}
	
	public void loadUserVBox(){
		loadVBox("/ui/admin/UserVBox.fxml");
	}
	
	public void loadVBox(String path){
		try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource(path));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
