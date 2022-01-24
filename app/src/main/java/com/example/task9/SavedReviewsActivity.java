package com.example.task9;

import android.content.Intent;
import android.os.Bundle;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Fragments.BestReviewsFragment;
import com.example.task9.Fragments.NewestReviewsFragment;
import com.example.task9.Fragments.TitleReviewsFragment;
import com.example.task9.Models.BookSearch;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.RecycleViewAdapter;
import com.example.task9.Models.RecycleViewAdapterReviews;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.task9.ui.main.SectionsPagerAdapter;
import com.example.task9.databinding.ActivitySavedReviewsBinding;

import java.util.LinkedList;
import java.util.List;

public class SavedReviewsActivity extends AppCompatActivity implements RecycleViewAdapterReviews.onNoteListener {

    private List<BookDatabaseModel> bookList;
    private Fragment newestFragment;
    private Fragment bestFragment;
    private Fragment titleFragment;

    private Button newestButton;
    private Button bestButton;
    private Button titleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_reviews);

        bookList = new BookRepository(this.getApplication()).getAll();
        newestButton = findViewById(R.id.newestButton);
        bestButton = findViewById(R.id.bestButton);
        titleButton = findViewById(R.id.titleButton);

        newestFragment = NewestReviewsFragment.newInstance("1", "2");
        bestFragment = BestReviewsFragment.newInstance();
        titleFragment = TitleReviewsFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentReviews, newestFragment).commit();

        bestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentReviews, bestFragment).commit();
            }
        });
        newestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentReviews, newestFragment).commit();
            }
        });
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentReviews, titleFragment).commit();
            }
        });

    }
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(SavedReviewsActivity.this, BookDetailsActivity.class);

        intent.putExtra("bookId", bookList.get(0));
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(int position) {
        Snackbar.make(findViewById(R.id.activity_saved_reviews), "hello", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.searchBookOptionBookMenu:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.videosOptionBookMenu:
                Intent intent2 = new Intent(this, SavedVideosActivity.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }
}