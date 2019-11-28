package com.curryout.model;

public class PaymentItemModel {

    String pay_id;
    private String items;
    private boolean selected;

    public PaymentItemModel(String pay_id, String items) {
        this.pay_id = pay_id;
        this.items = items;
    }

    public PaymentItemModel(String items) {

        this.items = items;

    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getItems() {

        return items;
    }

    public void setItemName(String name) {

        this.items = name;
    }
    public boolean getSelected() {
        return selected;
    }

    public boolean setSelected(Boolean selected) {
        return this.selected = selected;
    }
}

