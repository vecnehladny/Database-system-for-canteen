package ui.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.SQLConnector;
import data.Ingredient;
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
import ui.Paging;

public class IngredientsVBoxController {

	public Paging paging = new Paging();

	@FXML Button ingredientsSearchBtn,ingredientNextBtn,ingredientPreviousBtn;	
	@FXML TableView<Ingredient> ingredientTableView;
	@FXML TextField ingredientsSearchField;
	
	SQLConnector connector = new SQLConnector();
	
	//Premenne pre vyhladavanie
	boolean searching = false;
	String searchName;
	
	public void initialize()
	{
		System.out.println("initialize() IngredientsVBoxController");
		
		ingredientsSearchBtn.setOnAction(e->{
			searchName = ingredientsSearchField.getText();
			System.out.println("Searching for "+searchName );
			if(!searchName .isEmpty()) {
				paging = new Paging();
				searching = true;
				updateIngListSearching();
			}
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		ingredientNextBtn.setOnAction(e->{
				paging.incrementPage();
				if(!searching)
					updateIngredientsList();
				else
					updateIngListSearching();
		});
		ingredientPreviousBtn.setOnAction(e->{
				paging.decrementPage();
				if(!searching)
					updateIngredientsList();
				else
					updateIngListSearching();
		});
		
		//Vytvorenie moznosti pri kliku praveho tlacidla
		MenuItem deleteMenu = new MenuItem("Delete");
		MenuItem editMenu = new MenuItem("Edit");
		ContextMenu menu = new ContextMenu();
		menu.getItems().addAll(editMenu,deleteMenu);
		
		//Set up table
		ingredientTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		
		ingredientTableView.setRowFactory(e -> {
			TableRow<Ingredient> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		    	
		    	if(tRow.isEmpty())
		    		return;	    	
	            Ingredient ing = tRow.getItem();
	            
	            //Vybratie moznosti delete
				deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Deleting! "+ing.getName());
						showConfirmBox(ing);
					}
				});
				//Vyboratie moznosti edit
				editMenu.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						System.out.println("Editing! "+ing.getName());	
						openEditMenu(ing);
					}
				});
		    	
		        tRow.setContextMenu(menu);
		    	
		        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            //Asi netreba zobrazit info, ked jedine, co o nom vieme je meno
		            System.out.println("ID OF CLICKED chefs "+ing.getId());		            
		        }
		    });
		    return tRow ;
		});	
		
		updateIngredientsList();
	}

	public void updateIngredientsList() {
		ingredientNextBtn.setDisable(false);
		ingredientPreviousBtn.setDisable(false);
				
		ingredientTableView.getItems().clear();
		ingredientTableView.getItems().addAll(connector.getIngredientListFromDB(paging));
		
		if(ingredientTableView.getItems().size() < paging.getResultsPerPage()) {
			ingredientNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			ingredientPreviousBtn.setDisable(true);
		}
	}
	
	public void updateIngListSearching() {
		if(searchName.isEmpty()) {	return;}
		ingredientNextBtn.setDisable(false);
		ingredientPreviousBtn.setDisable(false);
		
		ingredientTableView.getItems().clear();
		ingredientTableView.getItems().addAll(connector.getSearchIngsInDB(searchName , paging));
		
		if(ingredientTableView.getItems().size() < paging.getResultsPerPage()) {
			ingredientNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			ingredientPreviousBtn.setDisable(true);
		}
	}
	
	private void openEditMenu(Ingredient ingredient)
	{
		try {
			//Pouzivam to iste XML ako pre chefs pretoze v oboch je len meno
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/edit/ChefEditPopup.fxml"));	
			IngEditController con = new IngEditController(); 
			loader.setController(con);
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Editing "+ingredient.getName());
			
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 			
			stage.show();	
			
			con.setIng(ingredient,stage,ingredientTableView);	
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Vytvara upozornenie pri mazani
	private void showConfirmBox(Ingredient ing)
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
            	if(connector.removeIngFromDB(ing)) {
            		ingredientTableView.getItems().remove(ing);
            	}
            }
        }
		);
	}
	
}

//Toto je controller pre edit menu
class IngEditController
{
	@FXML TextField nameField;
	@FXML Button saveBtn;
	
	public void setIng(Ingredient ing, Stage stage, TableView<Ingredient> table)
	{
		nameField.setText(ing.getName());
		
		saveBtn.setOnAction(e-> {
			System.out.println("Changes saved!");
			ing.setName(nameField.getText());
			SQLConnector connector = new SQLConnector();
			if(connector.updateIngDB(ing)) {
				table.refresh();
			}
			stage.close();
		});
	}
}

