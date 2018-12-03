package com.example.menuui;

import java.io.Serializable;
import java.util.Collections;

// class for the dish objects
class Dish implements Serializable {
    String name;
    String description;
    double avg_rating;
    double avg_recommended;
    int dish_img;
    String restaurant;

    Dish(String name, String description, double avg_rating, double avg_recommended, int dish_img, String restaurant) {
        this.name = name;
        this.description = description;
        this.avg_rating = avg_rating;
        this.avg_recommended = avg_recommended;
        this.dish_img = dish_img;
        this.restaurant = restaurant;
    }

    public String getDishName() {
        String dish_name = this.name;
        return dish_name;
    }

    public double getDishRating() {
        double dish_rating = this.avg_rating;
        return dish_rating;
    }

}
