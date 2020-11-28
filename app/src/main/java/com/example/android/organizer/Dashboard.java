package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    Button signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        signOut = findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mGoogleSignInClient.signOut();
                Intent intent = new Intent(Dashboard.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

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


    public void openTodo(View view) {
        Intent intent = new Intent(this, Todo.class);
        startActivity(intent);
    }
}