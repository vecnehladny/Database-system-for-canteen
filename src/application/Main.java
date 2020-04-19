package application;
	
import data.DataFaker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
//import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	String LOGIN_STRING = "Login screen";
	
	@Override
	public void start(Stage primaryStage) {
		//Application.setUserAgentStylesheet(getClass().getResource("stylesheet.css").toExternalForm());
			
		
		//DataFaker dataFaker = new DataFaker();
		//dataFaker.initFaker();
		
		try {				
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/ui/LoginScreen.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle(LOGIN_STRING);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
