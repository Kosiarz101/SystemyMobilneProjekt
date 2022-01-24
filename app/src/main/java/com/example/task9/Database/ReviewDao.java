package com.example.task9.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.task9.Models.DatabaseModels.Review;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Review review);

    @Update
    public void update(Review review);

    @Delete
    public void delete(Review review);

    @Query("DELETE FROM review")
    public void deleteAll();

    @Query("SELECT * FROM review WHERE bookId == :bookId")
    public Review findReviewByBookId(String bookId);
}
