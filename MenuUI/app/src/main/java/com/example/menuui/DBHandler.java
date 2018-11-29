package com.example.menuui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public void populateMenuItems(Integer rId) {
        db = this.getWritableDatabase();
        db.execSQL(MI_TABLE_CREATE);
        ContentValues v = new ContentValues();
        v.put(MI_COLUMN_RID, rId);
        v.put(MI_COLUMN_DISH_NAME, "Mortadella");
        v.put(MI_COLUMN_DESC, "Olive salsa, chile oil");
        v.put(MI_COLUMN_RATING, 4.2);
        v.put(MI_COLUMN_RECOMMEND, 85);

        db.insert(MI_TABLE_NAME, null, v);

        ContentValues v2 = new ContentValues();
        v2.put(MI_COLUMN_RID, rId);
        v2.put(MI_COLUMN_DISH_NAME, "Grilled Cheese");
        v2.put(MI_COLUMN_DESC, "House-made bread & american cheese");
        v2.put(MI_COLUMN_RATING, 4.6);
        v2.put(MI_COLUMN_RECOMMEND, 95);

        db.insert(MI_TABLE_NAME, null, v2);

        ContentValues v3 = new ContentValues();
        v3.put(MI_COLUMN_RID, rId);
        v3.put(MI_COLUMN_DISH_NAME, "Gnocci");
        v3.put(MI_COLUMN_DESC, "with cheese & butter");
        v3.put(MI_COLUMN_RATING, 3.2);
        v3.put(MI_COLUMN_RECOMMEND, 65);

        db.insert(MI_TABLE_NAME, null, v3);

        ContentValues v4 = new ContentValues();
        v4.put(MI_COLUMN_RID, rId);
        v4.put(MI_COLUMN_DISH_NAME, "Duck Breast");
        v4.put(MI_COLUMN_DESC, "squash caponata, black garlic");
        v4.put(MI_COLUMN_RATING, 5);
        v4.put(MI_COLUMN_RECOMMEND, 100);
        db.insert(MI_TABLE_NAME, null, v4);
        db.close();
    }
}
