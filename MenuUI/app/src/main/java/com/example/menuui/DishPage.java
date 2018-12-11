package com.example.menuui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DishPage extends AppCompatActivity {
    // DrawerLayout for the nav menu
    private Button leave_review;
    private DrawerLayout mDrawerLayout;

    private String dish;
    private String restaurant_info;
    String restaurant_name;
    private List<Review> reviews;
    FetchReviews other_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_page);

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

                        // Update UI based on item selected
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_homepage:
                                // send to the landing page
                                Intent home_intent = new Intent(DishPage.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(DishPage.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // save the user favorites hashmap to the shared preferences
                                ((MenuApp)DishPage.this.getApplication()).saveHashMap();
                                // save the reviews and ratings
                                ((MenuApp)DishPage.this.getApplication()).saveReviews();
                                ((MenuApp)DishPage.this.getApplication()).saveRatings();
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(DishPage.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }
                        return true;
                    }
                });

        // get dish name from intent -- and set the name
        Intent intent = getIntent();
        dish = intent.getStringExtra("dishName");
       restaurant_name = intent.getStringExtra("restName");
        Log.i("restName", restaurant_name);
        TextView dish_title = (TextView) findViewById(R.id.dish_title);
        dish_title.setText(dish);
        // get the restaurant info from intent -- for the back to restaurant button
        restaurant_info = intent.getStringExtra("RESTAURANT_INFO");


        // get the dish reviews
        reviews = new ArrayList<Review>();
        reviews = getReviewsFromDB();
        if (reviews.size() != 0) {
            // there are existing reviews
            // Recycler View for reviews
            RecyclerView rv = (RecyclerView) findViewById(R.id.review_recycler);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv.setLayoutManager(llm);
            // review recycler adapter
            ReviewAdapter adapter = new ReviewAdapter(reviews);
            rv.setAdapter(adapter);
        }

        // get reviews
//        getReviews();
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



    // get/set reviews
    private void getReviews(){
        reviews = new ArrayList<>();
        List<Review> fetched_reviews = getReviewsFromDB();
        other_reviews = new FetchReviews(this, restaurant_name);
        Log.i("reviews working", "rest_name");
        RequestThread get_request = new RequestThread();
        get_request.start();

        synchronized (get_request) {
            try {
                System.out.println("Waiting for request get to complete...");
                get_request.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        ReviewThread get_reviews = new ReviewThread();
        get_reviews.start();

        synchronized (get_reviews) {
            try {
                System.out.println("Waiting for dish get to complete...");
                get_reviews.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        List<Review> more_reviews = other_reviews.get_all_reviews();
        List<Review> review_union = new ArrayList<Review>();
        review_union.addAll(more_reviews);
        review_union.addAll(fetched_reviews);
        reviews = review_union;
    }

    public void sendToLeaveReview(View view) {
        // get the dish name
        TextView dish_name = (TextView)findViewById(R.id.dish_title);
        String dish = dish_name.getText().toString();

        Intent leave_review_intent = new Intent(this, LeaveReview.class);
        leave_review_intent.putExtra("dish", dish);
        leave_review_intent.putExtra("RESTAURANT_INFO", restaurant_info);
        startActivity(leave_review_intent);
    }

    public void sendBackToRestaurant(View view) {
        // use the restaurant info to go back to the restaurant page (load the restaurant page)
        Intent restaurant_intent = new Intent(this, RestaurantPage.class);
        restaurant_intent.putExtra("RESTAURANT_INFO", restaurant_info);
        startActivity(restaurant_intent);
    }

    // temp: get review lists
    public List<Review> getReviewsFromDB() {
        // return ((MenuApp) this.getApplication()).getReviews(dish);
        return ((MenuApp) this.getApplication()).getDishReviews(dish);
    }

    class RequestThread extends Thread {

        @Override
        public void run() {
            synchronized (this) {
                other_reviews.get_request();
                notify();
            }

        }

    }

    class ReviewThread extends Thread {

        @Override
        public void run() {
            synchronized (this) {
                reviews.addAll(other_reviews.get_all_reviews());
                notify();
            }

        }

    }
}


