package ui.admin;

import data.FoodItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FoodVBoxController {

	@FXML Button foodFilterBtn,foodNextBtn,foodPreviousBtn;	
	@FXML TableView<FoodItem> foodTableView;
	
	public void initialize()
	{
		System.out.println("initialize() foodVBoxController");
		
		foodFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
		});	
		
		//Pridat funkcie addFood, next a previous - aby sme nezobrazili 1milion zaznamom naraz
		foodNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		foodPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Set up table
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
		
		//TODO pridat nacitanie jedla z DB
		
		//Debug
		for(int i=1;i<1000;i++)
		{
			foodTableView.getItems().add(new FoodItem(i, "Gulas"+i, 25, "Jozko Mrkvicka",null));
		}

	}
	
}
