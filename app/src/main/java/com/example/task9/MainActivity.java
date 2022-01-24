package com.example.task9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.task9.Models.BookSearch;
import com.example.task9.Models.BookContainer;
import com.example.task9.Models.BookService;
import com.example.task9.Models.RecycleViewAdapter;
import com.example.task9.Models.RetroInstance;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecycleViewAdapter.onNoteListener{

    List<BookSearch> bookList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetroInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), "Something went wrong... Please try later!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<BookSearch> books) {
        bookList = books;
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        final RecycleViewAdapter adapter = new RecycleViewAdapter(this, this);
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.savedReviewOptionBookMenu:
                Intent intent = new Intent(this, SavedReviewsActivity.class);
                startActivity(intent);
                return true;
            case R.id.videosOptionBookMenu:
                Intent intent2 = new Intent(this, SavedVideosActivity.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("bookId", bookList.get(position));
        startActivity(intent);
    }

}