package com.example.android.booklisting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static String authorName;
    public static String titleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView searchView = (TextView) findViewById(R.id.search_click);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText authorEditText = (EditText) findViewById(R.id.author_edit);
                authorName = authorEditText.getText().toString();

                EditText titleEditText = (EditText) findViewById(R.id.title_edit);
                titleName = titleEditText.getText().toString();

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
}
