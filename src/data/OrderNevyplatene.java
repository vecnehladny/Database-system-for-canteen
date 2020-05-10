package data;

public class OrderNevyplatene {

    public int Count;
	public double Price;
	public String Name;
    
    public OrderNevyplatene(String name, double price, int count){
        this.Name = name;
        this.Price = price;
        this.Count = count;
    }

    public int getCount(){
        return Count;
    }

    public String getName(){
        return Name;
    }

    public double getPrice(){
        return Price;
    }
}