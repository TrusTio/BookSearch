package com.example.booksearch;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Book implements Serializable {
    private String imgSrc;
    private String name;
    private List<String> authors;
    private String price;
    private List<String> genres;
    private String isbn13;
    private String isbn10;
    private boolean maturity;
    private String pages;
    private String webLink;
    private String description;

    public Book(String imgSrc, String name, List<String> authors,
                String price, List<String> genres, String isbn13, String isbn10,
                boolean maturity, String pages, String webLink, String description) {
        this.imgSrc = imgSrc;
        this.name = name;
        this.authors = authors;
        this.price = price;
        this.genres = genres;
        this.isbn13 = isbn13;
        this.isbn10 = isbn10;
        this.maturity = maturity;
        this.pages = pages;
        this.webLink = webLink;
        this.description = description;

    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getName() {
        return name;
    }

    public String getAuthors() {
        String authors = "";
        Iterator iterator = this.authors.iterator();
        while (iterator.hasNext()) {
            authors += iterator.next();
            if (iterator.hasNext()) {
                authors += ", ";
            }
        }
        return authors;
    }

    public String getFirstAuthor() {
        return authors.get(0);
    }

    public String getPrice() {
        return price;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getMaturityToString() {
        if (maturity) {
            return "Mature";
        }
        return "Not Mature";
    }

    public String getWebLink() {
        return webLink;
    }

    public String getDescription() {
        return description;
    }

    public String getPages() {
        return pages;
    }

    public String getGenres() {
        String genres = "";
        Iterator iterator = this.genres.iterator();
        while (iterator.hasNext()) {
            genres += iterator.next();
            if (iterator.hasNext()) {
                genres += ", ";
            }
        }
        return genres;
    }
}
