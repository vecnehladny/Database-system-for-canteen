package data;

import java.util.ArrayList;
import java.util.List;

public class FoodItem {

	private int id;
	private String name;
	private float price;
	private String chef;
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	
	public FoodItem(int id, String name, float price, String chef, ArrayList<Ingredient> ingredients) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.chef = chef;
		this.ingredients = ingredients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return String.valueOf(price);
	}

	public void setPrice(float price) {
		this.price = price;
	}
	public String getChef() {
		return chef;
	}

	public void setChef(String chef) {
		this.chef = chef;
	}


	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	
}
