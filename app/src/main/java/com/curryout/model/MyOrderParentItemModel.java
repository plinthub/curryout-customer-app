package com.curryout.model;

public class MyOrderParentItemModel {

    String product_name,ordered_item_quantity;

    public MyOrderParentItemModel(String product_name, String ordered_item_quantity) {
        this.product_name = product_name;
        this.ordered_item_quantity = ordered_item_quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOrdered_item_quantity() {
        return ordered_item_quantity;
    }

    public void setOrdered_item_quantity(String ordered_item_quantity) {
        this.ordered_item_quantity = ordered_item_quantity;
    }
}
