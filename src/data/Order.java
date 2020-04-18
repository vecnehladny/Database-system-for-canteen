package data;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private int id;
	private String createdTime;
	private boolean paid;
	private float price;
	private String customer;
	private List<FoodItem> food = new ArrayList<>();
	
	public Order(int id, String createdTime, boolean paid, float price, String customer, List<FoodItem> food) {
		this.id = id;
		this.createdTime = createdTime;
		this.paid = paid;
		this.price = price;
		this.customer = customer;
		this.food = food;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String isPaid() {
		return String.valueOf(paid);
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getPrice() {
		return String.valueOf(price);
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public List<FoodItem> getFood() {
		return food;
	}

	public void setFood(List<FoodItem> food) {
		this.food = food;
	}
	
	
}
