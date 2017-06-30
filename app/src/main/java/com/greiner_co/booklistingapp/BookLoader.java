package com.greiner_co.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * AsyncTaskLoader to handle background fetch of JSON data from Google Books API URL
 * Created by Jens Greiner on 29.06.17.
 */

class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();

    private final String mQuery;

    public BookLoader(Context context, String query) {
        super(context);
        this.mQuery = query;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "onStartLoading is called...");

        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        Log.d(LOG_TAG, "loadInBackground is called...");

        if(TextUtils.isEmpty(mQuery)) {
            return null;
        }

        List<Book> books = QueryUtils.fetchBookData(mQuery);
        return books;
    }
}
