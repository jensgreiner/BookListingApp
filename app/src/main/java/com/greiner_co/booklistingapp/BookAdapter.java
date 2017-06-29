package com.greiner_co.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Provides a view for the books listview
 * Created by Jens Greiner on 29.06.17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private final Context mContext;

    public BookAdapter(@NonNull Context context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.book_list_item, parent, false);

            holder = new ViewHolder();
            holder.bookTitle = (TextView) convertView.findViewById(R.id.book_title);
            holder.bookAuthor = (TextView) convertView.findViewById(R.id.book_author);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book currentBook = getItem(position);

        if(currentBook != null) {
            holder.bookTitle.setText(currentBook.getmBookTitle());
            holder.bookAuthor.setText(currentBook.getmBookAuthor());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView bookTitle;
        TextView bookAuthor;
    }
}
