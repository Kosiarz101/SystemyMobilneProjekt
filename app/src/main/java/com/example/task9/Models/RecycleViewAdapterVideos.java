package com.example.task9.Models;

import android.content.Context;
import android.graphics.Typeface;
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

import java.util.List;

public class RecycleViewAdapterVideos extends RecyclerView.Adapter<RecycleViewAdapterVideos.RecycleViewHolder> {
    List<Review> reviews;
    List<BookDatabaseModel> books;
    BookRepository bookRepository;
    ReviewRepository reviewRepository;
    Context context;
    RecycleViewAdapterVideos.onNoteListener theOnNoteListener;

    public RecycleViewAdapterVideos(RecycleViewAdapterVideos.onNoteListener theOnNoteListener, Context context, BookRepository bookRepository, ReviewRepository reviewRepository){
        this.theOnNoteListener = theOnNoteListener;
        this.context = context;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
    }
    @NonNull
    @Override
    public RecycleViewAdapterVideos.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        return new RecycleViewAdapterVideos.RecycleViewHolder(view, this.theOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterVideos.RecycleViewHolder holder, int position) {
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

        private TextView bookVideoTextView;
        private TextView videoTitleTextView;
        private TextView authorVideoTextView;
        private ImageView videoCover;

        RecycleViewAdapterVideos.onNoteListener onNoteListener;
        public RecycleViewHolder(@NonNull View itemView, RecycleViewAdapterVideos.onNoteListener onNoteListener) {
            super(itemView);

            bookVideoTextView = itemView.findViewById(R.id.bookVideoTextView);
            videoTitleTextView = itemView.findViewById(R.id.videoTitleTextView);
            videoCover = itemView.findViewById(R.id.img_cover);
            authorVideoTextView = itemView.findViewById(R.id.authorVideoTextView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind (BookDatabaseModel bookDatabaseModel, Review review) {
            if(review != null && bookDatabaseModel != null) {
                bookVideoTextView.setText(bookDatabaseModel.getTitle() + ", " + bookDatabaseModel.getAuthorName());
                videoTitleTextView.setText(review.getYtOriginalTitle());
                videoCover.setImageResource(R.drawable.ic_baseline_ondemand_video_24);
                authorVideoTextView.setText(review.getChannelName());
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
