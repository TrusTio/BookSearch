package com.example.booksearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookAdapter extends ArrayAdapter<Book> {

    BookAdapter(Context context, ArrayList<Book> bookList) {
        super(context, R.layout.book_list_layout, bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_layout, parent, false);
        }
        final Book currentBook = getItem(position);

        //Load the fields with data from the Book
        ImageView bookImageView = listItemView.findViewById(R.id.book_image);
        Picasso.get().load(currentBook.getImgSrc()).into(bookImageView);

        TextView bookNameTextView = listItemView.findViewById(R.id.book_name);
        bookNameTextView.setText(currentBook.getName());

        TextView bookAuthorTextView = listItemView.findViewById(R.id.book_author);
        bookAuthorTextView.setText(currentBook.getFirstAuthor());

        TextView bookPriceTextView = listItemView.findViewById(R.id.book_price);
        if (currentBook.getPrice().equals("N/A")) {
            bookPriceTextView.setText(currentBook.getPrice());
        } else {
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(new Locale("bg", "BG"));
            bookPriceTextView.setText(defaultFormat.format(Double.parseDouble(currentBook.getPrice())));
        }

        LinearLayout bookItemLayout = listItemView.findViewById(R.id.book_item_layout);
        bookItemLayout.setOnClickListener((view) -> {
            Intent intent = new Intent(getContext(),DetailedBookActivity.class);
            intent.putExtra("CURRENT_BOOK",currentBook);

            getContext().startActivity(intent);
        });
        return listItemView;
    }
}
