package com.greiner_co.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyListTextView;
    private BookAdapter mAdapter;
    private SearchView mSearchView;
    private View mLoadingIndicator;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setQueryHint(getString(R.string.query_hint));

        ListView bookListView = (ListView) findViewById(R.id.list);

        // Find empty list TextView
        mEmptyListTextView = (TextView) findViewById(R.id.empty_view_message);
        bookListView.setEmptyView(mEmptyListTextView);

        //noinspection Convert2Diamond
        final List<Book> books = new ArrayList<Book>();
        mAdapter = new BookAdapter(this, books);
        
        bookListView.setAdapter(mAdapter);

        mLoadingIndicator = findViewById(R.id.loading_spinner);

        // Check for network state
        if (internetIsConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.d(LOG_TAG, "initLoader is called ...");
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Hide the progressBar spinner after loading
            mLoadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display
            mEmptyListTextView.setText(R.string.no_internet_connection_text);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(internetIsConnected()) {
                    // Hide the progressBar spinner after loading
                    mLoadingIndicator.setVisibility(View.VISIBLE);

                    getLoaderManager().restartLoader(0, null, BookActivity.this);
                    return false;
                } else {
                    // get rid of items of previous query
                    mAdapter.clear();
                    // Hide the progressBar spinner after loading
                    mLoadingIndicator.setVisibility(View.GONE);

                    // Set empty state text to display
                    mEmptyListTextView.setText(R.string.no_internet_connection_text);
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Do nothing
                return false;
            }


        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getLoaderManager().restartLoader(0, null, BookActivity.this);
                return false;
            }
        });

        //Set an OnClickListener on every item of the ListView
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Create a Uri with the link of the current Book Object
                Uri link = Uri.parse(books.get(position).getmBookUrl());

                //Create an Intent with the link
                Intent webIntent = new Intent(Intent.ACTION_VIEW, link);

                //If there is an App to handle the Intent, start it
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });
        
    }

    // Save the state of the query during screen reconfigurations (portrait/landscape)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mQuery = mSearchView.getQuery().toString();
        outState.putString("query", mQuery);
        super.onSaveInstanceState(outState);
    }

    // Get the saved state and redo the query
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mQuery = savedInstanceState.getString("query");
        mSearchView.setQuery(mQuery, true);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader is called...");

        mQuery = mSearchView.getQuery().toString();
        return new BookLoader(BookActivity.this, mQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.d(LOG_TAG, "onLoadFinished is called...");

        // Hide the progressBar spinner after loading
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display
        mEmptyListTextView.setText(R.string.empty_list_text);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.d(LOG_TAG, "onLoaderReset is called...");

        mAdapter.clear();
    }

    private boolean internetIsConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
