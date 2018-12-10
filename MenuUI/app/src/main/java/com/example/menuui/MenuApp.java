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



    // reviews database (dishname, list of reviews)
    private HashMap<String, List<Review>> reviews;

    // get reviews from shared preferences
    public void getSavedReviews() {
        System.out.println("get saved reviews");
        SharedPreferences shared_prefs = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String hashmap_string = shared_prefs.getString("dishReviews","");
        if (hashmap_string == "") {
            System.out.println("hashmap_string is null");
            // save initial reviews hashmap to shared preferences
            HashMap<String, List<Review>> empty_reviews_map = new HashMap<String, List<Review>>();
            //convert to string using gson
            String hashMapString = gson.toJson(empty_reviews_map);
            //save in shared prefs
            shared_prefs.edit().putString("dishReviews", hashMapString).apply();
            // get from shared prefs
            String storedHashMapString = shared_prefs.getString("dishReviews", "");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<Review>>>(){}.getType();
            HashMap<String, List<Review>> reviews_map = gson.fromJson(storedHashMapString, type);
            reviews = reviews_map;
        }
        else {
            System.out.println("review hashmap string is not null");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<Review>>>(){}.getType();
            HashMap<String, List<Review>> reviews_map = gson.fromJson(hashmap_string, type);
            reviews =  reviews_map;
        }
    }

    // save reviews hashmap to shared preferences
    public void saveReviews() {
        HashMap<String, List<Review>> map = reviews;
        SharedPreferences shared_prefs = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_prefs.edit();
        Gson gson = new Gson();
        String hashmap_string = gson.toJson(map);
        editor.putString("dishReviews", hashmap_string);
        editor.apply();
    }

    // get list of reviews for a dish
    public List<Review> getDishReviews(String dish) {
        List<Review> dish_reviews = reviews.get(dish);
        if (dish_reviews == null) {
            // dish has no reviews
            List<Review> empty_list = new ArrayList<Review>();
            return empty_list;
        }
        else {
            return dish_reviews;
        }
    }

    // add review for a dish
    public void addDishReview(String dish, Review review) {
        // get the list of reviews for this dish
        if (reviews.get(dish) == null) {
            // dish has no reviews, add directly
            List<Review> dish_reviews = new ArrayList<Review>();
            // add this new restaurant to the list
            dish_reviews.add(review);
            // edit the hashmap entry or add a new entry if none exists
            reviews.put(dish, dish_reviews);
            // update the dish avg rating
            updateDishRating(dish, review);
        }
        else {
            // dish already exists in the hashmap
            // get the dish's reviews list
            List<Review> dish_reviews = reviews.get(dish);
            // add this new review to the list
            dish_reviews.add(review);
            // edit the hashmap entry or add a new entry if none exists
            reviews.put(dish, dish_reviews);
            // update the dish avg rating
            updateDishRating(dish, review);
        }
    }


    // dish ratings data
    private HashMap<String, List<Float>> ratings;
    // get dish ratings from shared preferences
    public void getRatings() {
        System.out.println("get dish ratings");
        SharedPreferences shared_prefs = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String hashmap_string = shared_prefs.getString("dishRatings","");
        if (hashmap_string == "") {
            System.out.println("hashmap_string is null");
            // save initial reviews hashmap to shared preferences
            HashMap<String, List<Float>> empty_ratings_map = new HashMap<String, List<Float>>();
            //convert to string using gson
            String hashMapString = gson.toJson(empty_ratings_map);
            //save in shared prefs
            shared_prefs.edit().putString("dishRatings", hashMapString).apply();
            // get from shared prefs
            String storedHashMapString = shared_prefs.getString("dishRatings", "");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<Float>>>(){}.getType();
            HashMap<String, List<Float>> ratings_map = gson.fromJson(storedHashMapString, type);
            ratings = ratings_map;
        }
        else {
            System.out.println("rating hashmap string is not null");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<Float>>>(){}.getType();
            HashMap<String, List<Float>> ratings_map = gson.fromJson(hashmap_string, type);
            ratings =  ratings_map;
        }
    }

    // save reviews hashmap to shared preferences
    public void saveRatings() {
        HashMap<String, List<Float>> map = ratings;
        SharedPreferences shared_prefs = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_prefs.edit();
        Gson gson = new Gson();
        String hashmap_string = gson.toJson(map);
        editor.putString("dishRatings", hashmap_string);
        editor.apply();
    }

    // get rating for a dish
    public Float getDishRating(String dish) {
        Float dish_rating = ratings.get(dish).get(0);
        if (dish_rating == null) {
            // dish has no reviews
            return 0f;
        }
        else {
            return dish_rating;
        }
    }

    // update rating for a dish
    public void updateDishRating(String dish, Review review) {
        // get the rating for this dish
        if (ratings.get(dish) == null) {
            // dish has no existing rating, add directly
            // get the review rating
            float dish_rating = review.rating;
            // create the rating list -- index 0 stores the rating, index 1 stores the count of reviews for this dish
            List<Float> rating_list = new ArrayList<Float>();
            rating_list.add(dish_rating);
            rating_list.add(1f);             // start with count = 1
            // edit the hashmap entry or add a new entry if none exists
            ratings.put(dish, rating_list);
        }
        else {
            // dish already exists in the hashmap
            // get the dish's current rating
            List<Float> rating_list = ratings.get(dish);
            Float dish_rating = rating_list.get(0);
            Float review_count = rating_list.get(1);
            // update the avg rating -- add the new review rating
            Float new_rating = review.rating;
            Float new_avg_rating = ((dish_rating * review_count) + new_rating) / (review_count + 1);
            List<Float> new_rating_list = new ArrayList<Float>();
            new_rating_list.add(new_avg_rating);
            new_rating_list.add(review_count + 1);
            // edit the hashmap entry or add a new entry if none exists
            ratings.put(dish, new_rating_list);
        }
    }


}
