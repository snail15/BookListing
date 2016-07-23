package com.example.android.booklisting;

/**
 * Created by OWNER on 7/22/2016.
 */
public class Book {

    private String mAuthor;
    private String mTitle;
    private double mRating;

    public Book(String author, double rating, String title) {
        mAuthor = author;
        mRating = rating;
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public double getRating() {
        return mRating;
    }

    public String getTitle() {
        return mTitle;
    }
}
