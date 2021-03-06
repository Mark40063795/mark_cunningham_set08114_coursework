package com.mark.foodorderapp.Model;

/**
 * Created by Mark on 08/03/2018.
 */

public class Order {
    private String FoodID;
    private String FoodName;
    private String Quantity;
    private String Price;
    private String Discount;

    public Order() {
    }

    public Order(String foodID, String foodName, String quantity, String price, String discount) {
        FoodID = foodID;
        FoodName = foodName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
