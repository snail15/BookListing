package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    public static String authorName;
    public static String titleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected){
            showToast();
        }




        TextView searchView = (TextView) findViewById(R.id.search_click);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText authorEditText = (EditText) findViewById(R.id.author_edit);
                try {
                    authorName = URLEncoder.encode(authorEditText.getText().toString(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                EditText titleEditText = (EditText) findViewById(R.id.title_edit);
                try {
                    titleName = URLEncoder.encode(titleEditText.getText().toString(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Intent searchPage = new Intent(MainActivity.this, BookListingActivity.class);
                startActivity(searchPage);

            }
        });
    }
    public static String getAuthorName(){
        return authorName;
    }

    public static String getTitleName(){
        return titleName;
    }

    private void showToast(){
        Toast.makeText(MainActivity.this, "Oops, do you have internet??!",Toast.LENGTH_SHORT).show();
    }
}
