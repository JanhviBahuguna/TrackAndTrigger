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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {

    private  EditText username_register, password_register, email_register, phone_register;
    private Button register,login;
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        username_register = findViewById(R.id.username_register_text);
        password_register =  findViewById(R.id.register_password_text);
        email_register =  findViewById(R.id.register_email_text);
        phone_register =  findViewById(R.id.register_phone_text);

        register =  findViewById(R.id.register_button);
        login =  findViewById(R.id.register_login_button);

        mAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view){
             rootNode = FirebaseDatabase.getInstance();
             reference = rootNode.getReference();


             String user_register_ = username_register.getText().toString();
             String user_password_ = password_register.getText().toString();
             String user_email_ = email_register.getText().toString();
             String user_phone_ = phone_register.getText().toString();


             if(TextUtils.isEmpty(user_register_)){
                 username_register.setError("Username is required");
             }
             if(TextUtils.isEmpty(user_password_)){
                 password_register.setError("Username is required");
             }
             if(TextUtils.isEmpty(user_email_)){
                 email_register.setError("Username is required");
             }
             if(TextUtils.isEmpty(user_phone_)){
                 phone_register.setError("Username is required");
             }

             boolean regex_email = Pattern.matches("^[a-z0-9]+[\\._]?[a-z0-9]+[@]\\w+[.]\\w{2,3}$","user_email_");
             if(regex_email == true){
                 email_register.setError("email format is wrong");
             }
             boolean regex_phone = Pattern.matches("[0-9]{10}","user_phone_");
             if(regex_phone == true){
                 phone_register.setError("enter a 10 digit phone number");
             }

             mAuth.createUserWithEmailAndPassword(user_email_,user_password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         openLogin();
                     }
                     else
                     {
                         String error = task.getException().toString();
                         Toast.makeText(Register_Activity.this, "registration failed" + error, Toast.LENGTH_SHORT).show();
                     }

                 }
             });

            Register_data register_data = new Register_data(user_register_,user_password_,user_phone_,user_email_);
             reference.child("users").child(user_register_).setValue(register_data);
             Toast.makeText(Register_Activity.this, "registered successfully", Toast.LENGTH_SHORT).show();


         }


        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }

        });
    }


    public void openLogin() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
}

