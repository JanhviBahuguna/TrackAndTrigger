package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Student extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
    }

    public void openStationary(View view) {
        Intent intent = new Intent(this, Stationary.class);
        startActivity(intent);
    }

    public void openBooks(View view) {
        Intent intent = new Intent(this, Books.class);
        startActivity(intent);
    }

    public void openSportsAccessories(View view) {
        Intent intent = new Intent(this, SportsAccessories.class);
        startActivity(intent);
    }
}
