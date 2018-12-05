package com.example.menuui;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MenuApp extends Application {

    // member variables

    // temp: preset dish reviews
    private List<Review> preset_reviews;
    private List<Review> octopus_reviews;
    private List<Review> beef_stew_reviews;

    public void setReviews() {
        preset_reviews = new ArrayList<>();
        preset_reviews.add(new Review("This dish was excellent", 5, true, "User1"));
        preset_reviews.add(new Review("This dish was delicious", 4, true, "User2"));
        preset_reviews.add(new Review("This dish was mediocre", 3, true, "User3"));

        octopus_reviews = new ArrayList<>();
        octopus_reviews.add(new Review("The octopus was cooked perfectly and it was delicious", 5, true, "Sally"));
        octopus_reviews.add(new Review("This dish was fantastic! I really enjoyed it", 5, true, "Karen"));
        octopus_reviews.add(new Review("This is my favorite dish to get at this restaurant", 5, true, "Ronald"));
        octopus_reviews.add(new Review("I love seafood, so I enjoyed this very much", 5, false, "Dylan"));

        beef_stew_reviews = new ArrayList<>();
        beef_stew_reviews.add(new Review("The noodles are very chewy and the soup is very aromatic", 5, true, "Jenna"));
        beef_stew_reviews.add(new Review("The beef was cooked just right", 4, true, "Joshua"));
        beef_stew_reviews.add(new Review("I've had better noodle soups elsewhere", (float)2.5, false, "Anna"));
    }

    List<Review> getReviews(String dish) {
        if ((dish.toLowerCase().trim()).equals("octopus")) {
            return octopus_reviews;
        }
        else if ((dish.toLowerCase().trim()).equals("beef stew noodle soup")) {
            return beef_stew_reviews;
        }
        else {
            return preset_reviews;
        }
    }

    public void addReview(String dish, Review review) {
        if ((dish.toLowerCase().trim()).equals("octopus")) {
            octopus_reviews.add(review);
        }
        else if ((dish.toLowerCase().trim()).equals("beef stew noodle soup")) {
            beef_stew_reviews.add(review);
        }
    }

}
