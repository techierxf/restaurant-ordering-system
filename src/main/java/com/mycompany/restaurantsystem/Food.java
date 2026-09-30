package com.mycompany.restaurantsystem;

public class Food {

    private int foodID;
    private String foodName;
    private double price;
    private int quantity;
    private String category;

    public Food(int foodID, String foodName, double price, int quantity, String category) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public int getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}