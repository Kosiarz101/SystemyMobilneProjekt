package com.example.task9.Models;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;
import com.example.task9.R;
import com.example.task9.SavedReviewsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapterReviews extends RecyclerView.Adapter<RecycleViewAdapterReviews.RecycleViewHolder> {
    List<Review> reviews;
    List<BookDatabaseModel> books;
    BookRepository bookRepository;
    ReviewRepository reviewRepository;
    Context context;
    RecycleViewAdapterReviews.onNoteListener theOnNoteListener;

    public RecycleViewAdapterReviews(RecycleViewAdapterReviews.onNoteListener theOnNoteListener, Context context, BookRepository bookRepository, ReviewRepository reviewRepository){
        this.theOnNoteListener = theOnNoteListener;
        this.context = context;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
    }
    @NonNull
    @Override
    public RecycleViewAdapterReviews.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_list_item, parent, false);
        return new RecycleViewAdapterReviews.RecycleViewHolder(view, this.theOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterReviews.RecycleViewHolder holder, int position) {
        if(books != null)
        {
            BookDatabaseModel book = books.get(position);
            Review review = reviewRepository.findReviewByBookId(book.getWorkId());
            holder.bind(book, review);
        }
    }

    public void setReviews(List<BookDatabaseModel> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private TextView bookNumber;
        private ImageView bookCover;

        RecycleViewAdapterReviews.onNoteListener onNoteListener;
        public RecycleViewHolder(@NonNull View itemView, RecycleViewAdapterReviews.onNoteListener onNoteListener) {
            super(itemView);

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            bookCover = itemView.findViewById(R.id.img_cover);
            bookNumber = itemView.findViewById(R.id.number);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind (BookDatabaseModel bookDatabaseModel, Review review) {
            if(review != null && bookDatabaseModel != null) {
                bookTitleTextView.setText(bookDatabaseModel.getTitle());
                bookAuthorTextView.setText(bookDatabaseModel.getAuthorName());
                bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                bookNumber.setText(review.getRating() + "/10");
                bookNumber.setTypeface(null, Typeface.BOLD);
            }
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteListener.onNoteLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface onNoteListener {
        public void onNoteClick(int position);
        public void onNoteLongClick(int position);
    }

}