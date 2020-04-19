package ui.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.SQLConnector;
import ui.Paging;

import data.Chef;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ChefsVBoxController extends Paging {

	@FXML Button chefsSearchBtn,chefsNextBtn,chefsPreviousBtn;	
	@FXML TableView<Chef> chefsTableView;
	@FXML TextField chefsSearchField;
	
	public void initialize()
	{
		System.out.println("initialize() ChefsVBoxController");
		
		chefsSearchBtn.setOnAction(e->{
			System.out.println("Searching for "+chefsSearchField.getText());
			//TODO chef vyhladavanie
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		chefsNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		chefsPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Vytvorenie moznosti pri kliku praveho tlacidla
		MenuItem deleteMenu = new MenuItem("Delete");
		MenuItem editMenu = new MenuItem("Edit");
		ContextMenu menu = new ContextMenu();
		menu.getItems().addAll(editMenu,deleteMenu);
		
		//Set up table
		chefsTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		
		chefsTableView.setRowFactory(e -> {
		    TableRow<Chef> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		    	
		    	if(tRow.isEmpty())
		    		return;	    	
	            Chef chef = tRow.getItem();
	            
	            //Vybratie moznosti delete
				deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Deleting! "+chef.getName());
						showConfirmBox(chef);
					}
				});
				//Vyboratie moznosti edit
				editMenu.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						System.out.println("Editing! "+chef.getName());	
						openEditMenu(chef);
					}
				});
		    	
		        tRow.setContextMenu(menu);
		    	
		        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            //Asi netreba zobrazit info, ked jedine, co o nom vieme je meno
		            System.out.println("ID OF CLICKED chefs "+chef.getId());		            
		        }
		    });
		    return tRow ;
		});	
		
		update();
	}

	public void update() {
		chefsTableView.getItems().clear();
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		if (connector.isConnectedToDB()) {
			ArrayList<Chef> chefList = connector.getChefListFromDB(this);

			for (Chef ch : chefList) {
				chefsTableView.getItems().add(ch);
			}
		}
		connector.closeConnection();
	}
	
	private void openEditMenu(Chef chef)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/edit/ChefEditPopup.fxml"));	
			ChefsEditController chefsEditController = new ChefsEditController();
			loader.setController(chefsEditController);
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Editing "+chef.getName());
			
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 			
			stage.show();	
			
			chefsEditController.setChef(chef, stage,chefsTableView);			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Vytvara upozornenie pri mazani
	private void showConfirmBox(Chef chef)
	{
		Platform.runLater(() -> {
            ButtonType okay = new ButtonType("Delete");
            ButtonType cancel = new ButtonType("Cancel");
            Alert alert = new Alert(Alert.AlertType.WARNING,"",okay,cancel);
            alert.setTitle("Continue?");
            alert.setHeaderText("Do you really want to delete that record?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if(result.orElse(cancel) == okay)
            {
            	System.out.println("Pressed delete");
            	chefsTableView.getItems().remove(chef);
            	//TODO vymazat zaznam chef
            }
        }
		);
	}
	
}

//Toto je controller pre edit menu
class ChefsEditController
{
	@FXML TextField nameField;
	@FXML Button saveBtn;
	
	public void setChef(Chef chef, Stage stage, TableView<Chef> table)
	{
		nameField.setText(chef.getName());
		
		saveBtn.setOnAction(e-> {
			System.out.println("Changes saved!");
			chef.setName(nameField.getText());
			table.refresh();
			//TODO ulozit zmeny chef
			stage.close();
		});
	}
}
