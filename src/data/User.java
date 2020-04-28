package data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", unique = true)
	private int id;
	@Column(name = "name", unique = false)
	private String name;
	@Column(name = "address", unique = false)
	private String address;
	@Column(name = "email", unique = true)
	private String email;
	@Column(name = "priviledged", unique = false)
	private boolean priviledged;
	@Column(name = "password",unique = false)
	private String password;
	
	public User() {}
		
	public User(String name, String address, String email, boolean priviledged, String password) {
		super();
		this.name = name;
		this.address = address;
		this.email = email;
		this.priviledged = priviledged;
		this.password = password;
	}

	public User(int id, String name, String address, String email, boolean priviledged) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.email = email;
		this.priviledged = priviledged;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isPriviledged() {
		return priviledged;
	}
	public void setPriviledged(boolean priviledged) {
		this.priviledged = priviledged;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
