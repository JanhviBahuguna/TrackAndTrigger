package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }
    public void openGrocery(View view) {
        Intent intent = new Intent(this, Grocery.class);
        startActivity(intent);
    }

    public void openMedicines(View view) {
        Intent intent = new Intent(this, Medicines.class);
        startActivity(intent);
    }

    public void openHomeAppliances(View view) {
        Intent intent = new Intent(this, HomeAppliances.class);
        startActivity(intent);
    }

    public void openBills(View view) {
        Intent intent = new Intent(this, Bills.class);
        startActivity(intent);
    }
}
