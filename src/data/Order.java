package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.mapping.Set;

@Entity
@Table(name="orders")
public class Order implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private int id;
	@Column(name = "time",unique = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	User user;
	
	/*@OneToMany(mappedBy = "order")
	Set foodOrder;
	/*private boolean paid;
	private float price;
	private String customer;
	private List<FoodItem> food = new ArrayList<>();*/
	
	public Order()	{}
	
	/*public Order(int id, String createdTime, boolean paid, float price, String customer, List<FoodItem> food) {
		this.id = id;
		this.createdTime = createdTime;
		this.paid = paid;
		this.price = price;
		this.customer = customer;
		this.food = food;
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	/*public String isPaid() {
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
	}*/
	
	
}
