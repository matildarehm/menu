package com.example.menuui;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

// Recycler View Adapter for Dishes on the RestaurantPage page
public class EditDishAdapter extends RecyclerView.Adapter<EditDishAdapter.DishViewHolder> {

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView dish_name;
        TextView dish_rating;
        TextView dish_description;
        TextView dish_recommended;
        ImageView dish_photo;

        DishViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.dish_cardv);
            dish_name = (TextView)itemView.findViewById(R.id.rest_dish_title);
            dish_rating = (TextView)itemView.findViewById(R.id.rest_avg_rating);
            dish_description = (TextView)itemView.findViewById(R.id.rest_dish_description);
            dish_recommended = (TextView)itemView.findViewById(R.id.rest_avg_recommendation);
            dish_photo = (ImageView)itemView.findViewById(R.id.rest_dish_image);
        }

    }

    List<Dish> dishes;
    EditDishAdapter(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_dish_card, viewGroup, false);
        DishViewHolder dvh = new DishViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(final DishViewHolder dishViewHolder, int i) {
        dishViewHolder.dish_name.setText(dishes.get(i).name);
        dishViewHolder.dish_rating.setText(String.format("%.2f", dishes.get(i).avg_rating));
        dishViewHolder.dish_description.setText(dishes.get(i).description);
        dishViewHolder.dish_recommended.setText(String.format("%.2f", dishes.get(i).avg_recommended));
        dishViewHolder.dish_photo.setImageResource(dishes.get(i).dish_img);

        // !!! set on click listener for the delete button !!!
        // remove the dish from the database

//        dishViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView dish_name = view.findViewById(R.id.rest_dish_title);
//                String dish = dish_name.getText().toString();
//
//                Intent dish_reviews_intent = new Intent(view.getContext(), DishPage.class);
//                dish_reviews_intent.putExtra("dishName", dish);
//                view.getContext().startActivity(dish_reviews_intent);
//            }
//        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
