package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Professional extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);
    }

    public void openMeetings(View view){
        Intent intent = new Intent(this, Meetings.class);
        startActivity(intent);
    }

    public void openBills(View view) {
        Intent intent = new Intent(this, Bills.class);
        startActivity(intent);
    }
    public void openOfficeAccessories(View view) {
        Intent intent = new Intent(this, OfficeAccessories.class);
        startActivity(intent);
    }
}
