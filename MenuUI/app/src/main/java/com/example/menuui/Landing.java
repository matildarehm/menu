package com.example.menuui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.menuui.GetRestaurantId;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Hour;
import com.yelp.fusion.client.models.SearchResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;

public class Landing extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

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

    // for nav bar drawer layout
    private DrawerLayout mDrawerLayout;

    // RestaurantPage info to be passed to restaurant page
    private String restaurant_1_info;
    private String restaurant_2_info;
    private String restaurant_3_info;

    // Database
    DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Make the navbar (hamburger menu)
        createNavBar();
        // Geolocation code
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        // Set up views for nearby restaurants
        if (checkLocation()) setupNearbyRestaurants();
    }

    // Set up Nav Bar (hamburger menu)
    public void createNavBar(){
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
                                Intent home_intent = new Intent(Landing.this, Landing.class);
                                startActivity(home_intent);
                                break;
                            case R.id.nav_favorite_dishes:
                                // send to favorite dishes page
                                Intent fav_dishes_page_intent = new Intent(Landing.this, FavoriteDishes.class);
                                startActivity(fav_dishes_page_intent);
                                break;
                            case R.id.nav_favorite_restaurants:
                                // send to favorite restaurants page
                                Intent fav_rest_page_intent = new Intent(Landing.this, FavoriteRestaurants.class);
                                startActivity(fav_rest_page_intent);
                                break;
                            case R.id.nav_logout:
                                // log out and send to the welcome page
                                Intent logout_intent = new Intent(Landing.this, MainActivity.class);
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
    // Populate views with information of nearby restaurants
    public void setupNearbyRestaurants(){
        // Pass null on first call to just get restaurants in area
        new populate().execute();

        // redirect to restaurant page
        final ImageView restaurantImg1 = (ImageView) findViewById(R.id.rest_img_1);
        final ImageView restaurantImg2 = (ImageView) findViewById(R.id.rest_img_2);
        final ImageView restaurantImg3 = (ImageView) findViewById(R.id.rest_img_3);

        restaurantImg1.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRestaurantPage(restaurant_1_info);
            }
        });

        restaurantImg2.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRestaurantPage(restaurant_2_info);
            }
        });

        restaurantImg3.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRestaurantPage(restaurant_3_info);
            }
        });
    }

    // Set up search bar in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_bar_menu, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search for food, restaurants, ...");
        searchView.onActionViewExpanded();

        // Send query information to SearchPage Activity on search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent search_intent = new Intent(Landing.this, SearchPage.class);
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

    // Google API
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

    // Google API
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("DEBUG", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    // Google API
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("DEBUG", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    // Google API
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    // Google API
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    // Google API
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

    // Google API
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Landing.this);
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

                    // Access the landing page's restaurant labels and images
                    Log.d("INFO", "1");
                    TextView label_1 = (TextView) findViewById(R.id.rest_label_1);
                    TextView label_2 = (TextView) findViewById(R.id.rest_label_2);
                    TextView label_3 = (TextView) findViewById(R.id.rest_label_3);
                    Log.d("INFO", "2");
                    ImageView image_1 = (ImageView) findViewById(R.id.rest_img_1);
                    ImageView image_2 = (ImageView) findViewById(R.id.rest_img_2);
                    ImageView image_3 = (ImageView) findViewById(R.id.rest_img_3);
                    Log.d("INFO", "3");

                    // Change the restaurant labels to restaurant names
                    String n1 = businesses.get(0).getName();
                    String n2 = businesses.get(1).getName();
                    label_1.setText(n1);
                    label_2.setText(n2);
                    label_3.setText(businesses.get(2).getName());
                    Log.d("INFO", "TEXT SET");
                    new DownloadImageTask(image_1).execute(businesses.get(0).getImageUrl());
                    new DownloadImageTask(image_2).execute(businesses.get(1).getImageUrl());
                    new DownloadImageTask(image_3).execute(businesses.get(2).getImageUrl());

                    // Reset and update restaurant info
                    restaurant_1_info = getBusinessJSON(businesses.get(0));
                    restaurant_2_info = getBusinessJSON(businesses.get(1));
                    restaurant_3_info = getBusinessJSON(businesses.get(2));
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
        ret += "\"id\":\"" + db.findRestaurant(b.getName(), b.getLocation().getAddress1()) + "\",";
        ret += "\"image\":\"" + b.getImageUrl() + "\",";
        ret += "\"phone\":\"" + phoneString(b.getPhone()) + "\",";
        ret += "\"street\":\"" + b.getLocation().getAddress1() + "\",";
        ret += "\"city\":\"" + b.getLocation().getCity() + "\",";
        ret += "\"state\":\"" + b.getLocation().getState() + "\",";
        ret += "\"zip\":\"" + b.getLocation().getZipCode() + "\"}";
        return ret;
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
    // Function for Surprise Me's onClick
    // Send a random restaurant's information to Restaurant activity
    public void surpriseRestaurant(View view){
        List<String> rest_list = new ArrayList<String>();
        rest_list.add(restaurant_1_info);
        rest_list.add(restaurant_2_info);
        rest_list.add(restaurant_3_info);
        String random_rest = rest_list.get(new Random().nextInt(rest_list.size()));
        Intent restaurant_intent = new Intent(this, RestaurantPage.class);
        restaurant_intent.putExtra("RESTAURANT_INFO", random_rest);
        startActivity(restaurant_intent);
    }
}
