package com.example.menuui;

class Review {
    String review;
    int rating;
    boolean would_recommend;
    String author;

    Review(String review, int rating, boolean would_recommend, String author) {
        this.review = review;
        this.rating = rating;
        this.would_recommend = would_recommend;
        this.author = author;
    }
}
