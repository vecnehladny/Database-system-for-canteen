package ui.admin;

import java.io.IOException;

import data.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

	@FXML Button userSearchBtn,userNextBtn,userPreviousBtn;	
	@FXML TableView<User> userTableView;
	@FXML TextField userSearchField;
	
	public void initialize()
	{
		System.out.println("initialize() userVBoxController");
		
		userSearchBtn.setOnAction(e->{
			System.out.println("Searching for "+userSearchField.getText());
			//TODO user vyhladavanie
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		userNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		userPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
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
						//Pridame aj overenie typu "Ste si isty?"  ?
						//TODO vymazat zaznam usera
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
		            System.out.println("ID OF CLICKED chefs "+user.getId());		            
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
			
			con.setUser(user, stage);			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

//Toto je controller pre edit menu
class UsersEditController
{
	@FXML TextField nameField,addressField;
	@FXML CheckBox priviledgedCheckbox;
	@FXML Button saveBtn;
	
	public void setUser(User user, Stage stage)
	{
		nameField.setText(user.getName());
		addressField.setText(user.getAddress());
		priviledgedCheckbox.setSelected(user.isPriviledged());
		
		saveBtn.setOnAction(e-> {
			System.out.println("Changes saved!");
			//TODO ulozit zmeny usera
			stage.close();
		});
	}
}
