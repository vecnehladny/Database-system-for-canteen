package ui.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import ui.Paging;

import application.SQLConnector;
import data.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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

public class UserVBoxController {

	Paging paging = new Paging();

	@FXML Button userSearchBtn,userNextBtn,userPreviousBtn;	
	@FXML TableView<User> userTableView;
	@FXML TextField userSearchField;
	
	SQLConnector connector = new SQLConnector();
	
	//Premenne pre vyhladavanie
	boolean searching = false;
	String searchName;
	
	public void initialize()
	{
		//paging.setPage(47000);
		System.out.println("initialize() userVBoxController");
		
		userSearchBtn.setOnAction(e->{
			searchName = userSearchField.getText();
			System.out.println("Searching for "+searchName);
			if(!searchName.isEmpty()) {
				paging = new Paging();
				searching = true;
				updateUsersListSearching();
			}
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		userNextBtn.setOnAction(e->{
				paging.incrementPage();
				if(!searching)
					updateUsersList();
				else
					updateUsersListSearching();
		});
		userPreviousBtn.setOnAction(e->{
				paging.decrementPage();
				if(!searching)
					updateUsersList();
				else
					updateUsersListSearching();
		});		
		
		//Vytvorenie moznosti pri kliku praveho tlacidla
		MenuItem deleteMenu = new MenuItem("Delete");
		MenuItem editMenu = new MenuItem("Edit");
		ContextMenu menu = new ContextMenu();
		menu.getItems().addAll(editMenu,deleteMenu);
		
		//Set up table
		userTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("email"));
		userTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
		userTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("priviledged"));
		
		userTableView.setRowFactory(e -> {
			TableRow<User> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		    	
		    	if(tRow.isEmpty())
		    		return;	    	
	            User user = tRow.getItem();
	            
	            //Vybratie moznosti delete
				deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Deleting! "+user.getName());
						showConfirmBox(user);
					}
				});
				//Vyboratie moznosti edit
				editMenu.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						System.out.println("Editing! "+user.getName());	
						openEditMenu(user);
					}
				});
		    	
		        tRow.setContextMenu(menu);
		    	
		        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            //TODO zobrazit info
		            System.out.println("ID OF CLICKED user "+user.getId());		            
		        }
		    });
		    return tRow ;
		});	
		
		updateUsersList();

	}
	
	public void updateUsersList() {
		userNextBtn.setDisable(false);
		userPreviousBtn.setDisable(false);
				
		userTableView.getItems().clear();
		userTableView.getItems().addAll(connector.getUserListFromDB(paging));
		
		if(userTableView.getItems().size() < paging.getResultsPerPage()) {
			userNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			userPreviousBtn.setDisable(true);
		}
	}
	
	public void updateUsersListSearching() {
		if(searchName.isEmpty()) {	return;}
		
		userNextBtn.setDisable(false);
		userPreviousBtn.setDisable(false);
		
		userTableView.getItems().clear();
		userTableView.getItems().addAll(connector.getSearchUsersInDB(searchName , paging));
		
		if(userTableView.getItems().size() < paging.getResultsPerPage()) {
			userNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			userPreviousBtn.setDisable(true);
		}
	}
	
	private void openEditMenu(User user)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/edit/UserEditPopup.fxml"));	
			UsersEditController con = new UsersEditController();
			loader.setController(con);
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Editing "+user.getName());
			
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 			
			stage.show();	
			
			con.setUser(user, stage,userTableView);			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Vytvara upozornenie pri mazani
	private void showConfirmBox(User user)
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
            	if(connector.removeUserFromDB(user)) {
            		//userTableView.getItems().remove(user);
    				if(!searching)
    					updateUsersList();
    				else
    					updateUsersListSearching();
            	}
            }
        }
		);
	}
	
	
}

//Toto je controller pre edit menu
class UsersEditController
{
	@FXML TextField nameField,addressField;
	@FXML CheckBox priviledgedCheckbox;
	@FXML Button saveBtn;
	
	public void setUser(User user, Stage stage,TableView<User> table)
	{
		nameField.setText(user.getName());
		addressField.setText(user.getAddress());
		priviledgedCheckbox.setSelected(user.isPriviledged());
		
		saveBtn.setOnAction(e-> {
			SQLConnector connector = new SQLConnector();
			System.out.println("Changes saved!");
			user.setName(nameField.getText());
			user.setPriviledged(priviledgedCheckbox.isSelected());
			user.setAddress(addressField.getText());
			if(connector.updateUserDB(user,false))
			{	
				table.refresh();
			}
			stage.close();
		});
	}

}
