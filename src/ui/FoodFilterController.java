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
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.admin.FoodVBoxController;
import ui.user.UserMenuController;

//Zatial sa pouziva iba na filtrovanie jedla
public class FoodFilterController {

	@FXML MenuButton containBox;
	@FXML MenuButton notContainBox;
	@FXML Slider priceSlider;
	@FXML Label priceLabel;
	@FXML TextField chefName;
	@FXML TextField foodName;
	
	float maxPrice;	
	float minPrice;	
	UserMenuController userMenuController = null;
	FoodVBoxController foodVBoxController = null;
	
	public void initialize()
	{
		System.out.println("Loaded filter screen!");
		
		updateMaxPrice();
		updateMinPrice();
		priceSlider.setMax(maxPrice);
		priceSlider.setMin(minPrice);
		
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
		containBox.setText("");
		notContainBox.setText("");
		
		updateIngredientsList();

	}

	public void updateMaxPrice(){
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		if (connector.isConnectedToDB()) {
			maxPrice = (float)connector.getMaxPriceFromDB();
		}
		connector.closeConnection();
	}

	public void updateMinPrice(){
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		if (connector.isConnectedToDB()) {
			minPrice = (float)connector.getMinPriceFromDB();
		}
		connector.closeConnection();
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

	public Filter getFilter(){
		Filter f = new Filter();

		for(MenuItem m : containBox.getItems()){
			CheckBox ch = ((CheckBox)((CustomMenuItem) m).getContent());
			if(ch.isSelected()){
				f.insertShouldContain(ch.getText());
			}
		}

		for(MenuItem m : notContainBox.getItems()){
			CheckBox ch = ((CheckBox)((CustomMenuItem) m).getContent());
			if(ch.isSelected()){
				f.insertShouldNotContain(ch.getText());
			}
		}

		f.setChefName(chefName.getText());
		f.setFoodName(foodName.getText());
		f.setMaxPrice(priceSlider.getValue());

		return f;
	}
	
	//Tato metoda zavola metodu v food menu a tam sa nastavi filtrovanie food tabulky.
	//TODO vyresetovat paging pri aplikovani filtra
	public void saveChanges(ActionEvent event)
	{
		System.out.println("Filter used!!");
		if(userMenuController!=null) {
			userMenuController.changeFoodFilter(getFilter());
		}
		else {
			foodVBoxController.changeFoodFilter(getFilter());
		}
		Stage pStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		pStage.close();
	}
	
}
