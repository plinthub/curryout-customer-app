package com.curryout;

import android.net.Uri;

import com.androidquery.AQuery;

public class SearchGridDataModel {

    private String title;
    private String price;
    private int imgTitle;
    private String productID;
    String proQty;
    Uri imgTitl;
    String img;

    public SearchGridDataModel(String title, String price) {
        this.title = title;
        this.price = price;
    }

    public SearchGridDataModel(String title, String productID,String proQty) {
        this.title = title;
        this.productID = productID;
        this.proQty = proQty;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public SearchGridDataModel(String title, String price, int imgTitle) {
        this.title = title;
        this.price = price;
        this.imgTitle = imgTitle;
    }

    public SearchGridDataModel(String productID,String title, String price, String imgTitle) {
        this.productID = productID;
        this.title = title;
        this.price = price;
        this.img = imgTitle;
    }

    public SearchGridDataModel(String productID,String title, String price, Uri imgTitl) {
        this.productID = productID;
        this.title = title;
        this.price = price;
        this.imgTitl = imgTitl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(int imgTitle) {
        this.imgTitle = imgTitle;
    }
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Uri getImgTitl() {
        return imgTitl;
    }

    public void setImgTitl(Uri imgTitl) {
        this.imgTitl = imgTitl;
    }
}
