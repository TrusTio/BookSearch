package com.example.booksearch;

import android.app.LoaderManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Loader;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        findViewById(R.id.searchButton).setOnClickListener((View)->{
            EditText searchText = findViewById(R.id.searchBar);
            if(!TextUtils.isEmpty(searchText.getText())) {
                getLoaderManager().restartLoader(1, null, this);
            }
        });
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }




    private void updateUi(ArrayList<Book> bookList) {
        ListView bookListView = findViewById(R.id.book_list);
        bookListView.setAdapter(new BookAdapter(this, bookList));
    }


    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(BOOK_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        EditText searchText = findViewById(R.id.searchBar);
        uriBuilder.appendQueryParameter("q",searchText.getText().toString());
        uriBuilder.appendQueryParameter("maxResults","40");
        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        updateUi(books);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        loader.reset();
    }

}
