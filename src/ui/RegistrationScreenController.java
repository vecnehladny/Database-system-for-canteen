package ui;

import java.util.ArrayList;
import java.util.List;

import application.SQLConnector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
	SQLConnector connector;
	
	public void initialize() {
		new Thread() {
			public void run() {
				connector = new SQLConnector();
			}
		}.start();
	}
	
	public void registerUser()
	{
		StringBuilder errorString = new StringBuilder();
		boolean error = false;
		
		List<TextField> textfields = new ArrayList<TextField>();
		textfields.add(passwordField1);
		textfields.add(passwordField2);
		textfields.add(emailField);
		textfields.add(addressField);
		textfields.add(nameField);
		
		for(TextField field: textfields) {
			field.setStyle(null);
		}
		
		//Kontrola prazdnych poli
		if(passwordField1.getText().isEmpty() || passwordField2.getText().isEmpty() ||
				emailField.getText().isEmpty() || nameField.getText().isEmpty() ||
				addressField.getText().isEmpty())
		{
			for(TextField field: textfields) {
				if(field.getText().isEmpty())
					field.setStyle("-fx-background-color: red;");
			}			
			error = true;
			errorString.append("You need to fill all fields\n");
		}
		
		
		if(nameField.getText().length() < 4 || !nameField.getText().contains(" ") || !isValidName(nameField.getText())) {
			nameField.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Please enter correct full name!\n");
		}
		
		if(!isValidEmailAddress(emailField.getText())) {
			emailField.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Invalid email adress!\n");
		}
		
		if(addressField.getText().length() < 10 || !isValidPostalAdress(addressField.getText())) {
			addressField.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Invalid postal adress!\n");
		}
		

		if(!passwordField1.getText().equals(passwordField2.getText()))
		{
			passwordField1.setStyle("-fx-background-color: red;");
			passwordField2.setStyle("-fx-background-color: red;");
			error = true;
			errorString.append("Passwords do not match\n");
		}	
		else {
			if(passwordField1.getText().length() < 8) {
				passwordField1.setStyle("-fx-background-color: red;");
				passwordField2.setStyle("-fx-background-color: red;");
				error = true;
				errorString.append("Password needs to be at least 8 characters long!\n");
			}
		}
		
		
		if(error) {
			showAlertBox(errorString.toString());
			return;
		}
		
		//Pridanie pouzivatela do DB
		try {
			
			//Ak je uzivatel extremne rychly pri zadavani udajov, vacsinou sa to ale nestane ani raz
			while(connector == null) {
				Thread.sleep(50);
			}
			
			boolean result = connector.addUserToDB(nameField.getText(), addressField.getText(), 
					passwordField1.getText(), emailField.getText());
			
			if(!result)
			{
				showAlertBox("Adding user failed - your username is probably taken");
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
		}catch (Exception e) {
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
	
	//Skopirovane z https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    
    //Skopirovane z https://stackoverflow.com/questions/35392798/regex-to-validate-full-name-having-atleast-four-characters
    public static boolean isValidName(String name) {
        String control = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){1,2}$"; 
        return name.matches(control);        
    }
    
    private boolean isValidPostalAdress(String adress) {
    	String control = "[A-Za-z0-9'.-/, ]+"; 
    	return adress.matches(control);
    }
}
