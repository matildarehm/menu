package com.example.menuui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuApp extends Application {

    // member variables

    // temp: preset dish reviews
    private List<Review> preset_reviews;
    private List<Review> octopus_reviews;
    private List<Review> beef_stew_reviews;

    // store current user
    private String username;

    // favorite restaurants
    private HashMap<String, List<String>> favorite_restaurants;

    public void setReviews() {
        preset_reviews = new ArrayList<>();
        preset_reviews.add(new Review("This dish was excellent", 5, true, "User1"));
        preset_reviews.add(new Review("This dish was delicious", 4, true, "User2"));
        preset_reviews.add(new Review("This dish was mediocre", 3, true, "User3"));

        octopus_reviews = new ArrayList<>();
        octopus_reviews.add(new Review("The octopus was cooked perfectly and it was delicious", 5, true, "Sally"));
        octopus_reviews.add(new Review("This dish was fantastic! I really enjoyed it", 5, true, "Karen"));
        octopus_reviews.add(new Review("This is my favorite dish to get at this restaurant", 5, true, "Ronald"));
        octopus_reviews.add(new Review("I love seafood, so I enjoyed this very much", 5, false, "Dylan"));

        beef_stew_reviews = new ArrayList<>();
        beef_stew_reviews.add(new Review("The noodles are very chewy and the soup is very aromatic", 5, true, "Jenna"));
        beef_stew_reviews.add(new Review("The beef was cooked just right", 4, true, "Joshua"));
        beef_stew_reviews.add(new Review("I've had better noodle soups elsewhere", (float)3.5, false, "Anna"));
    }

    List<Review> getReviews(String dish) {
        if ((dish.toLowerCase().trim()).equals("octopus")) {
            return octopus_reviews;
        }
        else if ((dish.toLowerCase().trim()).equals("beef stew noodle soup")) {
            return beef_stew_reviews;
        }
        else {
            return preset_reviews;
        }
    }

    public void addReview(String dish, Review review) {
        if ((dish.toLowerCase().trim()).equals("octopus")) {
            octopus_reviews.add(review);
        }
        else if ((dish.toLowerCase().trim()).equals("beef stew noodle soup")) {
            beef_stew_reviews.add(review);
        }
    }

    public void setCurrentUser(String username) {
        this.username = username;
    }

    public String getCurrentUser() {
        return username;
    }

    public List<String> getUserFavorites(String user) {
        List<String> favorites = favorite_restaurants.get(user);
        if (favorites == null) {
            // user has no favorites
            List<String> empty_list = new ArrayList<String>();
            return empty_list;
        }
        else {
            return favorites;
        }
    }

    public void addFavoriteRestaurant(String user, String restaurant_info) {
        // get the restaurants list
        if (favorite_restaurants.get(user) == null) {
            // user has no existing favorites, add directly
            List<String> restaurants = new ArrayList<String>();
            // add this new restaurant to the list
            restaurants.add(restaurant_info);
            // edit the hashmap entry or add a new entry if none exists
            favorite_restaurants.put(user, restaurants);
        }
        else {
            // user already exists in the hashmap
            // get the user's favorite restaurants list
            List<String> restaurants = favorite_restaurants.get(user);
            // add this new restaurant to the list
            restaurants.add(restaurant_info);
            // edit the hashmap entry or add a new entry if none exists
            favorite_restaurants.put(user, restaurants);
        }
    }

    // save favorite restaurants hashmap to shared preferences
    public void saveHashMap() {
        HashMap<String, List<String>> map = favorite_restaurants;
        SharedPreferences shared_prefs = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_prefs.edit();
        Gson gson = new Gson();
        String hashmap_string = gson.toJson(map);
        editor.putString("favoriteRestaurants", hashmap_string);
        editor.apply();
    }

    // get favorite restaurants hashmap from shared preferences
    public void getHashMap() {
        System.out.println("get hash map");
        SharedPreferences shared_prefs = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String hashmap_string = shared_prefs.getString("favoriteRestaurants","");
        if (hashmap_string == "") {
            System.out.println("hashmap_string is null");
            // save initial favorite restaurants hashmap to shared preferences
            HashMap<String, List<String>> empty_favorites_map = new HashMap<String, List<String>>();
            //convert to string using gson
            String hashMapString = gson.toJson(empty_favorites_map);
            //save in shared prefs
            shared_prefs.edit().putString("favoriteRestaurants", hashMapString).apply();
            // get from shared prefs
            String storedHashMapString = shared_prefs.getString("favoriteRestaurants", "");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
            HashMap<String, List<String>> favorites_map = gson.fromJson(storedHashMapString, type);
            favorite_restaurants = favorites_map;
        }
        else {
            System.out.println("hashmap string is not null");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
            HashMap<String, List<String>> favorites_map = gson.fromJson(hashmap_string, type);
            favorite_restaurants =  favorites_map;
        }
    }

}
