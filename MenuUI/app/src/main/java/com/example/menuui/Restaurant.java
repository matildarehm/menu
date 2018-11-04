package com.example.menuui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    Dialog filterDialog;

    // dishes list for recycler
    private List<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant);

        // set restaurant information
        Intent intent = getIntent();
        String restaurant_name = intent.getStringExtra("restaurantName");
        TextView restaurant_title = (TextView) findViewById(R.id.restaurant_title);
        restaurant_title.setText(restaurant_name);

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
                                Intent home_intent = new Intent(Restaurant.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(Restaurant.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(Restaurant.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(Restaurant.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }


                        return true;
                    }
                });

        // create dialog for filter popup
        filterDialog = new Dialog(this);


        // recycler view for dish cards
        RecyclerView rv = (RecyclerView)findViewById(R.id.restaurant_dishes_recycler);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initializeData();

        DishAdapter adapter = new DishAdapter(dishes);
        rv.setAdapter(adapter);

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

        return true;
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



    // class for the dish objects
    class Dish {
        String name;
        double avg_rating;
        double avg_recommended;
        int dish_img;

        Dish(String name, double avg_rating, double avg_recommended, int dish_img) {
            this.name = name;
            this.avg_rating = avg_rating;
            this.avg_recommended = avg_recommended;
            this.dish_img = dish_img;
        }
    }

    // initialize dish data
    private void initializeData() {
        dishes = new ArrayList<>();
        dishes.add(new Dish("Tacos", 3.7, 77.5, R.drawable.menuyellow));
        dishes.add(new Dish("Tacos2", 3.5, 70.5, R.drawable.menuyellow));
        dishes.add(new Dish("Tacos3", 3.2, 80, R.drawable.menuyellow));
    }

}



