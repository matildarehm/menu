package com.example.menuui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class LandingPage extends AppCompatActivity {
    private boolean retrieving_data = false;
    private int restaurants = 0;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

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
                                Intent home_intent = new Intent(LandingPage.this, LandingPage.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(LandingPage.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(LandingPage.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(LandingPage.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }


                        return true;
                    }
                });

        // Pass null on first call to just get restaurants in area
        new populate().execute();

        final SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Search for food, restaurants, ...");
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new populate().execute(query);
                // searchView.setQuery("", false); // Not sure if this is best UX
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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


    // DISPLAY CUSTOM DIALOG
    public void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    /**
     * Call the Yelp API and populate landing page with restaurant data
     * @param {String} term - search query, will provide generic results if null
     */
    private class populate extends AsyncTask<String, String, String> {
        @Override
        public String doInBackground(String... args) {
            String term;
            String message = "";
            if (args.length > 0) {
                term = args[0];
            } else {
                term = null;
            }

            String apiKey = "oAfBKQD-m09-PIkZv5_XSGliHVRdchALtbNU-OvsF9xVCNjFZ03zyjJ5lp_QgaLamH0HN0KxRfJoFGL762HS2EkBzOGBXMG53I0M9S2IX_BFvzjwcw6kgmaurd2WW3Yx";
            YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
            YelpFusionApi yelpFusionApi = null;
            try {
                yelpFusionApi = apiFactory.createAPI(apiKey);
            } catch (Exception e) {
                Log.d("DEBUG", "Error: " + e.toString());
            }

            Map<String, String> params = new HashMap<>();

            // Use search term if queried
            if (term != null) {
                params.put("term", term);
            }
            params.put("latitude", "40.581140");
            params.put("longitude", "-111.914184");

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            try {
                SearchResponse sr = call.execute().body();
                if (sr.getTotal() == 0) {
                    Log.d("DEBUG", "No restaurants found in area matching \"" + term + "\"");
                    // TODO: Implement dialog popup
                    message = "No restaurants found in area matching \"" + term + "\"";
                    // dialog(message);
                } else {
                    ArrayList<Business> businesses = sr.getBusinesses();

                    // Access the landing page's restaurant labels and images
                    TextView label_1 = (TextView) findViewById(R.id.rest_label_1);
                    TextView label_2 = (TextView) findViewById(R.id.rest_label_2);
                    TextView label_3 = (TextView) findViewById(R.id.rest_label_3);
                    ImageView image_1 = (ImageView) findViewById(R.id.rest_img_1);
                    ImageView image_2 = (ImageView) findViewById(R.id.rest_img_2);
                    ImageView image_3 = (ImageView) findViewById(R.id.rest_img_3);

                    // Change the restaurant labels to restaurant names
                    label_1.setText(businesses.get(0).getName());
                    label_2.setText(businesses.get(1).getName());
                    label_3.setText(businesses.get(2).getName());
                    new DownloadImageTask(image_1).execute(businesses.get(0).getImageUrl());
                    new DownloadImageTask(image_2).execute(businesses.get(1).getImageUrl());
                    new DownloadImageTask(image_3).execute(businesses.get(2).getImageUrl());
                }
            } catch (Exception e) {
                Log.d("DEBUG", "Error: " + e.toString());
            }

            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            if (message != "") {
                dialog(message);
            }
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

