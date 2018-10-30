package com.example.menuui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.net.URI;

public class Register extends AppCompatActivity {
    private Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

//        hideActionBar();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            signUp();

        }
    }

    );

}

//    public void hideActionBar() {
//        this.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_
//        );
//    }

    public void signUp() {
        Intent landing_intent = new Intent(this, Landing.class);
        startActivity(landing_intent);
    }

}

