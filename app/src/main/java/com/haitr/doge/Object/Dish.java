package com.haitr.doge.Object;

import java.io.Serializable;

/**
 * Created by haitr on 5/27/2017.
 */

public class Dish implements Serializable{
    private int price;
    private int quantity;
    private int dishId;
    private String dishName;
    private String date = "";

    public Dish(int dishId, int quantity, int price, String dishName) {
        this.quantity = quantity;
        this.dishId = dishId;
        this.price = price;
        this.dishName = dishName;
    }

    public Dish(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int  quantity) {
        this.quantity = quantity;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }
}
