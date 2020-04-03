package ui;

import java.util.ArrayList;
import java.util.List;

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

public class FilterScreenController {

	@FXML MenuButton containBox;
	@FXML MenuButton notContainBox;
	@FXML Slider priceSlider;
	@FXML Label priceLabel;
	
	
	public void initialize()
	{
		System.out.println("Loaded filter screen!");
		
		//Zistit max cenu - alebo dame fixnu?
		float maxPrice = 20;
		
		priceSlider.setMax(maxPrice);
		priceSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {		
				int temp = (int)(newValue.doubleValue()*100);
				double value = temp/100.0;
				priceLabel.setText(String.valueOf(value)+" E");
			}
			
		});
		
		priceSlider.setValue(maxPrice);
		
		
		containBox.getItems().clear();
		notContainBox.getItems().clear();
		containBox.setText("");
		notContainBox.setText("");
		
		//Dorobit nacitavanie ingrendiencii
		List<String> ingredients = new ArrayList<>();
		ingredients.add("oregano");
		ingredients.add("paprika");
		ingredients.add("rajciny");
		ingredients.add("kecup");
		
		for (String string : ingredients) {
			CheckBox cb0 = new CheckBox(string);  
			CustomMenuItem item0 = new CustomMenuItem(cb0); 
			item0.setHideOnClick(false);  
			containBox.getItems().add(item0);
		}
		
		for (String string : ingredients) {
			CheckBox cb0 = new CheckBox(string);  
			CustomMenuItem item0 = new CustomMenuItem(cb0); 
			item0.setHideOnClick(false);  
			notContainBox.getItems().add(item0);
		}
		
		
	}
	
	public void saveChanges(ActionEvent event)
	{
		System.out.println("Filter used!!");
		closeWindow(event);
	}
	
	private void closeWindow(ActionEvent event)
	{
		Stage pStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		pStage.close();
	}
	
}
