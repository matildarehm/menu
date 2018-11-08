package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class LeaveReview extends AppCompatActivity {
    private Button post_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_review);

        // get dish name from intent -- and set the name
        Intent intent = getIntent();
        String dish_name = intent.getStringExtra("dish");
        System.out.println(dish_name);
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
        Intent verify_intent = new Intent(this, DishPage.class);
        startActivity(verify_intent);
    }

}
