package com.example.menuui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {
    private Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            signUp();

        }
    });
}

    public void signUp() {
        // TODO: Implement homepage, switch to homepage
//        Intent homepage_intent = new Intent(this, Homepage.class);
//        startActivity(homepage_intent);
    }
}

