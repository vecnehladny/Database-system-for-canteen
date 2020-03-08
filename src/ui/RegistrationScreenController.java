package ui;

import java.awt.Button;

import application.SQLConnector;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationScreenController {

	@FXML PasswordField passwordField1;
	@FXML PasswordField passwordField2;
	@FXML TextField emailField;
	@FXML TextField nameField;
	@FXML TextField addressField;
	@FXML Button registerButton;
	
	public void registerUser()
	{
		//Kontrola prazdnych poli
		if(passwordField1.getText().isEmpty() || passwordField2.getText().isEmpty() ||
				emailField.getText().isEmpty() || nameField.getText().isEmpty() ||
				addressField.getText().isEmpty())
		{
			System.out.println("You need to fill all fields");
		}
		
		//Kontrola zhodovania hesla
		if(!passwordField1.equals(passwordField1))
		{
			System.out.println("Passwords do not match");
			return;
		}
		
		//Pridanie pouzivatela do DB
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		
		if(connector.isConnectedToDB())
		{
			boolean result = connector.addUserToDB(nameField.getText(), addressField.getText(), 
					passwordField1.getText(), emailField.getText());
			
			if(!result)
			{
				System.out.println("Adding user failed");
			}
			else 
			{
				//Zavretie registracneho okna + pridat popup OK?
				Stage stage = (Stage) passwordField1.getScene().getWindow();
				stage.close();
			}
		}
		else 
		{
			System.out.println("Failed to connect to DB");
		}
	}
}
