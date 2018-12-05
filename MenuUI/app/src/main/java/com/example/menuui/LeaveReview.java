package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class LeaveReview extends AppCompatActivity {
    private Button post_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_review);

        // get dish name from intent -- and set the name
        Intent intent = getIntent();
        String dish_name = intent.getStringExtra("dish");
        TextView dish_title = (TextView) findViewById(R.id.review_dish_name);
        dish_title.setText(dish_name);

        post_review = (Button) findViewById(R.id.submit_review);
        post_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_review();

            }
        });

    }

    private void add_review() {
        // get the dish name
        String dish;
        TextView dish_name = (TextView)findViewById(R.id.review_dish_name);
        dish = dish_name.getText().toString();
        // System.out.println(dish);

        // get the contents of the review form
        boolean recommend;
        Switch rec_switch = (Switch)findViewById(R.id.recommend_switch);
        recommend = rec_switch.isChecked();
        // System.out.println(recommend);

        float review_rating;
        RatingBar rate_bar = (RatingBar)findViewById(R.id.rating_bar);
        review_rating = rate_bar.getRating();
        // System.out.println(review_rating);

        String review_text;
        EditText review_edit = (EditText)findViewById(R.id.review_edit_text);
        review_text = review_edit.getText().toString();

        // create review object to add
        Review review = new Review(review_text, review_rating, recommend, "Alexis");
        // add review
        ((MenuApp) this.getApplication()).addReview(dish, review);

        // System.out.println(review_text);

        // Add the review to the database
        // Connect to the database
//        String url = "menuappdb.cctlbaybdt7x.us-east-2.rds.amazonaws.com";
//        String user = "menuadmin";
//        String password = "menuappadmin4";
//
//        try {
//            // Get a connection to the database
//            Connection conn = DriverManager.getConnection(url, user, password);
//            // Create a statement
//            Statement stmt = conn.createStatement();
//            // Execute SQL query
//            String sql = "insert into reviews "
//                    + "(rid, did, username, reviewtext, recommend, rating"
//                    + " values ( , , 'User', review_text, recommend, rating)";
//            stmt.executeUpdate(sql);
//        }
//        catch (Exception exc) {
//            exc.printStackTrace();
//        }

        // return to the dish page
        Intent verify_intent = new Intent(this, DishPage.class);
        verify_intent.putExtra("dishName", dish);
        startActivity(verify_intent);
    }

}
