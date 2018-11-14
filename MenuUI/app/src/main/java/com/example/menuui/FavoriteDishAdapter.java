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

// Recycler View Adapter for Dishes on the Restaurant page
public class FavoriteDishAdapter extends RecyclerView.Adapter<FavoriteDishAdapter.FavoriteDishViewHolder> {

    public static class FavoriteDishViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView dish_name;
        TextView dish_restaurant_name;
        ImageView dish_photo;

        FavoriteDishViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.favorite_dish_cardv);
            dish_name = (TextView)itemView.findViewById(R.id.favorite_dish_name);
            dish_restaurant_name = (TextView)itemView.findViewById(R.id.favorite_dish_restaurant);
            dish_photo = (ImageView)itemView.findViewById(R.id.favorite_dish_image);
        }

    }

    List<Dish> dishes;
    FavoriteDishAdapter(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public FavoriteDishViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorite_dish_card, viewGroup, false);
        FavoriteDishViewHolder fdvh = new FavoriteDishViewHolder(v);
        return fdvh;
    }

    @Override
    public void onBindViewHolder(final FavoriteDishViewHolder favoriteDishViewHolder, int i) {
        favoriteDishViewHolder.dish_name.setText(dishes.get(i).name);
        favoriteDishViewHolder.dish_restaurant_name.setText(dishes.get(i).restaurant);
        favoriteDishViewHolder.dish_photo.setImageResource(dishes.get(i).dish_img);

//        favoriteDishViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
