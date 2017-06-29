package com.greiner_co.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();
    private static final String BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=android";
    private TextView mEmptyListTextView;
    private BookAdapter mAdapter;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        ListView bookListView = (ListView) findViewById(R.id.list);

        // Find empty list TextView
        mEmptyListTextView = (TextView) findViewById(R.id.empty_view_message);
        bookListView.setEmptyView(mEmptyListTextView);
        
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        
        bookListView.setAdapter(mAdapter);

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
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display
            mEmptyListTextView.setText(R.string.no_internet_connection_text);
        }
        
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader is called...");

        return new BookLoader(BookActivity.this, BOOKS_API_URL);
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
