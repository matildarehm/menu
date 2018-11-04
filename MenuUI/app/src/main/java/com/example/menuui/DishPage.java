package com.example.menuui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DishPage extends AppCompatActivity {

    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_page);

        // Recycler View
        RecyclerView rv = (RecyclerView) findViewById(R.id.review_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        // get reviews
        initializeReviews();

        // review recycler adapter
        ReviewAdapter adapter = new ReviewAdapter(reviews);
        rv.setAdapter(adapter);


        // get dish name from intent -- and set the name
        Intent intent = getIntent();
        String dish_name = intent.getStringExtra("dishName");
        TextView dish_title = (TextView) findViewById(R.id.dish_title);
        dish_title.setText(dish_name);

    }



    // get/set reviews
    private void initializeReviews(){
        reviews = new ArrayList<>();
        reviews.add(new Review("This dish was excellent", 5, "Yes"));
        reviews.add(new Review("This dish was delicious", 4, "Yes"));
    }

}

