package com.example.android.organizer;

public class Todo_data {

    String task,time,id,date;
    public Todo_data(String task,String time,String id,String date)
    {
        this.task = task;
        this.time = time;
        this.id =id;
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Todo_data() {
    }



}
