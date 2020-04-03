package ui.admin;

import data.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrderVBoxController {

	@FXML Button orderFilterBtn,orderNextBtn,orderPreviousBtn;	
	@FXML TableView<Order> orderTableView;
	
	public void initialize()
	{
		System.out.println("initialize() OrderVBoxController");
		
		orderFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		orderNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		orderPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Set up table
		orderTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customer"));
		orderTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
		orderTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("paid"));
		orderTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("createdTime"));
		
		orderTableView.setRowFactory(e -> {
		    TableRow<Order> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            Order order = tRow.getItem();
		            System.out.println("ID OF CLICKED ORDER "+order.getId());
		        }
		    });
		    return tRow ;
		});
		
		//TODO pridat nacitanie objednavok
		
		//Debug
		for(int i=1;i<1000;i++)
		{
			orderTableView.getItems().add(new Order(i, "11.2.2020", false, 101, "Jozko Mrkvicka",null));
		}

	}
	
}
