package com.example.booksearch;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     *
     * Returns the given Url string as an URL object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
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

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //return empty String if url == null
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*ms*/);
            urlConnection.setConnectTimeout(15000/*ms*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error message code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results", e);
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

    /**
     *
     * Extracts an ArrayList of Book objects from the given JSON string
     */
    private static ArrayList<Book> extractBooks(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        ArrayList<Book> bookList = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(bookJSON);
            JSONArray arrayObject = rootObject.getJSONArray("items");
            for (int i = 0; i < arrayObject.length(); i++) {
                JSONObject currentObject = arrayObject.getJSONObject(i);
                String bookImgSrc = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
                String bookName = "N/A";
                List<String> bookAuthors = new ArrayList<>();
                String bookPrice = "N/A";
                String bookIsbn13 = "N/A";
                String bookIsbn10 = "N/A";
                boolean bookMaturity = false;
                String bookPages = "N/A";
                String bookWebLink = "";
                String bookDescription = "N/A";
                List<String> bookGenres = new ArrayList<>();

                //volumeInfo
                JSONObject bookInfo = currentObject.getJSONObject("volumeInfo"); // Name
                JSONArray isbnInfo = bookInfo.getJSONArray("industryIdentifiers"); // isbn13 at index 0 & isbn10 at index 1
                JSONObject isbn13 = isbnInfo.getJSONObject(0);
                JSONObject isbn10 = isbnInfo.getJSONObject(1);

                //checking if any of the values are missing
                try {
                    JSONObject imageLinks = bookInfo.getJSONObject("imageLinks"); // Thumbnail
                    bookImgSrc = imageLinks.getString("smallThumbnail");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for imageLinks", e);
                }

                try {
                    bookName = bookInfo.getString("title");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for title", e);
                }

                try {
                    JSONArray authorInfo = bookInfo.getJSONArray("authors"); //Author
                    for (int j = 0; j < authorInfo.length(); j++) {
                        bookAuthors.add(authorInfo.getString(j));
                    }
                } catch (JSONException e) {
                    bookAuthors.add("N/A");
                    Log.e(LOG_TAG, "No value for authors", e);
                }

                try {
                    JSONObject bookSaleInfo = currentObject.getJSONObject("saleInfo");
                    bookPrice = bookSaleInfo.getJSONObject("listPrice").getString("amount");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for listPrice", e);
                }

                try {
                    bookIsbn13 = isbn13.getString("identifier");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for identifier(13)", e);
                }

                try {
                    bookIsbn10 = isbn10.getString("identifier");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for identifier(10)", e);
                }

                try {
                    if (!bookInfo.getString("maturityRating").equals("NOT_MATURE")) {
                        bookMaturity = true;
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for listPrice", e);
                }

                try {
                    bookPages = bookInfo.getString("pageCount");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for identifier(10)", e);
                }

                try {
                    bookWebLink = bookInfo.getString("infoLink");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for identifier(10)", e);
                }

                try {
                    bookDescription = bookInfo.getString("description");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "No value for identifier(10)", e);
                }

                try {
                    JSONArray genresInfo = bookInfo.getJSONArray("categories");
                    for (int l = 0; l < genresInfo.length(); l++) {
                        bookGenres.add(genresInfo.getString(l));
                    }
                } catch (JSONException e) {
                    bookGenres.add("N/A");
                    Log.e(LOG_TAG, "No value for identifier(10)", e);
                }

                bookList.add(new Book(
                        bookImgSrc,
                        bookName,
                        bookAuthors,
                        bookPrice,
                        bookGenres,
                        bookIsbn13,
                        bookIsbn10,
                        bookMaturity,
                        bookPages,
                        bookWebLink,
                        bookDescription
                ));


            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return bookList;
    }

    /**
     *
     * fetches an ArrayList of Book objects from the given String url
     */

    public static ArrayList<Book> fetchBooksData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return extractBooks(jsonResponse);
    }
}
