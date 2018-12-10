package com.example.menuui;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
public class SearchPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    // Used for Nav bar
    private DrawerLayout mDrawerLayout;

    // Variables necessary for geo location through Google API
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private double latitude = 42.745994;
    private double longitude = -73.694263;
    private long UPDATE_INTERVAL = 2 * 1000;
    private long FASTEST_INTERVAL = 2000;

    // Search Limit
    private int numquery = 5;
    // Reset Variables
    private String last_query;
    private int page = 0;

    // Dialog for sort restaurants button
    Dialog filterDialog;
    private boolean price_btn = false;

    // Views Layout information and restaurant information to be passed
    private ArrayList<Integer> all_image_id = new ArrayList<Integer>();
    private ArrayList<Integer> all_label_id = new ArrayList<Integer>();
    private ArrayList<String> restaurant_info = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        // get search information sent by user
        last_query = getIntent().getStringExtra("SEARCH_INFO");
        createNavBar();

        // Geolocation code
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        // Populate view with clickable restaurant pictures
        if (checkLocation()) setupNearbyRestaurants(last_query);
        // create dialog for sort filter popup
        filterDialog = new Dialog(this);
    }
    // handle filter popup. sort searches by distance or price
    public void showFilterPopup(View view) {
        filterDialog.setContentView(R.layout.sort_rest_popup);
        TextView close_txt;

        RadioGroup group = (RadioGroup) filterDialog.findViewById(R.id.optiongroup);
        if (price_btn) group.check(R.id.sort_price);

        close_txt = (TextView) filterDialog.findViewById(R.id.close_txt);
        close_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup group = (RadioGroup) filterDialog.findViewById(R.id.optiongroup);
                int id = group.getCheckedRadioButtonId();
                boolean old_btn = price_btn;
                if (id == R.id.sort_dist) {
                    price_btn = false;
                }
                else {
                    price_btn = true;
                }
                if (old_btn != price_btn) page = 0;
                new SearchPage.populate().execute(last_query);
                filterDialog.dismiss();
            }
        });
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.show();
    }
    // change what page user is on for the search
    public void decreasePage(View view){
        if (page > 0){
            page -= 1;
            ImageButton front = (ImageButton) findViewById(R.id.front);
            front.setVisibility(View.VISIBLE);
            if (page == 0) {
                ImageButton back = (ImageButton) findViewById(R.id.back);
                back.setVisibility(View.INVISIBLE);
            }
            new SearchPage.populate().execute(last_query);
        }
    }
    // change what page user is on for the search
    public void increasePage(View view){
        if (page < 3){
            page += 1;
            ImageButton back = (ImageButton) findViewById(R.id.back);
            back.setVisibility(View.VISIBLE);
            if (page == 3) {
                ImageButton front = (ImageButton) findViewById(R.id.front);
                front.setVisibility(View.INVISIBLE);
            }
            new SearchPage.populate().execute(last_query);
        }
    }
    // Set up Nav bar (hamburger menu)
    public void createNavBar(){
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
                                Intent home_intent = new Intent(SearchPage.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(SearchPage.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(SearchPage.this, MainActivity.class);
                                startActivity(logout_intent);
                                break;
                        }
                        return true;
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
    // Dynamically create views to display queried restaurants
    // @param {String} search_query is the term the user wants to find restaurants with
    public void setupNearbyRestaurants(String search_query){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linLayout);
        for (int i = 0; i < numquery; i++){
            TextView label = new TextView(this);
            Integer label_id = label.generateViewId();
            all_label_id.add(label_id);
            label.setId(label_id);
            label.setTextSize(18);
            layout.addView(label);

            ImageView image = new ImageView(this);
            Integer image_id = image.generateViewId();
            all_image_id.add(image_id);
            image.setId(image_id);
            layout.addView(image);
        }

        // Pass null on first call to just get restaurants in area
        new SearchPage.populate().execute(search_query);
        for (int i = 0; i < numquery; i++){
            ImageView restImg = (ImageView) findViewById(all_image_id.get(i));
            final int finalI = i;
            restImg.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRestaurantPage(restaurant_info.get(finalI));
                }
            });
        }
    }

    // Set up searchbar in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_bar_menu, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search for food, restaurants, ...");
        searchView.onActionViewExpanded();

        // Refresh queried restaurants on search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                last_query = query;
                new SearchPage.populate().execute(query);
                page = 0;
                ImageButton front = (ImageButton) findViewById(R.id.front);
                front.setVisibility(View.VISIBLE);
                ImageButton back = (ImageButton) findViewById(R.id.back);
                back.setVisibility(View.INVISIBLE);
                searchView.clearFocus();
                searchView.setQuery("", false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    /**
     * Open a restaurant page activity for a specific restaurant name
     * @param {String} restaurant_name is the name of the restaurant to access menu items for
     */
    private void openRestaurantPage(String restaurant_info) {
        Intent restaurant_intent = new Intent(this, RestaurantPage.class);
        restaurant_intent.putExtra("RESTAURANT_INFO", restaurant_info);
        startActivity(restaurant_intent);
    }


    // GoogleAPI setup
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }

        if (mLocation != null) {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }
    // GoogleAPI setup
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("DEBUG", "Connection Suspended");
        mGoogleApiClient.connect();
    }
    // GoogleAPI setup
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("DEBUG", "Connection failed. Error: " + connectionResult.getErrorCode());
    }
    // GoogleAPI setup
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    // GoogleAPI setup
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    // GoogleAPI setup
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }
    // GoogleAPI setup
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


    /**
     * Show a custom dialog with a provided message
     * @param {String} message - Message to show the user in the dialog
     */
    public void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchPage.this);
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
            params.put("latitude", Double.toString(latitude));
            params.put("longitude", Double.toString(longitude));

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            try {
                SearchResponse sr = call.execute().body();
                if (sr.getTotal() == 0) {
                    message = "No restaurants found in area matching \"" + term + "\"";
                    // dialog(message);
                } else {
                    ArrayList<Business> businesses = sr.getBusinesses();
                    if (price_btn) {
                        Collections.sort(businesses, new Comparator<Business>() {
                            @Override
                            public int compare(Business o1, Business o2) {
                                if (o1.getPrice() == null){
                                    return (o2.getPrice() == null) ? 0 : 1;
                                }
                                if (o2.getPrice() == null) return -1;
                                return o1.getPrice().compareTo(o2.getPrice());
                            }
                        });
                    }
                    for (int i = 0; i < numquery; i++){
                        int j = page*5 + i;
                        TextView label = (TextView) findViewById(all_label_id.get(i));
                        ImageView image = (ImageView) findViewById(all_image_id.get(i));
                        String label_text = String.valueOf(j+1) + ". " + businesses.get(j).getName();
                        if (businesses.get(j).getPrice() != null) label_text = label_text + "\t\t\t" + businesses.get(j).getPrice();
                        label.setText(label_text);
                        new SearchPage.DownloadImageTask(image).execute(businesses.get(j).getImageUrl());
                        restaurant_info.add(getBusinessJSON(businesses.get(j)));
                    }
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

    // Changes the ImageViews using an image URL, in a separate thread
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

            int currentBitmapWidth = result.getWidth();
            int currentBitmapHeight = result.getHeight();

            int ivWidth = bmImage.getWidth();
            int ivHeight = bmImage.getHeight();
            int newWidth = ivWidth;

            int newHeight = (int) Math.floor((double) currentBitmapHeight *( (double) newWidth / (double) currentBitmapWidth));

            Bitmap newbitMap = Bitmap.createScaledBitmap(result, newWidth, newHeight, true);

            bmImage.setImageBitmap(newbitMap);
        }
    }

    // GEO LOCATION FUNCTIONS
    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }
    // Alert user if Location Settings are not turned on
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
    // Check if Location Settings are turned on
    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Convert business information to a JSON String
     * @param {Business} b is the business object that we are extracting info from
     * @return {String} ret is the info in a JSON style string
     */
    private String getBusinessJSON(Business b) {

        String ret = "{\"name\":\"" + b.getName() + "\",";
        ret += "\"image\":\"" + b.getImageUrl() + "\",";
        ret += "\"phone\":\"" + phoneString(b.getPhone()) + "\",";
        ret += "\"street\":\"" + b.getLocation().getAddress1() + "\",";
        ret += "\"city\":\"" + b.getLocation().getCity() + "\",";
        ret += "\"state\":\"" + b.getLocation().getState() + "\",";
        ret += "\"zip\":\"" + b.getLocation().getZipCode() + "\"}";

        return ret;
    }


    /**
     * Change a 12 digit number into a phone number format
     * @param {String] phone is the 12 digit number to be converted to a string
     * @return {String} ret is the number in a phone number format +X (XXX) XXX-XXXX
     * Returns the input if it is not a 12 digit string
     */
    private String phoneString(String phone) {
        if (phone.length() != 12) {
            return phone;
        }
        String ret = "+1";
//        ret += phone.charAt(0) + phone.charAt(1);
        ret += " (" + phone.charAt(2) + phone.charAt(3) + phone.charAt(4) + ") ";
        ret += phone.charAt(5) + phone.charAt(6) + phone.charAt(7) + "-";
        ret += phone.charAt(8) + phone.charAt(9) + phone.charAt(10) + phone.charAt(11);
        return ret;
    }
}
