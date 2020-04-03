package ui.admin;

import data.Chef;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ChefsVBoxController {

	@FXML Button chefsFilterBtn,chefsNextBtn,chefsPreviousBtn;	
	@FXML TableView<Chef> chefsTableView;
	
	public void initialize()
	{
		System.out.println("initialize() ChefsVBoxController");
		
		chefsFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		chefsNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		chefsPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Set up table
		chefsTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		
		chefsTableView.setRowFactory(e -> {
		    TableRow<Chef> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            Chef chefs = tRow.getItem();
		            System.out.println("ID OF CLICKED chefs "+chefs.getId());
		        }
		    });
		    return tRow ;
		});
		
		//Debug
		for(int i=1;i<1000;i++)
		{
			chefsTableView.getItems().add(new Chef(i,"Janko Mrkvicka "+i));
		}
	}
}
