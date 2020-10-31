package com.example.android.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout usernameError, passwordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.login_username_text);
        password = (EditText) findViewById(R.id.login_password_text);
        login = (Button) findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }

            private void SetValidation() {
                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
            }
        });
//
//        public void SetValidation () {
//            // Check for a valid email address.
//            //TODO store data using firebase
//            if (email.getText().toString().isEmpty()) {
//                emailError.setError(getResources().getString(R.string.email_error));
//                isEmailValid = false;
//            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
//                emailError.setError(getResources().getString(R.string.error_invalid_email));
//                isEmailValid = false;
//            } else  {
//                isEmailValid = true;
//                emailError.setErrorEnabled(false);
//            }
//
//            // Check for a valid password.
//            if (password.getText().toString().isEmpty()) {
//                passError.setError(getResources().getString(R.string.password_error));
//                isPasswordValid = false;
//            } else if (password.getText().length() < 6) {
//                passError.setError(getResources().getString(R.string.error_invalid_password));
//                isPasswordValid = false;
//            } else  {
//                isPasswordValid = true;
//                passError.setErrorEnabled(false);
//            }
//
//            if (isEmailValid && isPasswordValid) {
//                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
//            }
//
//        }

    }
}