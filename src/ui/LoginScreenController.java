package ui;

import java.io.IOException;

import application.SQLConnector;
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
	
	String REGISTRATION_TITLE = "Registration";
	String MAIN_MENU_TITLE = "Main Menu";
	
	public void logInButtonPressed(ActionEvent event)
	{		
		System.out.println("Log in button pressed");
		
		//Kontrola prazdnych poli
		if(passwordField.getText().isEmpty() || emailField.getText().isEmpty())
		{
			showAlertBox("Please enter your email and password");
			return;
		}
		
		//Overenie pouzivatela v DB - vrati ID ak najde alebo -1 ak nenajde
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();	
		if(connector.isConnectedToDB())
		{
			int id = connector.getUserInDB(emailField.getText(), passwordField.getText());
			if(id<0)
			{
				showAlertBox("Email or password is wrong!");
			}
			else 
			{
				System.out.println("User ID in users table is "+id);
				connector.closeConnection();
				loadMainMenuScene(event,false);
			}
		}
		else 
		{
			showAlertBox("Error while connecting to database");
		}
	
	}
	
	public void registerButtonPressed()
	{
		System.out.println("Register button pressed");
		
		loadRegisterScene();
	}
	
	private void loadRegisterScene()
	{
		System.out.println("Changing scene to register scene");
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadMainMenuScene(ActionEvent event,boolean isPriviledged)
	{
		System.out.println("Changing scene to main menu scene");
		
		try {
			Parent root;
			//if(isPriviledged) {
				//root = FXMLLoader.load(getClass().getResource("/ui/admin/MainMenu.fxml"));
			//}
			//else {
				root = FXMLLoader.load(getClass().getResource("/ui/user/MainMenu.fxml"));
			//}
			Scene scene = new Scene(root);
			
			Stage pStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			Stage stage = new Stage();
			pStage.close();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle(MAIN_MENU_TITLE);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
