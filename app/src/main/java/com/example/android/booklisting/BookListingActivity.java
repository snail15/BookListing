package com.example.android.booklisting;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookListingActivity extends AppCompatActivity {
    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String BASE_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final String API_KEY =
            "&keyAIzaSyAZhekbbQJGkMBezf_fQYyrLh5h6gOikdM";

    private static final String TERM_SEPARATOR =
            "+inauthor:";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_listview);

        // Kick off an {@link AsyncTask} to perform the network request
        BookAsyncTask task = new BookAsyncTask();

        task.execute();

    }



    private void updateUi(ArrayList<Book> books){
        if (books == null){
            LinearLayout noResultScreen = (LinearLayout) View.inflate(BookListingActivity.this, R.layout.no_result,null);
            setContentView(noResultScreen);
        }
        else {
            BookAdapter bookAdapter = new BookAdapter(this, books);
            ListView listView = (ListView) findViewById(R.id.book_listview);
            listView.setAdapter(bookAdapter);
        }
    }

    private void showToast(){
        Toast.makeText(BookListingActivity.this, "Oops, something went wrong!",Toast.LENGTH_SHORT).show();
    }

    private class BookAsyncTask extends AsyncTask<URL,Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(BASE_URL+MainActivity.getTitleName()+TERM_SEPARATOR+MainActivity.getAuthorName()+API_KEY);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            try{
                ArrayList<Book> searchedBooks= extractFeatureFromJson(jsonResponse);
                return searchedBooks;
            }
            catch (NullPointerException e){
                Log.e(LOG_TAG, "None searched", e);
            }


            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return null;
        }

        protected void onPostExecute(ArrayList<Book> books) {
//            if (books == null) {
//                return;
//            }

            updateUi(books);
        }


        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);

            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null){
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else{
                    showToast();
                    Log.e(LOG_TAG, ""+urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG,"IOExcepion");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an  object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private ArrayList<Book> extractFeatureFromJson(String bookJSON) {

            ArrayList<Book> books = new ArrayList<Book>();
            if(TextUtils.isEmpty(bookJSON)){
                return null;
            }
            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");


                // If there are results in the features array
                if (itemsArray.length() > 0) {
                    for (int i = 0; i < itemsArray.length(); i++){
                        // Extract out the title, author, and rating
                        JSONObject itemObject = itemsArray.getJSONObject(i);
                        JSONObject volumeInfo = itemObject.getJSONObject("volumeInfo");
                        String title = volumeInfo.getString("title");
                        JSONArray authorArray = volumeInfo.getJSONArray("authors");
                        String author = authorArray.getString(0);
                        double rating = 0.0;
                        try{
                            rating = volumeInfo.getDouble("averageRating");

                        } catch (JSONException e) {
                            rating = 0.0;
                        }
                        Book book = new Book(author,rating,title);

                        books.add(book);

                    }
                    return books;

                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
                return null;
            }
        }
    }
}
