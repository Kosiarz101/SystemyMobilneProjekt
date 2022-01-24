package com.example.task9.Models;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task9.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecycleViewHolder> {
    List<BookSearch> books;
    Context context;
    RecycleViewAdapter.onNoteListener theOnNoteListener;

    public RecycleViewAdapter(RecycleViewAdapter.onNoteListener theOnNoteListener, Context context){
        this.theOnNoteListener = theOnNoteListener;
        this.context = context;
    }
    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_list_item, parent, false);
        return new RecycleViewHolder(view, this.theOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        if(books != null)
        {
            BookSearch book = books.get(position);
            book.setNumber(String.valueOf(position + 1));
            holder.bind(book);
        }
    }

    public void setBooks(List<BookSearch> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private TextView bookNumber;
        private ImageView bookCover;

        RecycleViewAdapter.onNoteListener onNoteListener;
        public RecycleViewHolder(@NonNull View itemView, RecycleViewAdapter.onNoteListener onNoteListener) {
            super(itemView);

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            bookCover = itemView.findViewById(R.id.img_cover);
            bookNumber = itemView.findViewById(R.id.number);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        public void bind (BookSearch book) {
            if(book != null && book.getTitle() != null && book.getAuthors() != null) {
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                bookNumber.setText(book.getNumber());
                if(book.getCover() != null) {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
                } else {
                    bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                }
            }
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface onNoteListener {
        public void onNoteClick(int position);
    }
}
