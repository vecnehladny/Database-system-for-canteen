package ui.admin;

import java.io.IOException;
import java.util.Optional;

import application.SQLConnector;
import data.Bill;
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
import ui.Paging;

public class OrderVBoxController {

	@FXML Button orderFilterBtn,orderNextBtn,orderPreviousBtn;	
	@FXML TableView<Bill> orderTableView;
	
	Paging paging = new Paging();
	SQLConnector connector = new SQLConnector();
	
	public void initialize()
	{
		System.out.println("initialize() OrderVBoxController");
		
		orderFilterBtn.setOnAction(e->{
			System.out.println("Opening filter");
			openFilterScreen();
		});	
		
		//Pridat funckie next a previous - aby sme nezobrazili 1milion zaznamom naraz
		orderNextBtn.setOnAction(e->{
			paging.incrementPage();
			updateBillsList();
		});
		orderPreviousBtn.setOnAction(e->{
			paging.decrementPage();
			updateBillsList();
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
			TableRow<Bill> tRow = new TableRow<>();
		    tRow.setOnMouseClicked(event -> {
		    	
		    	if(tRow.isEmpty())
		    		return;	    	
	            Bill bill = tRow.getItem();
	            
	            if(Boolean.valueOf(bill.isPaid())) {
	            	editMenu.setText("Set as not paid");
	            }
	            else {
	            	editMenu.setText("Set as paid");
				}          
	            
	            //Vybratie moznosti delete
				deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Deleting! "+bill);
						showConfirmBox(bill);
					}
				});
				//Vybratie moznosti edit
				editMenu.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						System.out.println("Editing! "+this);	
						if(connector.updateBillDB(bill)) {
							tRow.getItem().setPaid(!(Boolean.valueOf(bill.isPaid())));
							orderTableView.refresh();
						}
					}
				});
		    	
		        tRow.setContextMenu(menu);
		    	
		        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !tRow.isEmpty() ) {
		            //TODO zobrazit info - treba to?
		            System.out.println("ID OF CLICKED ORDER "+bill.getId());		            
		        }
		    });
		    return tRow ;
		});	

		updateBillsList();

	}
	
	//Vytvara upozornenie pri mazani
	private void showConfirmBox(Bill bill)
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
            	if(connector.removeOrderFromDB(bill.getOrder())) {
            		//orderTableView.getItems().remove(bill);
            		updateBillsList();
            	}
            }
        }
		);
	}
	
	public void updateBillsList() {
		orderNextBtn.setDisable(false);
		orderPreviousBtn.setDisable(false);
				
		orderTableView.getItems().clear();
		orderTableView.getItems().addAll(connector.getBillListFromDB(paging));
		
		if(orderTableView.getItems().size() < paging.getResultsPerPage()) {
			orderNextBtn.setDisable(true);
		}
		if(paging.getPage() <=1) {
			orderPreviousBtn.setDisable(true);
		}
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
