package com.curryout.model;

public class SearchFoodItemModel {

    int image;
    String pid,foodname,foodtime,foodprice,foodrestName,stringImg;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStringImg() {
        return stringImg;
    }

    public void setStringImg(String stringImg) {
        this.stringImg = stringImg;
    }

    public SearchFoodItemModel(int image, String foodname, String foodtime, String foodprice, String foodrestName) {

        this.image = image;
        this.foodname = foodname;
        this.foodtime = foodtime;
        this.foodprice = foodprice;
        this.foodrestName = foodrestName;
    }

    public SearchFoodItemModel(String pid,String foodname, String foodtime, String foodprice, String foodrestName,String stringImg) {
        this.pid = pid;
        this.foodname = foodname;
        this.foodtime = foodtime;
        this.foodprice = foodprice;
        this.foodrestName = foodrestName;
        this.stringImg = stringImg;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodtime() {
        return foodtime;
    }

    public void setFoodtime(String foodtime) {
        this.foodtime = foodtime;
    }

    public String getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(String foodprice) {
        this.foodprice = foodprice;
    }

    public String getFoodrestName() {
        return foodrestName;
    }

    public void setFoodrestName(String foodrestName) {
        this.foodrestName = foodrestName;
    }
}
