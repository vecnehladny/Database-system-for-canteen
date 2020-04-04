package ui;

import java.io.IOException;

import application.SQLConnector;
import data.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;

public class LoginScreenController {

	@FXML Button logInButton;
	@FXML Button registerButton;
	@FXML PasswordField passwordField;
	@FXML TextField emailField;
	
	private String REGISTRATION_TITLE = "Registration";
	private String USER_MENU_TITLE = "User Main Menu";
	private String ADMIN_MENU_TITLE = "Admin Main Menu";
	
	public void logInButtonPressed(ActionEvent event)
	{		
		System.out.println("Log in button pressed");
		
		//Kontrola prazdnych poli
		if(passwordField.getText().isEmpty() || emailField.getText().isEmpty())
		{
			showAlertBox("Please enter your email and password");
			return;
		}
		
		//Overenie pouzivatela v DB - vrati objekt User ak najde alebo null ak nenajde
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();	
		if(connector.isConnectedToDB())
		{
			User temp = connector.getUserInDB(emailField.getText(), passwordField.getText());
			if(temp == null)
			{
				showAlertBox("Email or password is wrong!");
			}
			else 
			{
				System.out.println("User ID in users table is "+ temp.getId());
				if(temp.isPriviledged()){
					System.out.println("LOADING ADMIN ACCOUNT");
					connector.closeConnection();
					loadMainMenuScene(event,true);
					return;
				}
				else {
					connector.closeConnection();
					loadMainMenuScene(event,false);
				}
				
			}
		}
		else 
		{
			showAlertBox("Error while connecting to database");
		}
	
	}
	
	public void loadRegisterScene()
	{
	
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/ui/RegistrationScreen.fxml"));
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle(REGISTRATION_TITLE);		
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 
			
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMainMenuScene(ActionEvent event,boolean isPriviledged)
	{		
		try {
			Parent root;
			Stage stage = new Stage();
			
			//Podla toho, ci ma prava otvorime spravne UI
			if(isPriviledged) {
				root = FXMLLoader.load(getClass().getResource("/ui/admin/AdminMenu.fxml"));
				stage.setTitle(ADMIN_MENU_TITLE);
			}
			else {
				root = FXMLLoader.load(getClass().getResource("/ui/user/UserMenu.fxml"));
				stage.setTitle(USER_MENU_TITLE);
			}
			Scene scene = new Scene(root);		
			Stage pStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			pStage.close(); //Zatvorenie stareho okna
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Vytvara chybove hlasky pri prihlasovani
	private void showAlertBox(String problem)
	{
		Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(problem);
            alert.showAndWait();
        }
		);
	}
	
}
