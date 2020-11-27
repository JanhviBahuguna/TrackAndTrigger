package com.example.android.organizer;

public class Bills_data {

    String bills,date,id,amount;
    public Bills_data(String bills,String date,String id,String amount)
    {
        this.bills = bills;
        this.date = date;
        this.id =id;
        this.amount = amount;
    }

    public String getBills() {
        return bills;
    }

    public void setBills(String bills) {
        this.bills = bills;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Bills_data() {
    }



}
