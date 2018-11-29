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

public class RestaurantPage extends AppCompatActivity {
    // DrawerLayout for the nav menu
    private DrawerLayout mDrawerLayout;

    // Dialog for filter dishes button
    Dialog filterDialog;

    // dishes list for recycler views
    private List<Dish> popular_dishes;
    private List<Dish> dishes;

    // RestaurantPage info
    private JSONObject restaurant_info;

    // Restaurant image url
    private String restaurant_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant);

        // Retrieve data that was passed through intent
        try {
            restaurant_info = new JSONObject(getIntent().getStringExtra("RESTAURANT_INFO"));
        } catch(Exception e) {
            Log.d("DEBUG", "ERROR: " + e.toString());
        }
        
        if (restaurant_info != null) {
            updateInfo();
        }

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
                                Intent home_intent = new Intent(RestaurantPage.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(RestaurantPage.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(RestaurantPage.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(RestaurantPage.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }


                        return true;
                    }
                });

        // get the dish data for the adapters
        getDishData();

        //recycler view for most popular dishes - reuse the dish adapter
        RecyclerView popular_rv = (RecyclerView)findViewById(R.id.restaurant_popular_recycler);
        // linear layout manager for the popular dish recycler view
        LinearLayoutManager popular_llm = new LinearLayoutManager(this);
        popular_rv.setLayoutManager(popular_llm);
        // call the dish adapter on the popular dishes
        DishAdapter popular_dish_adapter = new DishAdapter(popular_dishes);
        popular_rv.setAdapter(popular_dish_adapter);


        // create dialog for dish filter popup
        filterDialog = new Dialog(this);


        // recycler view for dish cards
        RecyclerView menu_rv = (RecyclerView)findViewById(R.id.restaurant_dishes_recycler);
        // linear layout manager for the dish recycler view
        LinearLayoutManager llm = new LinearLayoutManager(this);
        menu_rv.setLayoutManager(llm);
        // call the dish adapter on the restaurant dishes
        DishAdapter adapter = new DishAdapter(dishes);
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

        // Set listeners for UI Objects
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent search_intent = new Intent(RestaurantPage.this, SearchPage.class);
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


    // get the dish data for this restaurant
    private void getDishData() {
        // get the 3 most popular dishes for this restaurant
        // create new dish objects for these popular dishes
        popular_dishes = new ArrayList<>();
        popular_dishes.add(new Dish("Popular Dish1", "Description", 4.8, 95.0, R.drawable.menuyellow, "RestaurantName"));
        popular_dishes.add(new Dish("Popular Dish2", "Description", 4.5, 90.0, R.drawable.menuyellow, "RestaurantName"));
        popular_dishes.add(new Dish("Popular Dish3", "Description", 4.2, 92.0, R.drawable.menuyellow, "RestaurantName"));
        // create new dish objects for the remaining dishes
        dishes = new ArrayList<>();
        dishes.add(new Dish("Dish", "Description", 3.7, 77.5, R.drawable.menuyellow, "RestaurantName"));
        dishes.add(new Dish("Dish2", "Description", 3.5, 70.5, R.drawable.menuyellow, "RestaurantName"));
        dishes.add(new Dish("Dish3", "Description", 3.2, 80, R.drawable.menuyellow, "RestaurantName"));
    }


    // switch to the restaurant owner edit restaurant page
    public void goToEditRestaurant(View view) {
        // !!! check if the user is a verified owner of the restaurant !!!


        // go to the edit restaurant page
        // pass data through intent
        TextView restaurant_title = findViewById(R.id.restaurant_title);
        String restaurant = restaurant_title.getText().toString();
        TextView restaurant_location = findViewById(R.id.restaurant_location);
        String rest_loc = restaurant_location.getText().toString();
        TextView restaurant_phone = findViewById(R.id.restaurant_phone);
        String rest_phone = restaurant_phone.getText().toString();
        String rest_image = restaurant_image_url;

        Intent edit_restaurant_intent = new Intent(view.getContext(), RestaurantEdit.class);
        edit_restaurant_intent.putExtra("restaurant", restaurant);
        edit_restaurant_intent.putExtra("location", rest_loc);
        edit_restaurant_intent.putExtra("phone", rest_phone);
        edit_restaurant_intent.putExtra("image", rest_image);
        view.getContext().startActivity(edit_restaurant_intent);

    }

    /**
     * Update the UI elements with restaurant info passed from the landing page
     */
    private void updateInfo() {
        TextView restaurant_title = findViewById(R.id.restaurant_title);
        TextView restaurant_location = findViewById(R.id.restaurant_location);
        TextView restaurant_phone = findViewById(R.id.restaurant_phone);
        ImageView restaurant_image = findViewById(R.id.restaurant_main_image);
        try {
            String location = restaurant_info.getString("street") + ", "
                    + restaurant_info.getString("city") + ", "
                    + restaurant_info.getString("state");
            restaurant_title.setText(restaurant_info.getString("name"));
            restaurant_location.setText(location);
            restaurant_phone.setText(restaurant_info.getString("phone"));
            restaurant_image_url = restaurant_info.getString("image");
            new DownloadImageTask(restaurant_image).execute(restaurant_info.getString("image"));
        } catch (Exception e) {
            Log.d("DEBUG", "ERROR: Could not extract JSON from restaurant info");
        }
    }
    
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