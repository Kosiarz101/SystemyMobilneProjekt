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
import com.example.task9.Models.OperationManager;
import com.example.task9.Models.RecycleViewAdapterReviews;
import com.example.task9.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewestReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewestReviewsFragment extends Fragment implements RecycleViewAdapterReviews.onNoteListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<BookDatabaseModel> bookList;
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;

    private String mParam1;
    private String mParam2;

    public NewestReviewsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewestReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewestReviewsFragment newInstance(String param1, String param2) {
        NewestReviewsFragment fragment = new NewestReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookRepository = new BookRepository(getActivity().getApplication());
        reviewRepository = new ReviewRepository(getActivity().getApplication());
        bookList = bookRepository.getAll();
        bookList.sort(Comparator.comparing(BookDatabaseModel::getId).reversed());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newest_reviews, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view_reviews2);
        final RecycleViewAdapterReviews adapter = new RecycleViewAdapterReviews(this,
                getContext(), bookRepository, reviewRepository);
        adapter.setReviews(bookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailsActivity.class);
        intent.putExtra("bookId", OperationManager.CastBookDatabaseModelToBookSearch(bookList.get(0)));
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