package com.greiner_co.booklistingapp;

/**
 * Data object to hold the data of a single book
 * Created by Jens Greiner on 29.06.17.
 */

public class Book {
    private final String mBookTitle;
    private final String mBookAuthor;
    private final String mBookUrl;

    public Book(String bookTitle, String bookAuthor, String bookUrl) {
        this.mBookTitle = bookTitle;
        this.mBookAuthor = bookAuthor;
        this.mBookUrl = bookUrl;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    public String getmBookUrl() {
        return mBookUrl;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mBookTitle='" + mBookTitle + '\'' +
                ", mBookAuthor='" + mBookAuthor + '\'' +
                ", mBookUrl='" + mBookUrl + '\'' +
                '}';
    }
}
