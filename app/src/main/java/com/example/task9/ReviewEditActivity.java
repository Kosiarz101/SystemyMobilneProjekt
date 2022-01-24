package com.example.task9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.BookSearch;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;
import com.example.task9.Models.InputFilterMinMax;

public class ReviewEditActivity extends AppCompatActivity {
    private EditText editTitleEditText;
    private EditText editScoreEditText;
    private EditText editContentEditText;
    private Button buttonSave;
    private Review review;

    private BookSearch bookSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_edit);

        editTitleEditText = findViewById(R.id.editReviewTitle);
        editScoreEditText = findViewById(R.id.editReviewScore);
        editScoreEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "10")});
        editContentEditText = findViewById(R.id.editReviewContent);
        buttonSave = findViewById(R.id.saveReviewButton);

        review = getIntent().getParcelableExtra("review");
        bookSearch = getIntent().getParcelableExtra("bookId");
        if(review != null)
        {
            editTitleEditText.setText(review.getTitle());
            editScoreEditText.setText(review.getRating());
            editContentEditText.setText(review.getContent());
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReview();
                Intent intent = new Intent(ReviewEditActivity.this, BookDetailsActivity.class);
                intent.putExtra("bookId", bookSearch);
                startActivity(intent);
            }
        });
    }
    private void saveReview()
    {
        BookRepository bookRepository = new BookRepository(this.getApplication());
        BookDatabaseModel book = bookRepository.FindByWorkId(bookSearch.getIdWork());
        if(book == null)
        {
            book = new BookDatabaseModel(
                    bookSearch.getTitle(), bookSearch.getIdWork(), bookSearch.getAuthorKeys().get(0), bookSearch.getAuthors().get(0)
            );
            bookRepository.insert(book);
        }

        ReviewRepository reviewRepository = new ReviewRepository(this.getApplication());
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
    private void setReviewParameters()
    {
        review.setTitle(editTitleEditText.getText().toString());
        review.setContent(editContentEditText.getText().toString());
        review.setRating(editScoreEditText.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.savedReviewOptionOnlyMore:
                Intent intent = new Intent(this, SavedReviewsActivity.class);
                startActivity(intent);
                break;
            case R.id.searchBookOptionOnlyMore:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.only_more_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}