package data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ingredients")
public class Ingredient implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", unique = true)
	private int id;
	@Column(name = "name", unique = true)
	private String name;
	
	public Ingredient() {}
	
	public Ingredient(int id, String name) {
		setId(id);
		setName(name);
	}

	public Ingredient(String name) {
		setName(name);
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
