package ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;

public class LoginScreenController {

	@FXML Button logInButton;
	@FXML Button registerButton;
	
	String REGISTRATION_TITLE = "Registration";
	String MAIN_MENU_TITLE = "Main Menu";
	
	public void logInButtonPressed(ActionEvent event)
	{
		System.out.println("Log in button pressed");
		
		//Pridat logiku
		loadMainMenuScene(event);
	}
	
	public void registerButtonPressed(ActionEvent event)
	{
		System.out.println("Register button pressed");
		
		loadRegisterScene(event);
	}
	
	public void loadRegisterScene(ActionEvent event)
	{
		System.out.println("Changing scene to register scene");
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/ui/RegistrationScreen.fxml"));
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
