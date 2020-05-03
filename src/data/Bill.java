package data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javafx.scene.control.cell.PropertyValueFactory;

@Entity
@Table(name = "bill")
public class Bill implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", unique = true)
	int id;
	@OneToOne(targetEntity = Order.class)
	@JoinColumn(name = "order_id")
	Order order;
	@Column(name = "paid", unique = true)
	boolean paid;
	@Column(name = "price", unique = true)
	float price;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getCustomer() {
		return order.getUser().getName();
	}
	
	public String getCreatedTime() {
		return order.getCreatedTime().toString();
	}
		
	/*
	 * 	orderTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customer"));
		orderTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
		orderTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("paid"));
		orderTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("createdTime"));
	 * 
	 */
}
