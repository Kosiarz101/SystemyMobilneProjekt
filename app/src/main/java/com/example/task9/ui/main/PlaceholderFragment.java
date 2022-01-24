package com.example.task9.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task9.BookDetailsActivity;
import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.RecycleViewAdapterReviews;
import com.example.task9.R;
import com.example.task9.databinding.FragmentSavedReviewsBinding;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements RecycleViewAdapterReviews.onNoteListener{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentSavedReviewsBinding binding;
    private List<BookDatabaseModel> bookList;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookList = new BookRepository(getActivity().getApplication()).getAll();
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSavedReviewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView = root.findViewById(R.id.main_recycler_view_reviews);
        final RecycleViewAdapterReviews adapter = new RecycleViewAdapterReviews(this,
                getContext(), new BookRepository(getActivity().getApplication()), new ReviewRepository(getActivity().getApplication()));
        adapter.setReviews(bookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailsActivity.class);
        intent.putExtra("bookId", bookList.get(position));
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(int position) {

    }
}