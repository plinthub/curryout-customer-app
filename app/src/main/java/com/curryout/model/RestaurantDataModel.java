package com.curryout.model;

import com.androidquery.AQuery;

import java.io.Serializable;

public class RestaurantDataModel implements Serializable {

    private String restrauntID;
    private String RestauName;
    private String foodName;
    private String address;
    private String cuisine_name;
    private String foodType;

    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    private int imgRestaurant;

    public String getImgRestauran() {
        return imgRestauran;
    }

    public void setImgRestauran(String imgRestauran) {
        this.imgRestauran = imgRestauran;
    }

    String imgRestauran;

    public String getRestrauntID() {
        return restrauntID;
    }

    public void setRestrauntID(String restrauntID) {
        this.restrauntID = restrauntID;
    }

    public RestaurantDataModel( String restauName, String foodName, String address,int imgRestaurant) {

        this.RestauName = restauName;
        this.foodName = foodName;
        this.address = address;
        this.imgRestaurant = imgRestaurant;

    }

    public RestaurantDataModel(String restrauntID, String restauName, String foodName, String address, String imgRestaurant) {
        this.restrauntID = restrauntID;
        this.RestauName = restauName;
        this.foodName = foodName;
        this.address = address;
        this.imgRestauran = imgRestaurant;
    }

    public RestaurantDataModel(String restauName, String foodName, String address,String cuisine_name) {
        this.RestauName = restauName;
        this.foodName = foodName;
        this.address = address;
        this.cuisine_name = cuisine_name;
    }
    public String getRestauName() {
        return RestauName;
    }

    public void setRestauName(String restauName) {
        RestauName = restauName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImgRestaurant() {
        return imgRestaurant;
    }

    public void setImgRestaurant(int imgRestaurant) {
        this.imgRestaurant = imgRestaurant;
    }


    @Override
    public String toString() {
        return RestauName;
    }
}
