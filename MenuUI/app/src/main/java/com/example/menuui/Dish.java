package com.example.menuui;

// class for the dish objects
class Dish {
    String name;
    String description;
    double avg_rating;
    double avg_recommended;
    int dish_img;

    Dish(String name, String description, double avg_rating, double avg_recommended, int dish_img) {
        this.name = name;
        this.description = description;
        this.avg_rating = avg_rating;
        this.avg_recommended = avg_recommended;
        this.dish_img = dish_img;
    }
}
