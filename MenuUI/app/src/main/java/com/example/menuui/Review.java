package com.example.menuui;

class Review {
    String review;
    float rating;
    boolean would_recommend;
    String author;

    Review(String review, float rating, boolean would_recommend, String author) {
        this.review = review;
        this.rating = rating;
        this.would_recommend = would_recommend;
        this.author = author;
    }
}
