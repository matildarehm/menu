package com.example.menuui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RestaurantEdit extends AppCompatActivity {
    // DrawerLayout for the nav menu
    private DrawerLayout mDrawerLayout;

    // dishes list for recycler views
    private List<Dish> popular_dishes;
    private List<Dish> dishes;

    // Restaurant image url
    private String restaurant_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_owner_edit);

        // Retrieve data that was passed through intent
        Intent intent = getIntent();
        String restaurant_name = intent.getStringExtra("restaurant");
        TextView restaurant_title = (TextView)findViewById(R.id.restaurant_title);
        restaurant_title.setText(restaurant_name);
        String restaurant_location = intent.getStringExtra("location");
        TextView restaurant_address = (TextView)findViewById(R.id.restaurant_location);
        restaurant_address.setText(restaurant_location);
        String restaurant_phone = intent.getStringExtra("phone");
        TextView phone = (TextView)findViewById(R.id.restaurant_phone);
        phone.setText(restaurant_phone);
        String restaurant_image = intent.getStringExtra("image");
        ImageView image = (ImageView)findViewById(R.id.restaurant_main_image);
        new DownloadImageTask(image).execute(restaurant_image);


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
                                Intent home_intent = new Intent(RestaurantEdit.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(RestaurantEdit.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(RestaurantEdit.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(RestaurantEdit.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }
                        return true;
                    }
                });

        //recycler view for most popular dishes - reuse the dish adapter
        RecyclerView popular_rv = (RecyclerView)findViewById(R.id.restaurant_popular_recycler);
        // linear layout manager for the popular dish recycler view
        LinearLayoutManager popular_llm = new LinearLayoutManager(this);
        popular_rv.setLayoutManager(popular_llm);
        // get the 3 most popular dishes
        getPopularDishes();
        // call the dish adapter on the popular dishes
        EditDishAdapter popular_dish_adapter = new EditDishAdapter(popular_dishes);
        popular_rv.setAdapter(popular_dish_adapter);


        // recycler view for dish cards
        RecyclerView menu_rv = (RecyclerView)findViewById(R.id.restaurant_dishes_recycler);
        // linear layout manager for the dish recycler view
        LinearLayoutManager llm = new LinearLayoutManager(this);
        menu_rv.setLayoutManager(llm);
        // get the dish data for the adapter
        getDishData();
        // call the dish adapter on the restaurant dishes
        EditDishAdapter adapter = new EditDishAdapter(dishes);
        menu_rv.setAdapter(adapter);

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
        final SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search for food, restaurants, ...");
        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // searchView.setQuery("", false); // Not sure if this is best UX
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    // get the 3 most popular dishes for this restaurant
    private void getPopularDishes() {
        // create new dish objects for these popular dishes
        popular_dishes = new ArrayList<>();
        popular_dishes.add(new Dish("Fav Dish1", "Description", 4.8, 95.0, R.drawable.menuyellow, "RestaurantName"));
        popular_dishes.add(new Dish("Fav Dish2", "Description", 4.5, 90.0, R.drawable.menuyellow, "RestaurantName"));
        popular_dishes.add(new Dish("Fav Dish3", "Description", 4.2, 92.0, R.drawable.menuyellow, "RestaurantName"));
    }


    // get the dish data for this restaurant
    private void getDishData() {
        // create new dish objects
        dishes = new ArrayList<>();
        dishes.add(new Dish("Dish", "Description", 3.7, 77.5, R.drawable.menuyellow, "RestaurantName"));
        dishes.add(new Dish("Dish2", "Description", 3.5, 70.5, R.drawable.menuyellow, "RestaurantName"));
        dishes.add(new Dish("Dish3", "Description", 3.2, 80, R.drawable.menuyellow, "RestaurantName"));
    }


    // save the edits and return to the main restaurant page
    public void saveEditRestaurant(View view) {
        // return to the main restaurant page

    }


    // function that gets the restaurant image
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon11 = null;

            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("DEBUG", "Error: " + e.toString());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
        }
    }
}