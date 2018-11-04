package com.example.menuui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView review_text;
        TextView review_recommend;
        TextView review_rating;
        TextView review_author;

        ReviewViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.review_card);
            review_text = (TextView)itemView.findViewById(R.id.review_text);
            review_recommend = (TextView)itemView.findViewById(R.id.review_recommend);
            review_rating = (TextView)itemView.findViewById(R.id.dish_rating_score);
            review_author = (TextView)itemView.findViewById(R.id.review_author);
        }
    }

    List<Review> reviews;
    ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_card, viewGroup, false);
        ReviewViewHolder rvh = new ReviewViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int i) {
        reviewViewHolder.review_text.setText(reviews.get(i).review);
        reviewViewHolder.review_recommend.setText(reviews.get(i).would_recommend);
        reviewViewHolder.review_rating.setText(Integer.toString(reviews.get(i).hearts));
        reviewViewHolder.review_author.setText(reviews.get(i).author);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

