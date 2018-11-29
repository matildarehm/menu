package com.example.menuui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "menu.db";

    // RESTAURANTS TABLE
    private static final String TABLE_NAME = "restaurants";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String TABLE_CREATE = "CREATE TABLE RESTAURANTS (id integer primary key autoincrement not null , " +
            "name text not null, address text not null);";

    // MENU ITEMS
    private static final String MI_TABLE_NAME = "menu_items";
    private static final String MI_COLUMN_ID = "id";
    private static final String MI_COLUMN_RID = "rid";
    private static final String MI_COLUMN_DISH_NAME = "dish_name";
    private static final String MI_COLUMN_DESC = "description";
    private static final String MI_COLUMN_RATING = "avg_rating";
    private static final String MI_COLUMN_RECOMMEND = "avg_reccommend";
    private static final String MI_COLUMN_IMAGE = "image";
    private static final String MI_TABLE_CREATE = "CREATE TABLE MENU_ITEMS (id integer primary key autoincrement not null, " +
            "rid integer not null, dish_name text not null, description text, avg_rating float not null, avg_reccommend float not null, image text);";

    // REVIEW ITEMS
    private static final String RI_TABLE_NAME = "reviews";
    private static final String RI_COLUMN_ID = "id";
    private static final String RI_COLUMN_DID = "did";
    private static final String RI_COLUMN_USERNAME = "username";
    private static final String RI_COLUMN_REVIEW = "review";
    private static final String RI_COLUMN_RATING = "rating";
    private static final String RI_COLUMN_RECOMMEND = "recommend";
    private static final String RI_TABLE_CREATE = "CREATE TABLE REVIEWS (id integer primary key autoincrement not null, " +
            "did integer not null, username text not null, review text, rating int not null, recommend int not null);";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(MI_TABLE_CREATE);
        db.execSQL(RI_TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public Integer findRestaurant(String rName, String rAddress) {
        Log.d("INFO", "STARTING FIND RESTAURANT");
        db = this.getReadableDatabase();
        String query = "SELECT id, name, address FROM RESTAURANTS;";
        Cursor cursor = db.rawQuery(query, null);

        String tName, tAddress;
        Integer tId = -1;

        if (cursor.moveToFirst()) {
            Log.d("INFO", "SOMETHING FOUND");

            do {
                tName = cursor.getString(1);
                tAddress = cursor.getString(2);
                Log.d("INFO", "" + cursor.getInt(0) + " : " + tName + " : " + tAddress);
                if (tName.equals(rName) && tAddress.equals(rAddress)) {
                    Log.d("INFO", "FOUND!");
                    tId = cursor.getInt(0);
                    db.close();
                    return tId;
                }
            } while (cursor.moveToNext());
        } if (tId == -1) {
            tId = insertRestaurant(rName, rAddress);
        }

        db.close();
        Log.d("INFO", "RID : RNAME : RADD =" + tId + " : " + rName + " : " + rAddress);
        return tId;
    }

    public Integer insertRestaurant(String rName, String rAddress) {
        Log.d("INFO", "STARTING INSERT RESTAURANT");
        db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_NAME, rName);
        v.put(COLUMN_ADDRESS, rAddress);

        db.insert(TABLE_NAME, null, v);
        db.close();
        return findRestaurant(rName, rAddress);
    }

    public void getReviews(ArrayList<Review> reviews) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM reviews;";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(2);
                String review = cursor.getString(3);
                Integer rating = cursor.getInt(4);
                Integer rec = cursor.getInt(5);

                Boolean real_rec;
                if (rating > 0) {
                    real_rec = true;
                } else {
                    real_rec = false;
                }

                reviews.add(new Review(review, rating, real_rec, user));
            } while (cursor.moveToNext());
        }

        reviews.add(new Review("I found this dish quite tasty", 5, true, "Matilda"));
        reviews.add(new Review("It's good... I GUESS", 4, true, "datHomie"));
        reviews.add(new Review("To be quite frank I found this dish... shallow and pedantic", 3,  true, "YelpFan16"));
    }

    public void addReview(String review, String user, Integer rating, Boolean recommend) {
        db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        Integer rec;
        if (recommend) {
            rec = 1;
        } else {
            rec = 0;
        }
        v.put(RI_COLUMN_DID, 1);
        v.put(RI_COLUMN_USERNAME, user);
        v.put(RI_COLUMN_REVIEW, review);
        v.put(RI_COLUMN_RATING, rating);
        v.put(RI_COLUMN_RECOMMEND, recommend);

        db.insert(RI_TABLE_NAME, null, v);
        db.close();
    }


}
