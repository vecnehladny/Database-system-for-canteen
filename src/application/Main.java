package application;
	
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
		Application.setUserAgentStylesheet(getClass().getResource("stylesheet.css").toExternalForm());
			
		//Nechceme aby sa DB vytvarala pomocou kodu
		/*showLoadingPage(primaryStage);
		SQLConnector connector = new SQLConnector();
		connector.connectToDB();
		connector.closeConnection();*/
		//primaryStage.close();
		
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
	
	/*
	private void showLoadingPage(Stage primaryStage)
	{
		//Prerobit na fxml
		BorderPane rootAnchorPane = new BorderPane();
		Scene scene = new Scene(rootAnchorPane);
		final Label loadingLabel = new Label("loading");
		rootAnchorPane.setCenter(loadingLabel);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle(LOGIN_STRING);
		primaryStage.show();		
	}
	*/
}
