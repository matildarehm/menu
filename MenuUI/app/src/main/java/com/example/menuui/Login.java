package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private TextView fyp_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        fyp_button = (TextView) findViewById(R.id.fyp);
        fyp_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendToFYP();

            }
        });
    }

    public void sendToFYP() {
        Intent fyp_intent = new Intent(this, FYP.class);
        startActivity(fyp_intent);

    }
}
