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

// Recycler View Adapter for Restaurants on the Favorite Restaurants page
public class FavoriteRestaurantAdapter extends RecyclerView.Adapter<FavoriteRestaurantAdapter.FavoriteRestaurantViewHolder> {

    public static class FavoriteRestaurantViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView restaurant_name;
        ImageView restaurant_photo;

        FavoriteRestaurantViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.restaurant_cardv);
            restaurant_name = (TextView)itemView.findViewById(R.id.restaurant_name);
            restaurant_photo = (ImageView)itemView.findViewById(R.id.restaurant_image);
        }

    }

    List<Restaurant> restaurants;
    FavoriteRestaurantAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public FavoriteRestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_card, viewGroup, false);
        FavoriteRestaurantViewHolder frvh = new FavoriteRestaurantViewHolder(v);
        return frvh;
    }

    @Override
    public void onBindViewHolder(final FavoriteRestaurantViewHolder favoriteRestaurantViewHolder, int i) {
        favoriteRestaurantViewHolder.restaurant_name.setText(restaurants.get(i).name);
        favoriteRestaurantViewHolder.restaurant_photo.setImageResource(restaurants.get(i).restaurant_img);

//        favoriteRestaurantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
