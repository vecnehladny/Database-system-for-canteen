package data;

import java.util.ArrayList;
import java.util.List;

public class FoodItem {

	private int id;
	private String name;
	private int price;
	private String chef;
	private List<Ingredients> ingredients = new ArrayList<>();
	
	public FoodItem(int id, String name, int price, String chef, List<Ingredients> ingredients) {
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

	public void setPrice(int price) {
		this.price = price;
	}
	public String getChef() {
		return chef;
	}

	public void setChef(String chef) {
		this.chef = chef;
	}


	public List<Ingredients> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredients> ingredients) {
		this.ingredients = ingredients;
	}
	
	
}