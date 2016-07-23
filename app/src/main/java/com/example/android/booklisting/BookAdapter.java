package com.example.android.booklisting;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by OWNER on 7/23/2016.
 */
public class BookAdapter extends ArrayAdapter<Book>{

    public BookAdapter (Activity context, ArrayList<Book> books){
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_list,parent,false);
        }

        Book currentBook = getItem(position);

        TextView ratingView = (TextView) listView.findViewById(R.id.rating);
        ratingView.setText(String.valueOf(currentBook.getRating()));
        GradientDrawable magnitudeCircle = (GradientDrawable) ratingView.getBackground();

       int ratingColor = getMagnitudeColor(currentBook.getRating());
       magnitudeCircle.setColor(ratingColor);

        TextView titleView = (TextView) listView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle());

        TextView authorView = (TextView) listView.findViewById(R.id.author);
        authorView.setText(currentBook.getAuthor());
       return listView;
    }
    private int getMagnitudeColor(double rating) {
        int ratingID = 0;
        int ratingFloor = (int) Math.floor(rating);
        switch (ratingFloor) {
            case 0:
            case 1:
                ratingID = R.color.rating1;
                break;
            case 2:
                ratingID = R.color.rating2;
                break;
            case 3:
                ratingID = R.color.rating3;
                break;
            case 4:
                ratingID = R.color.rating4;
                break;
            case 5:
                ratingID = R.color.rating5;
                break;

        }
        return ContextCompat.getColor(getContext(), ratingID);
    }
}
