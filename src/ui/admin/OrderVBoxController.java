package ui.admin;

import java.io.IOException;
import java.util.Optional;

import data.Order;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrderVBoxController {

	@FXML Button orderFilterBtn,orderNextBtn,orderPreviousBtn;	
	@FXML TableView<Order> orderTableView;
	
	public void initialize()
	{
		System.out.println("initialize() OrderVBoxController");
		
		orderFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
			openFilterScreen();
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		orderNextBtn.setOnAction(e->{
			System.out.println("Next btn pressed");
		});
		orderPreviousBtn.setOnAction(e->{
			System.out.println("Previous btn pressed");
		});
		
		//Vytvorenie moznosti pri kliku praveho tlacidla
		MenuItem deleteMenu = new MenuItem("Delete");
		MenuItem editMenu = new MenuItem("Placeholder");
		ContextMenu menu = new ContextMenu();
		menu.getItems().addAll(editMenu,deleteMenu);
		
		//Set up table
		orderTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customer"));
		orderTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
		orderTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("paid"));
		orderTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("createdTime"));
		
		orderTableView.setRowFactory(e -> {
			TableRow<Order> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		    	
		    	if(tRow.isEmpty())
		    		return;	    	
	            Order order = tRow.getItem();
	            
	            if(Boolean.valueOf(order.isPaid())) {
	            	editMenu.setText("Set as not paid");
	            }
	            else {
	            	editMenu.setText("Set as paid");
				}          	
	            
	            //Vybratie moznosti delete
				deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Deleting! "+order.getCustomer());
						showConfirmBox(order );
						//TODO zmazat objednavku
					}
				});
				//Vybratie moznosti edit
				editMenu.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						System.out.println("Editing! "+order.getCustomer());	
						tRow.getItem().setPaid(!(Boolean.valueOf(order.isPaid())));
						orderTableView.refresh();
						//TODO: nastavit status zaplatenia na opacny
					}
				});
		    	
		        tRow.setContextMenu(menu);
		    	
		        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            //TODO zobrazit info - treba to? Ak hej treba dat vediet.
		            System.out.println("ID OF CLICKED ORDER "+order.getId());		            
		        }
		    });
		    return tRow ;
		});	
		//TODO pridat nacitanie objednavok
		
		//Debug
		orderTableView.getItems().add(new Order(18, "11.2.2020", true, 101, "Jozko Mrkvicka",null));
		for(int i=1;i<1000;i++)
		{
			orderTableView.getItems().add(new Order(i, "11.2.2020", false, 101, "Jozko Mrkvicka",null));
		}

	}
	
	//Vytvara upozornenie pri mazani
	private void showConfirmBox(Order order)
	{
		Platform.runLater(() -> {
            ButtonType okay = new ButtonType("Delete");
            ButtonType cancel = new ButtonType("Cancel");
            Alert alert = new Alert(Alert.AlertType.WARNING,"",okay,cancel);
            alert.setTitle("Continue?");
            alert.setHeaderText("Do you really want to delete that record?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if(result.orElse(cancel) == okay)
            {
            	System.out.println("Pressed delete");
            	orderTableView.getItems().remove(order);
            	//TODO vymazat zaznam order
            }
        }
		);
	}
	
	public void setFilter()
	{
		//TODO Aplikovanie filtra na order
	}
	
	public void openFilterScreen()
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/OrderFilter.fxml"));	
			OrderFilterController orderFilterController = new OrderFilterController();
			loader.setController(orderFilterController);
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Order filter");
			
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 			
			stage.show();	
			
			orderFilterController.init(this,stage);		
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

class OrderFilterController 
{
	OrderVBoxController orderVBoxController;
	Stage stage;
	@FXML TextField orderNumberField, customerField;
	@FXML CheckBox paidCheckbox;
	@FXML Button filterButton;
	
	public void init(OrderVBoxController orderVBoxController, Stage stage) {
		filterButton.setOnAction(e-> {
			callFilter();
		});
		this.stage = stage;
		this.orderVBoxController = orderVBoxController;
	}
	
	public void callFilter()
	{
		orderVBoxController.setFilter();
		stage.close();
	}
}
