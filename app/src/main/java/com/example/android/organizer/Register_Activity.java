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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {


    TextInputLayout regemail, regpassword, regphone, regprofession,regusername;
    Button regsignup;
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    public static String reg_email_;
    public static String reg_phone_;
    public static String reg_password_;
    public static String reg_profession_;
    public static String reg_username_;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


        regsignup = findViewById(R.id.reg_signup);
        mAuth = FirebaseAuth.getInstance();
        regsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                regusername = findViewById(R.id.reg_username);
                regemail = findViewById(R.id.reg_email);
                regpassword = findViewById(R.id.reg_password);
                regphone = findViewById(R.id.reg_phone);
                regprofession = findViewById(R.id.reg_profession);
                 reg_username_ = regusername.getEditText().getText().toString();
                 reg_email_ = regemail.getEditText().getText().toString();
                 reg_password_ = regpassword.getEditText().getText().toString();
                 reg_phone_ = regphone.getEditText().getText().toString();
                 reg_profession_ = regprofession.getEditText().getText().toString();

                if(TextUtils.isEmpty(reg_username_)){
                    regusername.setError("Username is required");
                }
                if(TextUtils.isEmpty(reg_email_)){
                    regemail.setError("Email is required");
                }
                if(TextUtils.isEmpty(reg_password_)){
                    regpassword.setError("Password is required");
                }
                if(TextUtils.isEmpty(reg_phone_)){
                    regphone.setError("Phone is required");
                }
                if(TextUtils.isEmpty(reg_profession_)){
                    regprofession.setError("Profession is required");
                }

                boolean regex_email = Pattern.matches("^[a-z0-9]+[\\._]?[a-z0-9]+[@]\\w+[.]\\w{2,3}$",reg_email_);
                boolean regex_student = Pattern.matches("[fp][2][0][0-9]{6}[@][a-z]+[.][b][i][t][s][-][p][i][l][a][n][i][.][a][c][.][i][n]",reg_email_);
                boolean regex_faculty = Pattern.matches("[a-z]+[@][a-z]+[.][b][i][t][s][-][p][i][l][a][n][i][.][a][c][.][i][n]",reg_email_);
                if(regex_email == false && regex_student == false && regex_faculty == false){
                    regemail.setError("Email format is wrong");
                }
                boolean regex_phone = Pattern.matches("[0-9]{10}",reg_phone_);
                if(regex_phone == false){
                    regphone.setError("Enter a 10 digit phone number");
                }
                boolean regex_username = Pattern.matches("([^\\\\s]*[\\\\s]+[^\\\\s]*)+",reg_username_);
                if(regex_username == true){
                    regusername.setError("Enter username without any white spaces");
                }
                mAuth.createUserWithEmailAndPassword(reg_email_,reg_password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register_Activity.this,"Please verify email to login.",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Register_Activity.this,Verification1.class);
                                    intent.putExtra("phoneNo",reg_phone_);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register_Activity.this,"Unsuccessful registration inner loop.",Toast.LENGTH_LONG).show();
                                }
                            });


                        }

                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Register_Activity.this, "registration failed" + error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }

}
