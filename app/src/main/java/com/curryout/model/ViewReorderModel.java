package com.curryout.model;

public class ViewReorderModel {

    String ordered_product_id,ordered_item_quantity,ordered_item_price,
            product_name,food_type,category,restraunt_name,
            restraunt_image,sub_category;


    public ViewReorderModel() {
    }

    public ViewReorderModel(String ordered_item_quantity, String ordered_item_price, String product_name) {
        this.ordered_item_quantity = ordered_item_quantity;
        this.ordered_item_price = ordered_item_price;
        this.product_name = product_name;
    }
    public ViewReorderModel(String ordered_item_quantity, String ordered_item_price, String product_name,String ordered_product_id) {
        this.ordered_item_quantity = ordered_item_quantity;
        this.ordered_item_price = ordered_item_price;
        this.product_name = product_name;
        this.ordered_product_id = ordered_product_id;
    }

    public ViewReorderModel(String ordered_product_id, String ordered_item_quantity, String ordered_item_price, String product_name, String food_type, String category, String restraunt_name, String restraunt_image, String sub_category) {
        this.ordered_product_id = ordered_product_id;
        this.ordered_item_quantity = ordered_item_quantity;
        this.ordered_item_price = ordered_item_price;
        this.product_name = product_name;
        this.food_type = food_type;
        this.category = category;
        this.restraunt_name = restraunt_name;
        this.restraunt_image = restraunt_image;
        this.sub_category = sub_category;
    }

    public String getOrdered_product_id() {
        return ordered_product_id;
    }

    public void setOrdered_product_id(String ordered_product_id) {
        this.ordered_product_id = ordered_product_id;
    }

    public String getOrdered_item_quantity() {
        return ordered_item_quantity;
    }

    public void setOrdered_item_quantity(String ordered_item_quantity) {
        this.ordered_item_quantity = ordered_item_quantity;
    }

    public String getOrdered_item_price() {
        return ordered_item_price;
    }

    public void setOrdered_item_price(String ordered_item_price) {
        this.ordered_item_price = ordered_item_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRestraunt_name() {
        return restraunt_name;
    }

    public void setRestraunt_name(String restraunt_name) {
        this.restraunt_name = restraunt_name;
    }

    public String getRestraunt_image() {
        return restraunt_image;
    }

    public void setRestraunt_image(String restraunt_image) {
        this.restraunt_image = restraunt_image;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }
}
