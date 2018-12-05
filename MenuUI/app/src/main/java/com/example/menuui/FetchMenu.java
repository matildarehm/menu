package com.example.menuui;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class FetchMenu {

    private RequestQueue queue;
    private Context restaurant_page;
    String restaurant_name;
    private URL rest_url;
    private HttpURLConnection restConnection;
    BufferedReader rest_buffer;
    Boolean completed_task = false;
    List<Dish> dishes = new ArrayList<>();
    String url;
    String API_KEY;
    private JSONObject restaurant_info;

    public String getRestaurantQuery(String rest_name) {
        String rest_query = rest_name.replaceAll("\\s+","%20");
        Log.i("no", rest_query);
        return rest_query;
    }

    public FetchMenu(Context context, String rest_name) {
        restaurant_name = rest_name;
        restaurant_page = context;
        Log.i("here", "im in fetch");

        queue = Volley.newRequestQueue(context);
        url = "https://developers.zomato.com/api/v2.1/search?entity_id=128782&entity_type=subzone&q=" + getRestaurantQuery(restaurant_name) + "&count=1";
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

        catch (IOException | JSONException  e) { e.printStackTrace(); }
        return key;
    }

    public void get_request() {
        Log.i("here", "in request");
        try { rest_url = new URL(url);
              restConnection = (HttpURLConnection) rest_url.openConnection();
              restConnection.setRequestProperty("Accept", " application/json");
              API_KEY = getAPIKey();
              restConnection.setRequestProperty("user-key", " " + API_KEY);
              restConnection.connect();


            if (200 <= restConnection.getResponseCode() && restConnection.getResponseCode() <= 299) {
                rest_buffer = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));
            }
            else {
                rest_buffer = new BufferedReader(new InputStreamReader(restConnection.getErrorStream()));
            }

            String message = org.apache.commons.io.IOUtils.toString(rest_buffer);
            Log.i("response", message);

            JSONObject response = new JSONObject(message);
            JSONArray rest_arr = (JSONArray) response.get("restaurants");
            for (int i = 0; i < rest_arr.length(); i++) {
                JSONObject base_obj = rest_arr.getJSONObject(i).getJSONObject("restaurant");
                String id = base_obj.getJSONObject("R").getString("res_id");
                final String menu = base_obj.getString("menu_url");


                Log.i("RESTAURANT_ID", id);
                Log.i("MENU_URL", menu);

                get_html(menu);
            }

        }
        catch (IOException | JSONException e) { e.printStackTrace(); }
    }

    public List<Dish>  get_all_dishes() { return dishes; }

    public void get_html(String menu) {
        Log.i("done", "here");
        try { new RetrieveHTMLText().execute(menu).get(); }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    class RetrieveHTMLText extends AsyncTask<String, Void, String> {

        String html_text;

        protected String doInBackground(String... strings) {
            try {
                final String menu_url = strings[0];

                URL oracle = new URL(menu_url);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String line;
                StringBuffer response = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                html_text = response.toString();

                Log.i("HTML EXTRACTED", "read");
                get_dishes(html_text);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return html_text;

        }

        protected void onPostExecute(String html) {
            super.onPostExecute(html);


        }

    }

    public void get_dishes(String html_text){
        Log.i("c", "completed");

        Document doc = Jsoup.parse(html_text);
//                ArrayList<String> html_text = new ArrayList<String>();

        Elements dish_items = doc.select("div.tmi-name.ft16.mt10.mb5");
        Elements dish_description = doc.select("div.tmi-desc-text.pt5.pb5");

        Iterator<Element> it1 = dish_items.iterator();
        Iterator<Element> it2 = dish_description.iterator();
        Iterator<Element> it3 = dish_description.iterator();

        Element current = null;
        Boolean prev_found = true;
        int count = 0;

        while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
            Element dish_item = it1.next();
            String dish_title = dish_item.text();
            String dish_extract = dish_title.replaceAll("(\\$)?[0-9]+\\.*[0-9]*", "");
            dish_extract = dish_extract.replaceAll("\\(", "").replaceAll("\\)","");

            String dish_info;
            Log.i("c", "completed");

            if (count == 0) {
                dish_info = it2.next().text();
            } else {
                dish_info = current.text();
            }

            if (dish_extract.contains(dish_info)) {
                if (count == 0) {
                    current = it3.next();
                }
                dish_extract = dish_extract.replace(current.text(), " ");

                count += 1;
                current = it3.next();
            } else {
                dish_info = "";
                prev_found = false;
                current = it3.next();
                count += 1;
            }

            System.out.println(restaurant_name);
            System.out.println(dish_extract);
            System.out.println(dish_info);


            int rating = get_rating(3, 5);
            float rec = get_rec(20, 100);


            dishes.add(new Dish(dish_extract, dish_info, rating, rec, R.drawable.menuyellow, restaurant_name));

        }

    }

    public int get_rating(int min, int max) {
        Random rand = new Random();

        int result = rand.nextInt((max - min) + 1) + min;

        return result;

    }

    public float get_rec(float min, float max) {
        Random rand = new Random();

        float result = rand.nextFloat() * (max - min) + min;

        return result;

    }

}




