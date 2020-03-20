package ui;

import java.io.IOException;

import application.Food;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//Tato class nastavuje jednotlive vlastnosti prvkov tabulky v main menu
public class FoodItemCell extends ListCell<Food> {

	private Pane foodView;
	private FoodItemController foodItemController;

	//Konstruktor nacita controller
	public FoodItemCell()
	{
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FoodItem.fxml"));		
		foodView = loader.load();
		foodItemController = loader.getController();
		setGraphic(foodView);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);	
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	//Sposobuje update tabulky so spravnymi hodnotami
	@Override
	protected void updateItem(Food item, boolean empty)
	{
		super.updateItem(item, empty);
		foodItemController.setFood(item);
		
		if(!empty)
		{
			//Detekovanie dvojkliku na prvok tabulky
			setOnMouseClicked(new EventHandler<MouseEvent>() {		
				@Override
				public void handle(MouseEvent click) {
					
					if(click.getClickCount() == 2)
					{
						Food currentItem = item;
						System.out.println("Details: "+currentItem.getName()+" price: "+currentItem.getPrice().toString());
						openDetailMenu(currentItem);
					}
					
				}
			});
		}
	}
	
	//Vytvori nove okno s detailom o polozke
	private String DETAIL_TITLE = "Detail o polozke";
	public void openDetailMenu(Food food)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ItemDetailScreen.fxml"));	
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);		
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle(DETAIL_TITLE);
			
			//Nastavuje prioritu. Neda sa vratit naspat dokial nezavru toto okno
			stage.initModality(Modality.APPLICATION_MODAL); 
			
			stage.show();
			
			
			ItemDetailScreenController itemDetailScreenController = loader.getController();
			itemDetailScreenController.loadDetails(food);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
