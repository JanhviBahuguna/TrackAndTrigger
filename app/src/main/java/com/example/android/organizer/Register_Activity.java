package com.example.android.organizer;


import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {

    private  EditText username_register, password_register, email_register, phone_register;
    private Button register,login;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    long max_id=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        username_register = (EditText) findViewById(R.id.username_register_text);
        password_register = (EditText) findViewById(R.id.register_password_text);
        email_register = (EditText) findViewById(R.id.register_email_text);
        phone_register = (EditText) findViewById(R.id.register_phone_text);
        register = (Button) findViewById(R.id.register_button);
        login = (Button) findViewById(R.id.register_login_button);
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
                 Toast.makeText(Register_Activity.this, "username not entered", Toast.LENGTH_LONG).show();
             }
             if(TextUtils.isEmpty(user_password_)){
                 Toast.makeText(Register_Activity.this, "password not entered", Toast.LENGTH_LONG).show();
             }
             if(TextUtils.isEmpty(user_email_)){
                 Toast.makeText(Register_Activity.this, "email not entered", Toast.LENGTH_LONG).show();
             }
             if(TextUtils.isEmpty(user_phone_)){
                 Toast.makeText(Register_Activity.this, "phone not entered", Toast.LENGTH_LONG).show();
             }

             boolean regex_email = Pattern.matches("^[a-z0-9]+[\\._]?[a-z0-9]+[@]\\w+[.]\\w{2,3}$","user_email_");
             if(regex_email == false){
                 Toast.makeText(Register_Activity.this, "email entered is invalid", Toast.LENGTH_LONG).show();
             }
             boolean regex_phone = Pattern.matches("[0-9]{10}","user_phone_");
             if(regex_phone == false){
                 Toast.makeText(Register_Activity.this, "phone entered is invalid", Toast.LENGTH_LONG).show();
             }



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

