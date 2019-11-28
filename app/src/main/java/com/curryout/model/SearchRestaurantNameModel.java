package com.curryout.model;

public class SearchRestaurantNameModel {

    String rid,imgRes,restName,resfoodType,foodTime,foodAddress,foodOffers;

    public SearchRestaurantNameModel(String rid,String imgRes, String restName, String resfoodType, String foodTime, String foodAddress, String foodOffers) {
        this.rid = rid;
        this.imgRes = imgRes;
        this.restName = restName;
        this.resfoodType = resfoodType;
        this.foodTime = foodTime;
        this.foodAddress = foodAddress;
        this.foodOffers = foodOffers;
    }

    public String getImgRes() {
        return imgRes;
    }

    public void setImgRes(String imgRes) {
        this.imgRes = imgRes;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getResfoodType() {
        return resfoodType;
    }

    public void setResfoodType(String resfoodType) {
        this.resfoodType = resfoodType;
    }

    public String getFoodTime() {
        return foodTime;
    }

    public void setFoodTime(String foodTime) {
        this.foodTime = foodTime;
    }

    public String getFoodAddress() {
        return foodAddress;
    }

    public void setFoodAddress(String foodAddress) {
        this.foodAddress = foodAddress;
    }

    public String getFoodOffers() {
        return foodOffers;
    }

    public void setFoodOffers(String foodOffers) {
        this.foodOffers = foodOffers;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
