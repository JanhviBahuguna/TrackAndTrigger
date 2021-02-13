package com.example.android.organizer;

public class Data {

    String item,email_id;
    String quantity;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return email_id;
    }

    public void setId(String id) {
        this.email_id = email_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Data(String item, String quantity, String email_id)
    {
        this.item = item;
        this.quantity = quantity;
        this.email_id =email_id;

    }

    public Data(){

    }


}
