package com.example.task9.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.task9.Models.DatabaseModels.BookDatabaseModel;

import java.util.List;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(BookDatabaseModel book);

    @Update
    public void update(BookDatabaseModel book);

    @Delete
    public void delete(BookDatabaseModel book);

    @Query("SELECT * FROM book")
    public List<BookDatabaseModel> getAll();

    @Query("DELETE FROM book")
    public void deleteAll();

    @Query("SELECT * FROM book WHERE workId LIKE :workId")
    public BookDatabaseModel findByWorkId(String workId);

}
