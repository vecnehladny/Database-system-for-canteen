package data;

public class Ingredients {

	private int id;
	private String name;
	
	public Ingredients(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Ingredients(String name) {
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
