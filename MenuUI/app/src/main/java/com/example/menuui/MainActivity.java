package com.example.menuui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // temporary -- used to test nav menu
    private Button nav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // temporary -- testing nav menu
//        nav_button = (Button) findViewById(R.id.nav_menu_button);
//        nav_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendToNav();
//            }
//        });
    }

    public void sendToLogin(View view) {
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);
    }

    public void sendToRegister(View view) {
        Intent register_intent = new Intent(this, Register.class);
        startActivity(register_intent);
    }

    public void sendToRestaurantPage(View view) {
        Intent restaurant_page_intent = new Intent(this, RestaurantPage.class);
        startActivity(restaurant_page_intent);
    }

    public void sendToLanding(View view) {
        Intent landing_intent = new Intent(this, Landing.class);
        startActivity(landing_intent);
    }

    // send to test page with the "hamburger"/navigation menu
//    public void sendToNav() {
//        Intent nav_menu_intent = new Intent(this, NavMenu.class);
//        startActivity(nav_menu_intent);
//    }

}
