package data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chefs")
public class Chef implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", unique = true)
	private int id;
	@Column(name = "name", unique = false)
	private String name;
	
	public Chef() {}
	
	public Chef(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
}
