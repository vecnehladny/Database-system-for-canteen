package data;


public class User {

	private int id;
	private String name;
	private String address;
	private String email;
	private boolean priviledged;
	
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
	
	
	
}
