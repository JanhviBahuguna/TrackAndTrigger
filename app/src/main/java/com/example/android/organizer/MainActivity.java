package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    private Button login, register;
    DatabaseReference reff;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();
        username =  findViewById(R.id.login_username_text);
        password = findViewById(R.id.login_password_text);
        login = findViewById(R.id.login_button);
        register =  findViewById(R.id.login_register_button);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username_ = username.getText().toString();
                String password_ = password.getText().toString();


                if(TextUtils.isEmpty(username_))
                {
                    username.setError("username is required");
                }

                if(TextUtils.isEmpty(password_))
                {
                    password.setError("password is required");
                }
                else{
                    mAuth.signInWithEmailAndPassword(username_,password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             Intent intent = new Intent(MainActivity.this,Dashboard.class);
                             startActivity(intent);
                             finish();
                         }
                         else{
                             String error = task.getException().toString();
                             Toast.makeText(MainActivity.this, "registration failed" + error, Toast.LENGTH_SHORT).show();
                         }
                        }
                    });
                }

            }



        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }
    public void openRegister() {
        Intent intent = new Intent(this, Register_Activity.class);
        startActivity(intent);
    }
    public void openDashboard(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}