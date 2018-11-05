package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LeaveReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_review);

        // get dish name from intent -- and set the name
        Intent intent = getIntent();
        String dish_name = intent.getStringExtra("dish");
        TextView dish_title = (TextView) findViewById(R.id.review_dish_name);
        dish_title.setText(dish_name);
        
    }

    public void goBackDishPage(View view) {
        // get the dish name
        TextView dish_name = (TextView)findViewById(R.id.review_dish_name);
        String dish = dish_name.getText().toString();

        Intent dish_page_intent = new Intent(this, DishPage.class);
        dish_page_intent.putExtra("dishName", dish);
        startActivity(dish_page_intent);
    }
}