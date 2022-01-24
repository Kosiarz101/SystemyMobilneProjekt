package com.example.task9.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.task9.BookDetailsActivity;
import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;
import com.example.task9.Models.RecycleViewAdapterReviews;
import com.example.task9.R;

import java.util.Comparator;
import java.util.List;

public class BestReviewsFragment extends Fragment implements RecycleViewAdapterReviews.onNoteListener{
    private List<BookDatabaseModel> bookList;
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;

    public BestReviewsFragment() {
        // Required empty public constructor
    }

    public static BestReviewsFragment newInstance() {
        BestReviewsFragment fragment = new BestReviewsFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookList = new BookRepository(getActivity().getApplication()).getAll();
        //bookList.sort(Comparator.comparing(BookDatabaseModel::getTitle));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newest_reviews, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view_reviews2);
        final RecycleViewAdapterReviews adapter = new RecycleViewAdapterReviews(this,
                getContext(), new BookRepository(getActivity().getApplication()), new ReviewRepository(getActivity().getApplication()));
        adapter.setReviews(bookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailsActivity.class);
        intent.putExtra("bookId", bookList.get(position));
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(int position) {
        AlertDialog alert = new AlertDialog.Builder(getContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BookDatabaseModel book = bookList.get(position);
                        Review review = reviewRepository.findReviewByBookId(book.getWorkId());
                        bookRepository.delete(book);
                        reviewRepository.delete(review);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}