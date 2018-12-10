package com.example.menuui;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FetchReviews {

    String restaurant_name;
    private Context restaurant_page;
    List<Review> reviews = new ArrayList<>();
    String url;
    String API_KEY;
    private JSONObject restaurant_info;
    private URL rest_url;
    private HttpURLConnection restConnection;
    BufferedReader rest_buffer;

    public FetchReviews(Context context, String rest_name) {
        restaurant_name = rest_name;
        restaurant_page = context;

        url = "https://developers.zomato.com/api/v2.1/search?entity_id=128782&entity_type=subzone&q=" + getRestaurantQuery(restaurant_name) + "&count=1";
    }

    public String getRestaurantQuery(String rest_name) {
        String rest_query = rest_name.replaceAll("\\s+","%20");
        Log.i("no", rest_query);
        return rest_query;
    }

    private String getAPIKey() {
        String key = null;
        try {
            InputStream input_json = restaurant_page.getResources().openRawResource(R.raw.zomatoapi);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input_json,"UTF-8"),8);
            StringBuilder json_builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                json_builder.append(line + "\n");
            }
            input_json.close();
            String zomato_json = json_builder.toString();
            JSONObject api_obj = new JSONObject(zomato_json);
            JSONObject api_key = api_obj.getJSONObject("zomatoAPI");
            key = api_key.getString("key");

        }

        catch (IOException | JSONException e) { e.printStackTrace(); }
        return key;
    }

    public void get_request() {
        Log.i("here", "in request");
        try {
            rest_url = new URL(url);
            restConnection = (HttpURLConnection) rest_url.openConnection();
            restConnection.setRequestProperty("Accept", " application/json");
            API_KEY = getAPIKey();
            restConnection.setRequestProperty("user-key", " " + API_KEY);
            restConnection.connect();


            if (200 <= restConnection.getResponseCode() && restConnection.getResponseCode() <= 299) {
                rest_buffer = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));
            } else {
                rest_buffer = new BufferedReader(new InputStreamReader(restConnection.getErrorStream()));
            }

            String message = org.apache.commons.io.IOUtils.toString(rest_buffer);
            Log.i("response", message);
            String res_id = null;

            JSONObject response = new JSONObject(message);
            JSONArray rest_arr = (JSONArray) response.get("restaurants");
            for (int i = 0; i < rest_arr.length(); i++) {
                JSONObject base_obj = rest_arr.getJSONObject(i).getJSONObject("restaurant");
                res_id = base_obj.getJSONObject("R").getString("res_id");
            }

            get_reviews(res_id);

        }
        catch (IOException | JSONException e) { e.printStackTrace(); }

    }

    public void get_reviews(String id) {
        try {

            Log.i("here", "in request");
            rest_url = new URL("https://developers.zomato.com/api/v2.1/reviews?res_id=" + id);
            restConnection = (HttpURLConnection) rest_url.openConnection();
            restConnection.setRequestProperty("Accept", " application/json");
            restConnection.setRequestProperty("user-key", " " + API_KEY);
            restConnection.connect();


            if (200 <= restConnection.getResponseCode() && restConnection.getResponseCode() <= 299) {
                rest_buffer = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));
            } else {
                rest_buffer = new BufferedReader(new InputStreamReader(restConnection.getErrorStream()));
            }

            String message = org.apache.commons.io.IOUtils.toString(rest_buffer);
            Log.i("response", message);

            JSONObject response = new JSONObject(message);
            JSONArray rev_arr = (JSONArray) response.get("user_reviews");
            for (int i = 0; i < rev_arr.length(); i++) {
                JSONObject temp_review = rev_arr.getJSONObject(i).getJSONObject("review");
                System.out.println("working");
                float rating = (float) temp_review.getInt("rating");
                String review_text = temp_review.getString("review_text");
                boolean rec = false;
                if (rating >= (float) 3) { rec = true; }
                JSONObject user_object = temp_review.getJSONObject("user");
                String review_author =  user_object.getString("name");

                System.out.println(rating);
                System.out.println(review_text);
                System.out.println(rec);
                System.out.println(review_author);

                reviews.add(new Review(review_text, rating, rec, review_author));
//
//                JSONArray all_reviews = (JSONArray) base_obj.getJSONArray("all_reviews");
//                for (int j = 0; i < all_reviews.length(); i++) {
//                    JSONObject temp_review = all_reviews.getJSONObject(i);
//                    float rating = (float) temp_review.getInt("rating");
//                    String review_text = temp_review.getString("review_text");
////                    boolean rec = false;
////                    if (rating >= (float) 3) { rec = true; }
////                    JSONObject user_object = temp_review.getJSONObject("user");
////                    String review_author =  user_object.getString("name");
//                    System.out.println(review_text + rating);
//////                    reviews.add(new Review());
////
//                }


                Log.i("RESTAURANT_ID", id);


            }
        }

        catch (IOException | JSONException e) { e.printStackTrace(); }
    }

    public List<Review> get_all_reviews() { return reviews; }
}
