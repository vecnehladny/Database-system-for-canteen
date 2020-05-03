package data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "food_orders")
public class FoodOrders implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@ManyToOne(targetEntity = FoodItem.class)
	@JoinColumn(name="food_id")
	FoodItem food;
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name="order_id")
	Order order;
	@Column(name ="count",unique = false)
	int count;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public FoodItem getFood() {
		return food;
	}
	public void setFood(FoodItem food) {
		this.food = food;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
