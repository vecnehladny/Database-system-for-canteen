package ui.admin;

import data.Ingredients;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IngredientsVBoxController {

	@FXML Button ingredientFilterBtn,ingredientNextBtn,ingredientPreviousBtn;	
	@FXML TableView<Ingredients> ingredientTableView;
	
	public void initialize()
	{
		System.out.println("initialize() IngredientsVBoxController");
		
		ingredientFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		ingredientNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		ingredientPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Set up table
		ingredientTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		
		ingredientTableView.setRowFactory(e -> {
		    TableRow<Ingredients> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            Ingredients ingredient = tRow.getItem();
		            System.out.println("ID OF CLICKED ingredient "+ingredient.getId());
		        }
		    });
		    return tRow ;
		});
		
		//TODO pridat nacitanie ingrediencii z DB
		
		//Debug
		for(int i=1;i<1000;i++)
		{
			ingredientTableView.getItems().add(new Ingredients(i,"oregano"));
		}
	}
}
