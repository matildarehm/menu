package com.example.menuui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.net.URI;

public class Register extends AppCompatActivity {
    private Button register_button;
    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = (TextInputEditText) findViewById(R.id.input_name);
        email = (TextInputEditText) findViewById(R.id.input_email);
        password = (TextInputEditText) findViewById(R.id.input_password);

        register_button = (Button) findViewById(R.id.btn_signup);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_user();


        }
    });

}

    private void register_user() {
        String user_name = username.getText().toString();
        String user_email = email.getText().toString();
        String user_pass = password.getText().toString();

       Intent authenticate_intent = new Intent(this, PrivateRegister.class);
        authenticate_intent.putExtra("username", user_name);
        authenticate_intent.putExtra("email", user_email);
        authenticate_intent.putExtra("password", user_pass);
       startActivity(authenticate_intent);
    }

}

