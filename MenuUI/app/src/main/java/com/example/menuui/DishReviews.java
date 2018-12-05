package com.example.menuui;

import java.util.ArrayList;
import java.util.List;

public class DishReviews {

    // temp: preset dish reviews
    private List<Review> preset_reviews;
    private List<Review> octopus_reviews;

    public DishReviews() {
        setReviews();
    }

    private void setReviews() {
        preset_reviews = new ArrayList<>();
        preset_reviews.add(new Review("This dish was excellent", 5, true, "User1"));
        preset_reviews.add(new Review("This dish was delicious", 4, true, "User2"));
        preset_reviews.add(new Review("This dish was mediocre", 3,  true, "User3"));

        octopus_reviews = new ArrayList<>();
        octopus_reviews.add(new Review("The octopus was cooked perfectly and it was delicious", 5, true, "Sally"));
        octopus_reviews.add(new Review("This dish was fantastic! I really enjoyed it", 4, true, "Karen"));
        octopus_reviews.add(new Review("This is my favorite dish to get at this restaurant", 5, true, "Ronald"));
        octopus_reviews.add(new Review("I've had better octopus at other restaurants", 3, false, "Dylan"));
    }

    List<Review> getReviews(String dish) {
        if ((dish.toLowerCase().trim()).equals("octopus")) {
            return octopus_reviews;
        }
        else {
            return preset_reviews;
        }
    }

    public void addReview(String dish) {

    }
}
