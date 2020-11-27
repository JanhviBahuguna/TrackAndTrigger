package com.example.android.organizer;

public class Data {

    String item,id;
    String quantity;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Data(String item, String quantity, String id)
    {
        this.item = item;
        this.quantity = quantity;
        this.id =id;

    }

    public Data(){

    }


}
