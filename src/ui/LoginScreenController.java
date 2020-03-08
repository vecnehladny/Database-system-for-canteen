package ui;

import java.io.IOException;

import application.SQLConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
			System.out.println("Please enter your email and password");
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
				System.out.println("User not found in DB");
			}
			else 
			{
				connector.closeConnection();
				loadMainMenuScene(event);
			}
		}
		else 
		{
			System.out.println("Error while connecting to DB");
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
	
	public void loadMainMenuScene(ActionEvent event)
	{
		System.out.println("Changing scene to main menu scene");
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/ui/MainMenu.fxml"));
			Scene scene = new Scene(root);
			
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle(MAIN_MENU_TITLE);
			
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
