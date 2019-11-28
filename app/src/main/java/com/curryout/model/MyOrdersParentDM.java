package com.curryout.model;

import java.util.ArrayList;

public class MyOrdersParentDM {

    private String restName;
    private String foodName;
    private String restImg;
    String OrderId;
    ArrayList<MyOrderParentItemModel> alist;


    public MyOrdersParentDM(String restName, String foodName, String restImg) {
        this.restName = restName;
        this.foodName = foodName;
        this.restImg = restImg;
    }

    public MyOrdersParentDM(String OrderId,String restName, String foodName, String restImg) {
        this.OrderId = OrderId;
        this.restName = restName;
        this.foodName = foodName;
        this.restImg = restImg;
    }

    public MyOrdersParentDM(String OrderId,String restName, String foodName, String restImg,ArrayList<MyOrderParentItemModel> alist) {
        this.OrderId = OrderId;
        this.restName = restName;
        this.foodName = foodName;
        this.restImg = restImg;
        this.alist = alist;
    }


    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestImg() {
        return restImg;
    }

    public void setRestImg(String restImg) {
        this.restImg = restImg;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public ArrayList<MyOrderParentItemModel> getAlist() {
        return alist;
    }

    public void setAlist(ArrayList<MyOrderParentItemModel> alist) {
        this.alist = alist;
    }

}
