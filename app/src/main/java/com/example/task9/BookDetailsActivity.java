package com.example.task9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.BookDetails.AuthorModel;
import com.example.task9.Models.BookDetails.AuthorService;
import com.example.task9.Models.BookDetails.EditionDetailsModel;
import com.example.task9.Models.BookDetails.EditionDetailsService;
import com.example.task9.Models.BookDetails.WorkDetailsModel;
import com.example.task9.Models.BookDetails.WorkDetailsService;
import com.example.task9.Models.BookSearch;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;
import com.example.task9.Models.RetroInstance;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private EditionDetailsModel edition;
    private WorkDetailsModel work;
    private BookSearch bookSearch;
    private AuthorModel author;
    private ImageView image;
    private Review review = null;

    private TextView titleTextView;
    private TextView authorNameTextView;
    private TextView publisherNameTextView;
    private TextView releaseYearTextView;
    private TextView descriptionTextView;
    private TextView reviewTitleTextView;
    private TextView reviewContentTextView;
    private TextView scoreTextView;
    private TextView titleVideoTextView;
    private TextView titleOriginalTextView;
    private TextView channelNameTextView;

    private Button reviewButton;
    private Button videoButton;
    private Button watchVideoButton;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double accelerationCurrentValueX;
    private double accelerationPreviousValueX;

    private final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        image = findViewById(R.id.imageCover);
        titleTextView = findViewById(R.id.titleBook);
        releaseYearTextView = findViewById(R.id.releaseYearBook);
        descriptionTextView = findViewById(R.id.description);
        publisherNameTextView = findViewById(R.id.publisherBook);

        bookSearch = getIntent().getParcelableExtra("bookId");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Works
        WorkDetailsService workService = RetroInstance.getRetrofitInstance().create(WorkDetailsService.class);
        String workId = bookSearch.getIdWork() + ".json";
        Call<WorkDetailsModel> worksApiCall = workService.findBooks(workId);

        worksApiCall.enqueue(new Callback<WorkDetailsModel>() {
            @Override
            public void onResponse(Call<WorkDetailsModel> call, Response<WorkDetailsModel> response) {
                work = response.body();
                SetParametersBook();
            }

            @Override
            public void onFailure(Call<WorkDetailsModel> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_main_view), "Something went wrong... Please try later!", Snackbar.LENGTH_LONG).show();
            }
        });
        //Editions
        EditionDetailsService bookService = RetroInstance.getRetrofitInstance().create(EditionDetailsService.class);
        String editionId = bookSearch.getEditions().get(0) + ".json";
        Call<EditionDetailsModel> booksApiCall = bookService.findBooks(editionId);

        booksApiCall.enqueue(new Callback<EditionDetailsModel>() {
            @Override
            public void onResponse(Call<EditionDetailsModel> call, Response<EditionDetailsModel> response) {
                edition = response.body();
                SetParametersEdition();
            }

            @Override
            public void onFailure(Call<EditionDetailsModel> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_main_view), "Something went wrong... Please try later!", Snackbar.LENGTH_LONG).show();
            }
        });

        //Authors
        AuthorService authorService = RetroInstance.getRetrofitInstance().create(AuthorService.class);
        String authorId = bookSearch.getAuthorKeys().get(0) + ".json";
        Call<AuthorModel> authorsApiCall = authorService.findAuthor(authorId);

        authorsApiCall.enqueue(new Callback<AuthorModel>() {
            @Override
            public void onResponse(Call<AuthorModel> call, Response<AuthorModel> response) {
                author = response.body();
                SetParametersAuthor();
            }

            @Override
            public void onFailure(Call<AuthorModel> call, Throwable t) {
                t.getCause();
                Snackbar.make(findViewById(R.id.activity_main_view), "Something went wrong... Please try later!", Snackbar.LENGTH_LONG).show();
            }
        });
        SetReviewParameters();
        reviewButton = findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, ReviewEditActivity.class);
                intent.putExtra("review", review);
                intent.putExtra("bookId", bookSearch);
                startActivity(intent);
            }
        });
        videoButton = findViewById(R.id.videoEditButton);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, VideoEditActivity.class);
                intent.putExtra("review", review);
                intent.putExtra("bookId", bookSearch);
                startActivity(intent);
            }
        });
        watchVideoButton = findViewById(R.id.watchVideoButton);
        watchVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review!=null && review.getYtVideoUrl() != null && review.getYtVideoUrl().length() != 0)
                {
                    Intent intent = new Intent(BookDetailsActivity.this, PlayerActivity.class);
                    intent.putExtra("review", review);
                    startActivity(intent);
                } else {
                    Snackbar.make(findViewById(R.id.activity_main_view), "You haven't attached any video yet", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void SetParametersBook()
    {
        if(work.getCovers() != null) {
            Picasso.with(this)
                    .load(IMAGE_URL_BASE + work.getCovers().get(0) + "-M.jpg")
                    .placeholder(R.drawable.ic_baseline_book_24).into(image);
        } else {
            image.setImageResource(R.drawable.ic_baseline_book_24);
        }

        titleTextView.setText(work.getTitle());
        releaseYearTextView.setText(work.getFirstPublishDate());
        if(work.getDescription() instanceof String)
            descriptionTextView.setText((String)work.getDescription());
        else if(work.getDescription() instanceof HashMap) {
            HashMap<String, String> description = (HashMap<String, String>)work.getDescription();
            descriptionTextView.setText(description.get("value"));
        }
        else if(work.getDescription() instanceof LinkedTreeMap) {
            LinkedTreeMap<String, String> description = (LinkedTreeMap<String, String>)work.getDescription();
            descriptionTextView.setText(description.get("value"));
        }

    }
    private void SetReviewParameters()
    {
        BookRepository bookRepository = new BookRepository(this.getApplication());
        BookDatabaseModel book = bookRepository.FindByWorkId(bookSearch.getIdWork());
        if(book != null)
        {
            reviewTitleTextView = findViewById(R.id.reviewTitle);
            reviewContentTextView = findViewById(R.id.reviewContent);
            scoreTextView = findViewById(R.id.score);
            titleVideoTextView = findViewById(R.id.videoEditTitle);
            titleOriginalTextView = findViewById(R.id.originalTitleEditTextView);
            channelNameTextView = findViewById(R.id.channelNameEditTextView);

            ReviewRepository reviewRepository = new ReviewRepository(this.getApplication());
            review = reviewRepository.findReviewByBookId(book.getWorkId());
            if(review != null)
            {
                reviewTitleTextView.setText(review.getTitle());
                scoreTextView.setText("Your Score:  " + review.getRating() + "/10");
                reviewContentTextView.setText(review.getContent());
                titleVideoTextView.setText(review.getYtUserTitle());
                titleOriginalTextView.setText("Title:   " + review.getYtOriginalTitle());
                channelNameTextView.setText("Channel Name:  " + review.getChannelName());
            }
        }
    }
    private void SetParametersEdition()
    {
        if(edition.getPublishers() != null)
            publisherNameTextView.setText(edition.getPublishers().get(0));
        else
            publisherNameTextView.setText("No info about publisher is available");
    }
    private void SetParametersAuthor()
    {
        authorNameTextView = findViewById(R.id.authorBook);
        authorNameTextView.setText(author.getName());
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
            case R.id.videosOptionOnlyMore:
                Intent intent3 = new Intent(this, SavedVideosActivity.class);
                startActivity(intent3);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.only_more_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(BookDetailsActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(BookDetailsActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        review = new ReviewRepository(getApplication()).findReviewByBookId(bookSearch.getIdWork());
        if(review != null)
        {
            channelNameTextView.setText("Title: "+ review.getChannelName());
            titleOriginalTextView.setText("Channel Name:    " + review.getYtOriginalTitle());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        accelerationCurrentValueX = x;
        double changeValue = Math.abs(accelerationCurrentValueX - accelerationPreviousValueX);
        if(changeValue > 15)
        {
            Intent intent = new Intent(BookDetailsActivity.this, ReviewEditActivity.class);
            intent.putExtra("review", review);
            intent.putExtra("bookId", bookSearch);
            startActivity(intent);
        }
        accelerationPreviousValueX = accelerationCurrentValueX;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}