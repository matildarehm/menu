package com.example.menuui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button login_button;

    // temporary -- used to test nav menu
    private Button nav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.homepage);

        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();

            }
        });

        // temporary -- testing nav menu
//        nav_button = (Button) findViewById(R.id.nav_menu_button);
//        nav_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendToNav();
//            }
//        });
    }

    public void sendToLogin() {
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);

    }

    public void sendToRegister(View view) {
        Intent register_intent = new Intent(this, Register.class);
        startActivity(register_intent);
    }


    // send to restaurant test page
//    public void sendToRestaurantTest(View view) {
//        Intent restaurant_test_intent = new Intent(this, RestaurantTestPage.class);
//        startActivity(restaurant_test_intent);
//    }


    // send to test page with the "hamburger"/navigation menu
//    public void sendToNav() {
//        Intent nav_menu_intent = new Intent(this, NavMenu.class);
//        startActivity(nav_menu_intent);
//    }

}
