package com.greiner_co.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Helper methods related to requesting and receiving book data from Google Books api
 * Created by Jens Greiner on 29.06.17.
 */

@SuppressWarnings("WeakerAccess")
public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private QueryUtils() {
        // intentionally left blank - no one could create an object from it
    }

    public static List<Book> fetchBookData(String query) {
        Log.d(LOG_TAG, "fetchBookData is called...");

        URL url = createUrl(query);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException ioe) {
            Log.e(LOG_TAG, "Problem making the HTTP request", ioe);
        }

        //noinspection UnnecessaryLocalVariable
        List<Book> books = extractBooks(jsonResponse);

        return books;
    }

    private static List<Book> extractBooks(String jsonResponse) {
        List<Book> books = new ArrayList<>();

        if(TextUtils.isEmpty(jsonResponse)) {
            Log.d(LOG_TAG, "List of books is empty");
            return null;
        }

        try {

            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(jsonResponse);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of features (or earthquakes).
            JSONArray items = root.getJSONArray("items");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < items.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject book = items.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of volume information
                // for that book.
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                String author;
                if(volumeInfo.has("authors")) {
                    // @link https://stackoverflow.com/a/10147984/1469260
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    String strings[] = new String[authors.length()];
                    for (int j = 0; j < strings.length; j++) {
                        strings[j] = authors.getString(j);
                    }
                    // @link https://stackoverflow.com/a/5283753/1469260
                    StringBuilder builder = new StringBuilder();
                    for (String authorString : strings) {
                        builder.append(authorString);
                        builder.append(", ");
                    }
                    // @link https://stackoverflow.com/a/205712/1469260
                    author = builder.length() > 0 ? builder.substring(0, builder.length() - 2) : "";
                } else {
                    author = "n/a";
                }

                String url = volumeInfo.getString("infoLink");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response and add it to the list of earthquakes
                books.add(new Book(title, author, url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        return books;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Handle redirect (response code 301)
            // @link https://discussions.udacity.com/t/earthquake-app-error-response-code-301/226098/8
            // See also @link http://www.mkyong.com/java/java-httpurlconnection-follow-redirect-example/
            int httpResponse = urlConnection.getResponseCode();
            if (httpResponse == 301) {
                // Get redirect url from "location" header field
                String newUrl = urlConnection.getHeaderField("Location");

                // Open the new connection again
                urlConnection = (HttpsURLConnection) new URL(newUrl).openConnection();

                Log.d(LOG_TAG, "Redirect to URL : " + newUrl);
            }

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    private static URL createUrl(String query) {
        URL url = null;
        try {
            url = new URL(BOOKS_API_URL + query);
            Log.d(LOG_TAG, url.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }
}
