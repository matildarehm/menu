package com.example.menuui;

class Review {
    String review;
    int hearts;
    String would_recommend;
    String author;

    Review(String review, int hearts, String would_recommend, String author) {
        this.review = review;
        this.hearts = hearts;
        this.would_recommend = would_recommend;
        this.author = author;
    }
}
