package com.example.android.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    private Button login, register;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout usernameError, passwordError;
    TextView click_here_to_register;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = (EditText) findViewById(R.id.login_username_text);
        password = (EditText) findViewById(R.id.login_password_text);
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.login_register_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff = FirebaseDatabase.getInstance().getReference().child("Register_data").child("username");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username_ = snapshot.child("username_").getValue().toString();
                        String password_ = snapshot.child("password_").getValue().toString();
                        if (username_.equals(username) == false || password_.equals(password) == false) {
                            Toast.makeText(MainActivity.this, "invalid username or password", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRegister();
                    }
                });
            }


        });

    }
    public void openRegister() {
        Intent intent = new Intent(this, Register_Activity.class);
        startActivity(intent);
    }
}