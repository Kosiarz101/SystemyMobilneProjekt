package com.example.task9;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Fragments.TitleReviewsFragment;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;
import com.example.task9.Models.RecycleViewAdapterReviews;
import com.example.task9.Models.RecycleViewAdapterVideos;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SavedVideosActivity extends AppCompatActivity implements RecycleViewAdapterVideos.onNoteListener {

    private List<BookDatabaseModel> bookList;
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_videos);

        bookRepository = new BookRepository(getApplication());
        reviewRepository = new ReviewRepository(getApplication());
        List<BookDatabaseModel> bookListPreview = bookRepository.getAll();
        bookList = new LinkedList<BookDatabaseModel>();
        for (BookDatabaseModel book : bookListPreview) {
            Review review = reviewRepository.findReviewByBookId(book.getWorkId());
            if(review != null && review.getYtVideoUrl() != null && review.getYtVideoUrl() != "")
            {
                bookList.add(book);
            }
        }
        bookList.sort(Comparator.comparing(BookDatabaseModel::getTitle));


        RecyclerView recyclerView = findViewById(R.id.main_recycler_view_videos);
        final RecycleViewAdapterVideos adapter = new RecycleViewAdapterVideos(this,
                this, bookRepository, reviewRepository);
        adapter.setReviews(bookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("review", reviewRepository.findReviewByBookId(bookList.get(position).getWorkId()));
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(int position) {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BookDatabaseModel book = bookList.get(position);
                        Review review = reviewRepository.findReviewByBookId(book.getWorkId());
                        review.setChannelName("None");
                        review.setYtOriginalTitle("None");
                        review.setYtVideoUrl(null);
                        review.setYtUserTitle(null);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.only_more_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.searchBookOptionOnlyMore:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.savedReviewOptionOnlyMore:
                Intent intent2 = new Intent(this, SavedReviewsActivity.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }
}