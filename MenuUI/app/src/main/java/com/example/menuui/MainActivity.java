package com.example.menuui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.homepage);

        login_button = (Button) findViewbyId(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendToLogin();

            }


        });
    }


    public void sendToLogin(View view ) {
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);


    }

    public void sendToRegister(View view) {

    }
}
