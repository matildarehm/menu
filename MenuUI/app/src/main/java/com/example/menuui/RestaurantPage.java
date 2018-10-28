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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RestaurantPage extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    Dialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_page);

        // handle nav bar implementation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id. drawer_layout);

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
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(RestaurantPage.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(RestaurantPage.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                        }


                        return true;
                    }
                });

        // take out?
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }
                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        // create dialog for filter popup
        filterDialog = new Dialog(this);


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


    // handle filter popup
    public void showFilterPopup(View view) {
        TextView close_txt;
        Button btn_filter_alpha;
        Button btn_filter_rating;
        filterDialog.setContentView(R.layout.filter_dishes_popup);
        close_txt = (TextView) filterDialog.findViewById(R.id.close_txt);
        btn_filter_alpha = (Button)filterDialog.findViewById(R.id.filter_dishes_alpha);
        btn_filter_rating = (Button)filterDialog.findViewById(R.id.filter_dishes_rating);
        close_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.show();
    }

}