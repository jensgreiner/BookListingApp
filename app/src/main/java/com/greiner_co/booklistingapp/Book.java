package com.greiner_co.booklistingapp;

/**
 * Data object to hold the data of a single book
 * Created by Jens Greiner on 29.06.17.
 */

public class Book {
    private final String mBookTitle;
    private final String mBookAuthor;

    public Book(String bookTitle, String bookAuthor) {
        this.mBookTitle = bookTitle;
        this.mBookAuthor = bookAuthor;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mBookTitle='" + mBookTitle + '\'' +
                ", mBookAuthor='" + mBookAuthor + '\'' +
                '}';
    }
}
