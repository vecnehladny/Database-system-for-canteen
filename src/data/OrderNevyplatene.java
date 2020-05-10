package data;

public class OrderNevyplatene {

    public int count;
	public double price;
	public String name;
    
    public OrderNevyplatene(String name, double price, int count){
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int getcount(){
        return count;
    }

    public String getname(){
        return name;
    }

    public double getprice(){
        return price;
    }
}