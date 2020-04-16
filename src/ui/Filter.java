package ui;

import java.util.ArrayList;

public class Filter {
    private ArrayList<String> shouldContain = new ArrayList<String>();
    private ArrayList<String> shouldNotContain = new ArrayList<String>();
    private float maxPrice;
    private String chefName = "";
    private String foodName = "";

    public Filter() {
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = (float) maxPrice;
    }

    public ArrayList<String> getShouldNotContain() {
        return shouldNotContain;
    }

    public ArrayList<String> getShouldContain() {
        return shouldContain;
    }

    public void insertShouldContain(String ingredient) {
        shouldContain.add(ingredient);
    }

    public void insertShouldNotContain(String ingredient) {
        shouldNotContain.add(ingredient);
    }
    
}