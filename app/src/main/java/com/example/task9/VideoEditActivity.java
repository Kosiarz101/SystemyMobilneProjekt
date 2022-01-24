package com.example.task9;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.BookSearch;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;
import com.google.android.material.snackbar.Snackbar;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class VideoEditActivity extends AppCompatActivity {

    public EditText titleEditView;
    public EditText urlEditView;
    public Button saveButton;

    public Review review;
    public BookSearch bookSearch;

    public ReviewRepository reviewRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        titleEditView = findViewById(R.id.videoTitleEditText);
        urlEditView = findViewById(R.id.videoUrlEditText);
        saveButton = findViewById(R.id.saveVideoButton);

        review = getIntent().getParcelableExtra("review");
        bookSearch = getIntent().getParcelableExtra("bookId");

        if(review != null)
        {
            titleEditView.setText(review.getYtUserTitle());
            urlEditView.setText(review.getYtVideoUrl());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReview();
                Intent intent = new Intent(VideoEditActivity.this, BookDetailsActivity.class);
                intent.putExtra("bookId", bookSearch);
                startActivity(intent);
            }
        });
    }

    private void saveReview() {
        BookRepository bookRepository = new BookRepository(this.getApplication());
        BookDatabaseModel book = bookRepository.FindByWorkId(bookSearch.getIdWork());
        if(book == null)
        {
            book = new BookDatabaseModel(
                    bookSearch.getTitle(), bookSearch.getIdWork(), bookSearch.getAuthorKeys().get(0), bookSearch.getAuthors().get(0), bookSearch.getEditions().get(0)
            );
            bookRepository.insert(book);
        }

        reviewRepository = new ReviewRepository(this.getApplication());
        if(review == null)
        {
            review = new Review();
            review.setBookId(bookSearch.getIdWork());
            setReviewParameters();
            reviewRepository.insert(review);
        }
        else {
            setReviewParameters();
            reviewRepository.update(review);
        }
    }

    private void setReviewParameters() {
        review.setYtUserTitle(titleEditView.getText().toString());
        review.setYtVideoUrl(urlEditView.getText().toString());
        review.setYtOriginalTitle("None");
        review.setChannelName("None");
        //Snackbar.make(findViewById(R.id.activity_main_view), "Wrong url - video doesn't exist", Snackbar.LENGTH_SHORT).show();
    }
}