package com.example.menuui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRestaurants extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    // restaurants list for recycler views
    private List<Restaurant> favorite_restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_restaurants_page);

        // handle nav bar implementation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_homepage:
                                // send to the landing page
                                Intent home_intent = new Intent(FavoriteRestaurants.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(FavoriteRestaurants.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(FavoriteRestaurants.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(FavoriteRestaurants.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }


                        return true;
                    }
                });
        //recycler view for the favorite restaurants -- using the restaurant adapter
        RecyclerView favorite_restaurants_rv = (RecyclerView)findViewById(R.id.favorite_restaurants_recycler);
        // linear layout manager for the favorite restaurants recycler view
        LinearLayoutManager favorite_restaurants_llm = new LinearLayoutManager(this);
        favorite_restaurants_rv.setLayoutManager(favorite_restaurants_llm);
        // get the favorite restaurants
        getFavoriteRestaurants();
        // call the restaurant adapter on the favorite restaurants
        FavoriteRestaurantAdapter favorite_restaurant_adapter = new FavoriteRestaurantAdapter(favorite_restaurants);
        favorite_restaurants_rv.setAdapter(favorite_restaurant_adapter);
    }

    // nav bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_bar_menu, menu);

        return true;
    }

    // get the user's favorite restaurants
    private void getFavoriteRestaurants() {
        // create new restaurant objects for the favorite restaurants
        favorite_restaurants = new ArrayList<>();
        favorite_restaurants.add(new Restaurant("Favorite Restaurant 1", R.drawable.menuyellow));
        favorite_restaurants.add(new Restaurant("Favorite Restaurant 2", R.drawable.menuyellow));
        favorite_restaurants.add(new Restaurant("Favorite Restaurant 3", R.drawable.menuyellow));
    }

}