package ui;

import java.awt.Button;

import application.SQLConnector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

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
			showAlertBox("You need to fill all fields");
			return;
		}
		
		//Kontrola zhodovania hesla
		if(!passwordField1.equals(passwordField1))
		{
			showAlertBox("Passwords do not match");
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
				showAlertBox("Adding user failed");
			}
			else 
			{
				//Zavretie registracneho okna + pridat popup OK?
				Stage stage = (Stage) passwordField1.getScene().getWindow();
				
				Platform.runLater(() -> {
					//Musel som pridat window pretoze pri zavreti Stagu sa Alert zasekol
		            ButtonType closeButton = new ButtonType("Close");
					
		            Alert alert = new Alert(Alert.AlertType.NONE,"",closeButton);
		            alert.setTitle("Success");
		            alert.setHeaderText("User added!");
		            
		            Window window = alert.getDialogPane().getScene().getWindow();
		            window.setOnCloseRequest(e -> alert.hide());
		            		            
		            alert.showAndWait();		           	            
		        }
				);
				
				stage.close();
				
			}
		}
		else 
		{
			showAlertBox("Error while connecting to database");
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
