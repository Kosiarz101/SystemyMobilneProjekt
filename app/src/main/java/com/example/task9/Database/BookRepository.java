package com.example.task9.Database;

import android.app.Application;

import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;

    public BookRepository(Application application) {
        DatabaseInstance instance = DatabaseInstance.getInstance(application);
        bookDao = instance.bookDao();
    }
    public void insert(BookDatabaseModel book) {
        bookDao.insert(book);
    }

    public void update(BookDatabaseModel book) {
        bookDao.update(book);
    }

    public void delete(BookDatabaseModel book) {
        bookDao.delete(book);
    }
    public BookDatabaseModel FindByWorkId(String workId)
    {
       BookDatabaseModel book = bookDao.findByWorkId(workId);
       return book;
    }
    public List<BookDatabaseModel> getAll(){return bookDao.getAll();}
    public void deleteAll(){
        bookDao.deleteAll();
    }
}
