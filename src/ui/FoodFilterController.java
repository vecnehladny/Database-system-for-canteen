package ui;

import java.util.ArrayList;
import java.util.List;

import application.SQLConnector;
import data.Ingredient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import ui.admin.FoodVBoxController;
import ui.user.UserMenuController;

//Zatial sa pouziva iba na filtrovanie jedla
public class FoodFilterController {

	@FXML MenuButton containBox;
	@FXML MenuButton notContainBox;
	@FXML Slider priceSlider;
	@FXML Label priceLabel;
	
	//Max cena na slider - Zistit max cenu - alebo dame fixnu?
	float maxPrice = 20;	
	UserMenuController userMenuController = null;
	FoodVBoxController foodVBoxController = null;
	
	public void initialize()
	{
		System.out.println("Loaded filter screen!");
		
		priceSlider.setMax(maxPrice);
		
		//Automaticka zmena textu pod sliderom
		priceSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
				int temp = (int)(newValue.doubleValue()*100);
				double value = temp/100.0;
				priceLabel.setText(String.valueOf(value)+" E");
			}
			
		});
		
		priceSlider.setValue(maxPrice);
		
		//TODO Dorobit nacitavanie ingrendiencii z DB
		updateIngredientsList();

	}

	public void updateIngredientsList() {
		containBox.getItems().clear();
		notContainBox.getItems().clear();
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		if (connector.isConnectedToDB()) {
			ArrayList<Ingredient> ingredientsList = connector.getIngredientsListFromDB();

			for (Ingredient ing : ingredientsList) {
				CheckBox cb0 = new CheckBox(ing.getName());  
				cb0.setUserData(ing);
				CustomMenuItem item0 = new CustomMenuItem(cb0); 
				item0.setHideOnClick(false);  
				containBox.getItems().add(item0);
			}

			for (Ingredient ing : ingredientsList) {
				CheckBox cb0 = new CheckBox(ing.getName());  
				cb0.setUserData(ing);
				CustomMenuItem item0 = new CustomMenuItem(cb0); 
				item0.setHideOnClick(false);
				notContainBox.getItems().add(item0);
			}

			containBox.setText(((CheckBox)((CustomMenuItem) containBox.getItems().get(0)).getContent()).getText());
			notContainBox.setText(((CheckBox)((CustomMenuItem) containBox.getItems().get(0)).getContent()).getText());
		}
		connector.closeConnection();
	}
	
	public void setUserFoodFilter(UserMenuController userMenuController) {
		foodVBoxController = null;
		this.userMenuController = userMenuController;
	}
	
	public void setAdminFoodFilter(FoodVBoxController foodVBoxController) {
		userMenuController=null;
		this.foodVBoxController = foodVBoxController;
	}
	
	//Tato metoda zavola metodu v food menu a tam sa nastavi filtrovanie food tabulky.
	public void saveChanges(ActionEvent event)
	{
		System.out.println("Filter used!!");
		if(userMenuController!=null) {
			userMenuController.changeFoodFilter();
		}
		else {
			foodVBoxController.changeFoodFilter();
		}
		Stage pStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		pStage.close();
	}
	
}
