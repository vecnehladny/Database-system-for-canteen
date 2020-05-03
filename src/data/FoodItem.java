package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "food")
public class FoodItem implements Serializable, Comparable<FoodItem>{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id",unique = true)
	private int id;
	@Column(name = "name",unique = false)	
	private String name;
	@Column(name = "price",unique = false)
	private float price;
	@Transient
	private String chef;
	@Transient
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	
	public FoodItem()	{}
	
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

	@Override
	public int compareTo(FoodItem fi) {
		return getId() - fi.getId();
	}
	
	
}
