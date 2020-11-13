package com.example.android.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView professional,home,student;


    }
    public void openProfessional(View view){
        Intent intent = new Intent(this, Professional.class);
        startActivity(intent);
    }
    public void openHome(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    public void openStudent(View view) {
        Intent intent = new Intent(this, Student.class);
        startActivity(intent);
    }
}