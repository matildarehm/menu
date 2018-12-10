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
    List<String> restaurants_info;

    FavoriteRestaurantAdapter(List<Restaurant> restaurants, List<String> restaurants_info) {
        this.restaurants = restaurants;
        this.restaurants_info = restaurants_info;
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
    public void onBindViewHolder(final FavoriteRestaurantViewHolder favoriteRestaurantViewHolder, final int i) {
        favoriteRestaurantViewHolder.restaurant_name.setText(restaurants.get(i).name);
        favoriteRestaurantViewHolder.restaurant_photo.setImageResource(restaurants.get(i).restaurant_img);

        favoriteRestaurantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get restaurant info
                String restaurant_info = restaurants_info.get(restaurants.get(i).id);
                Intent restaurant_intent = new Intent(view.getContext(), RestaurantPage.class);
                restaurant_intent.putExtra("RESTAURANT_INFO", restaurant_info);
                view.getContext().startActivity(restaurant_intent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
