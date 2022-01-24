package com.example.task9.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;

public class BookAndReview {
    @Embedded public BookDatabaseModel book;
    @Relation(
            parentColumn = "Id",
            entityColumn = "bookId"
    )
    public Review review;
}
