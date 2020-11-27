package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {


    TextInputLayout regemail, regpassword, regphone, regprofession;
    Button regsignup;
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


        regsignup = findViewById(R.id.reg_signup);
        mAuth = FirebaseAuth.getInstance();
        regsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){


                regemail = findViewById(R.id.reg_email);
                regpassword = findViewById(R.id.reg_password);
                regphone = findViewById(R.id.reg_phone);
                regprofession = findViewById(R.id.reg_profession);

                String reg_email_ = regemail.getEditText().getText().toString();
                String reg_password_ = regpassword.getEditText().getText().toString();
                String reg_phone_ = regphone.getEditText().getText().toString();
                String reg_professsion_ = regprofession.getEditText().getText().toString();


                if(TextUtils.isEmpty(reg_email_)){
                    regemail.setError("Username is required");
                }
                if(TextUtils.isEmpty(reg_password_)){
                    regpassword.setError("Username is required");
                }
                if(TextUtils.isEmpty(reg_phone_)){
                    regphone.setError("Username is required");
                }
                if(TextUtils.isEmpty(reg_professsion_)){
                    regprofession.setError("Username is required");
                }

                boolean regex_email = Pattern.matches("^[a-z0-9]+[\\._]?[a-z0-9]+[@]\\w+[.]\\w{2,3}$",reg_email_);
                if(regex_email == false){
                    regemail.setError("email format is wrong");
                }
                boolean regex_phone = Pattern.matches("[0-9]{10}",reg_phone_);
                if(regex_phone == false){
                    regphone.setError("enter a 10 digit phone number");
                }


                mAuth.createUserWithEmailAndPassword(reg_email_,reg_password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                            String onlineUserId = mUser.getUid();
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference().child("UserInfo").child(onlineUserId);
                            UserHelper userHelper = new UserHelper(reg_email_,reg_password_,reg_phone_,reg_professsion_);
                            reference.push().setValue(userHelper);
                            Toast.makeText(Register_Activity.this, "registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register_Activity.this,Verification1.class);
                            intent.putExtra("phoneNo",reg_phone_);
                            startActivity(intent);
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Register_Activity.this, "registration failed" + error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });



                //Entering values into the constructor
                //UserHelper helper = new UserHelper(Email,Password,phoneNo,Profession);
                //Entering values into the database
                //reference.child(Email).setValue(helper);


            }
        });

    }

    private void openLogin() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
