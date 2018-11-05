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
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Restaurant extends AppCompatActivity {
    // DrawerLayout for the nav menu
    private DrawerLayout mDrawerLayout;

    // Dialog for filter dishes button
    Dialog filterDialog;

    // dishes list for recycler
    private List<Dish> dishes;

    // Restaurant info
    private JSONObject restaurant_info;

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


    // get/set dish data
    private void initializeData() {
        dishes = new ArrayList<>();
        dishes.add(new Dish("Tacos", "Description", 3.7, 77.5, R.drawable.menuyellow));
        dishes.add(new Dish("Tacos2", "Description", 3.5, 70.5, R.drawable.menuyellow));
        dishes.add(new Dish("Tacos3", "Description", 3.2, 80, R.drawable.menuyellow));
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