package ui.admin;

import data.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserVBoxController {

	@FXML Button userFilterBtn,userNextBtn,userPreviousBtn;	
	@FXML TableView<User> userTableView;
	
	public void initialize()
	{
		System.out.println("initialize() userVBoxController");
		
		userFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		userNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		userPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Set up table
		userTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("email"));
		userTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
		userTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("priviledged"));
		
		userTableView.setRowFactory(e -> {
		    TableRow<User> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            User user = tRow.getItem();
		            System.out.println("ID OF CLICKED user "+user.getId());
		        }
		    });
		    return tRow ;
		});
		
		//TODO pridat nacitanie userov
		
		//Debug
		for(int i=1;i<1000;i++)
		{
			userTableView.getItems().add(new User(i, "Jozko Mrkvicka"+i, "Mlynska dolina "+i, "example123@gmail.com",false));
		}

	}
	
}
