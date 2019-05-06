package com.example.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {
    private static String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        return QueryUtils.fetchBooksData(url);
    }
}
