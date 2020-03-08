package ui;

import application.Food;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

//Vdaka tejto classe vieme zmennit jednotlive prvky tabulky a nahadzovat tam udaje o jedle
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
	}
	
}
