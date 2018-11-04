package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // temporary -- used to test nav menu
    private Button nav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

    }

    public void sendToLogin(View view) {
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);
    }

    public void sendToRegister(View view) {
        Intent register_intent = new Intent(this, Register.class);
        startActivity(register_intent);
    }

}
