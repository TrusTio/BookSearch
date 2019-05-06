package com.example.booksearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailedBookActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_book);

        Intent intent = getIntent();
        Book currentBook = (Book) intent.getSerializableExtra("CURRENT_BOOK");
        updateUi(currentBook);
    }

    /**
     * Updates the UI and it's fields with the given
     * @param currentBook
     */
    private void updateUi(Book currentBook) {
        ImageView bookImage = findViewById(R.id.detailed_book_image);
        Picasso.get().load(currentBook.getImgSrc()).into(bookImage);

        TextView bookNameTV = findViewById(R.id.detailed_book_name);
        bookNameTV.setText(currentBook.getName());

        TextView bookAuthorTV = findViewById(R.id.detailed_book_author);
        bookAuthorTV.setText(currentBook.getAuthors());

        TextView bookPriceTV = findViewById(R.id.detailed_book_price);
        if (currentBook.getPrice().equals("N/A")) {
            bookPriceTV.setText(currentBook.getPrice());
        } else {
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(new Locale("bg", "BG"));
            bookPriceTV.setText(defaultFormat.format(Double.parseDouble(currentBook.getPrice())));
        }

        TextView bookGenresTV = findViewById(R.id.detailed_book_genres);
        bookGenresTV.setText(currentBook.getGenres());

        TextView bookPagesTV = findViewById(R.id.detailed_book_pages);
        bookPagesTV.setText(currentBook.getPages());

        TextView bookIsbn13 = findViewById(R.id.detailed_book_isbn_13);
        bookIsbn13.setText(currentBook.getIsbn13());

        TextView bookIsbn10 = findViewById(R.id.detailed_book_isbn_10);
        bookIsbn10.setText(currentBook.getIsbn10());

        TextView bookMaturityTV = findViewById(R.id.detailed_book_maturity);
        bookMaturityTV.setText(currentBook.getMaturityToString());

        Button webButton = findViewById(R.id.detailed_book_web_link);
        webButton.setOnClickListener((View) -> {
            if(!TextUtils.isEmpty(currentBook.getWebLink())) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(currentBook.getWebLink()));
                startActivity(browserIntent);
            }
        });

        TextView bookDescription = findViewById(R.id.detailed_book_description);
        bookDescription.setText(currentBook.getDescription());

    }
}
