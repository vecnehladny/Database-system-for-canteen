package application;

//Classa, ktora drzi info o jedle. Z nej sa potom nacitava info do tabulky
public class Food {

	private String name;
	private Double price;
	
	public Food(String nameInc,Double priceInc)
	{
		name = nameInc;
		setPrice(priceInc);
	}
	
	public void setPrice(Double priceInc)
	{
		price = priceInc;
	}
	
	public Double getPrice()
	{
		return price;
	}
	
	public String getName()
	{
		return name;
	}
}
