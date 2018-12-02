package com.example.menuui;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDishes extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    // dishes list for recycler views
    private List<Dish> favorite_dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_dishes_page);// handle nav bar implementation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Menu App");

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
                                Intent home_intent = new Intent(FavoriteDishes.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(FavoriteDishes.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(FavoriteDishes.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(FavoriteDishes.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }


                        return true;
                    }
                });

        //recycler view for the favorite dishes
        RecyclerView favorite_dishes_rv = (RecyclerView)findViewById(R.id.favorite_dishes_recycler);
        // linear layout manager for the favorite dishes recycler view
        LinearLayoutManager favorite_dishes_llm = new LinearLayoutManager(this);
        favorite_dishes_rv.setLayoutManager(favorite_dishes_llm);
        // get the favorite dishes
        getFavoriteDishes();
        // call the dish adapter on the favorite dishes
        FavoriteDishAdapter favorite_dish_adapter = new FavoriteDishAdapter(favorite_dishes);
        favorite_dishes_rv.setAdapter(favorite_dish_adapter);
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

    // get the user's favorite dishes
    private void getFavoriteDishes() {
        // create new dish objects for the favorite dishes
        favorite_dishes = new ArrayList<>();
        favorite_dishes.add(new Dish("Fav Dish1", "Description", 4.8, 95.0, R.drawable.menuyellow, "Restaurant1"));
        favorite_dishes.add(new Dish("Fav Dish2", "Description", 4.5, 90.0, R.drawable.menuyellow, "Restaurant2"));
        favorite_dishes.add(new Dish("Fav Dish3", "Description", 4.2, 92.0, R.drawable.menuyellow, "Restaurant3"));
    }

}
