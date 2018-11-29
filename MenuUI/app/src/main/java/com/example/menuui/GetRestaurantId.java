package com.example.menuui;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.http.HttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetRestaurantId extends AsyncTask<String, String, String> {
    String name;
    String address;
    String info;

    public GetRestaurantId(String rName, String rAdd, String rInfo) {
        name = rName;
        address = rAdd;
        info = rInfo;
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            String query = "SELECT * FROM RESTAURANTS WHERE name = " + name + " AND address = " + address + ";";
            String link = "https://menuapp.000webhost.com/DBExtract.php?query=" + query;

            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = br.readLine()) != null) {
                sb.append(json + "\n");
            }
            Log.d("UPDATE", "RES = " + sb.toString());
            return sb.toString().trim();
        } catch (Exception e) {
            Log.d("DEBUG", "ERROR Accessing database: " + e.toString());
            return null;
        }
    }
};

//public class DBHandler {
//    private String query;
//    private Integer op;
//    /**
//     * Constructor for database handler. Constructs query to be sent in asynchronous task.
//     * @param extract is a boolean that is true if we are pulling and false when inserting data.
//     * @param flag refers to whether this will be used in saving restaurant data (1),
//     *                  menu item data (2) or review data (3)
//     * @param restaurant_name is the name of the restaurant name we are pulling from
//     * @param restaurant_address is the address of the restaurant we are comparing to
//     */
//    public DBHandler(Boolean extract, Integer flag, String restaurant_name, String restaurant_address) {
//        if (extract) {
//            query = "SELECT * FROM RESTAURANTS WHERE name=" + restaurant_name + " and address=" + restaurant_address + ";";
//        } else {
//            query = "INSERT INTO RESTAURANTS (name, address) VALUES (" + restaurant_name + ", " + restaurant_address + ");";
//        }
//
//        op = flag;
//    }
//
//
//}
