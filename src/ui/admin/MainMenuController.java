package ui.admin;

import java.io.IOException;

import data.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuController {

	@FXML Button profileButton;
	@FXML Button ingredientButton;
	@FXML Button orderButton;
	@FXML Button usersButton;
	@FXML Button chefsButton;
	@FXML Button logOutButton;
	@FXML VBox contentVBox;
	
	//Food VBox UI
	//ListView<Food> menuListView; 
	Button filterButton,nextButton,previousButton,addFoodButton;	
	
	//Show order VBox UI
	Button orderFilterBtn,orderNextBtn,orderPreviousBtn;	
	TableView<Order> orderTableView;

	
	String LOGIN_STRING = "Login screen";
	
	//Zavola sa pri prepnuti sceny
	public void initialize()
	{
		loadFoodVBox();
		//testTableInit();
		
		//testTable.getItems().add(new Tester("name ex",14,"my secret"));
	}	
	
	/*public void testTableInit()
	{
		testTable.getColumns().clear();
		testTable.setEditable(false);
		testTable.getColumns().add(new TableColumn<Tester, String>());
		testTable.getColumns().get(0).setText("User Name");
		testTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
		testTable.getColumns().add(new TableColumn<Tester,String>());
		testTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
		testTable.getColumns().get(1).setText("Price value");
		
		testTable.setRowFactory(e -> {
		    TableRow<Tester> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		            Tester tester = row.getItem();
		            System.out.println(tester.getSecret());
		        }
		    });
		    return row ;
		});
		
	}*/
	
	
	public void logOut(ActionEvent event)
	{
		try {
			Stage primaryStage2 = (Stage) ((Node)event.getSource()).getScene().getWindow();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/ui/LoginScreen.fxml"));
			Scene scene = new Scene(root);
			primaryStage2.close();
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle(LOGIN_STRING);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public void loadOrdersVBox()
	{
		/*try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/admin/OrdersVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		loadVBox("/ui/admin/OrdersVBox.fxml");
	}
	
	public void loadIngredientsVBox()
	{
		/*try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/admin/IngredientsVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		loadVBox("/ui/admin/IngredientsVBox.fxml");
	}
	
	public void loadChefsVBox()
	{
		/*try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/admin/ChefsVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		loadVBox("/ui/admin/ChefsVBox.fxml");
	}
	
	public void loadFoodVBox()
	{
		/*try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource("/ui/admin/FoodVBox.fxml"));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		loadVBox("/ui/admin/FoodVBox.fxml");
	}
	
	public void loadUserVBox()
	{
		loadVBox("/ui/admin/UserVBox.fxml");
	}
	
	public void loadVBox(String path)
	{
		try {
			VBox newVBox= (VBox)FXMLLoader.load(getClass().getResource(path));
			contentVBox.getChildren().clear();
			contentVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openFilterMenu()
	{
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("/ui/FilterScreen.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Filter");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
