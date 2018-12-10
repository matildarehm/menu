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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRestaurants extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    // restaurants list for recycler views
    private List<Restaurant> favorite_restaurants;

    // list of favorite restaurants info
    private List<String> favorite_restaurants_info;

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

                        // Update the UI based on the item selected
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_homepage:
                                // send to the landing page
                                Intent home_intent = new Intent(FavoriteRestaurants.this, Landing.class);
                                startActivity(home_intent);
                                break;
                        case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(FavoriteRestaurants.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // save the user favorites hashmap to the shared preferences
                                ((MenuApp)FavoriteRestaurants.this.getApplication()).saveHashMap();
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
        FavoriteRestaurantAdapter favorite_restaurant_adapter = new FavoriteRestaurantAdapter(favorite_restaurants, favorite_restaurants_info);
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

    // add search bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_bar_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search for food, restaurants, ...");
        searchView.onActionViewExpanded();

        // Set listeners for UI Objects
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent search_intent = new Intent(FavoriteRestaurants.this, SearchPage.class);
                search_intent.putExtra("SEARCH_INFO", query);
                startActivity(search_intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    // get the user's favorite restaurants
    private void getFavoriteRestaurants() {
        // get the current user's username
        String user = ((MenuApp) this.getApplication()).getCurrentUser();

        // create and add to favorite restaurants lists
        favorite_restaurants = new ArrayList<>();

        // get this user's favorite restaurants
        favorite_restaurants_info = ((MenuApp) this.getApplication()).getUserFavorites(user);

        if (favorite_restaurants_info.size() == 0) {
            // this user has no favorite restaurants
            // set text visibility to true
            TextView no_favs_tv = (TextView)findViewById(R.id.no_favs_text);
            no_favs_tv.setVisibility(View.VISIBLE);
        }

        else {
            TextView no_favs_tv = (TextView)findViewById(R.id.no_favs_text);
            no_favs_tv.setVisibility(View.INVISIBLE);
            // create new restaurant objects for the favorite restaurants
            JSONObject restaurant_json = new JSONObject();
            String restaurant_name;
            for (int i = 0; i < favorite_restaurants_info.size(); i++) {
                try {
                    restaurant_json = new JSONObject(favorite_restaurants_info.get(i));
                    // get restaurant info from json
                    restaurant_name = restaurant_json.getString("name");
                    favorite_restaurants.add(new Restaurant(restaurant_name, R.drawable.menuyellow, i));
                }
                catch (Exception e) {
                    Log.d("DEBUG", "ERROR: " + e.toString());
                }
            }
        }

        // dummy data
//        favorite_restaurants = new ArrayList<>();
//        favorite_restaurants.add(new Restaurant("Favorite Restaurant 1", R.drawable.menuyellow, 1));
//        favorite_restaurants.add(new Restaurant("Favorite Restaurant 2", R.drawable.menuyellow, 2));
//        favorite_restaurants.add(new Restaurant("Favorite Restaurant 3", R.drawable.menuyellow, 3));
    }

}