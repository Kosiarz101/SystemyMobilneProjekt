package com.example.task9.Models;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class BookContainer {
    @SerializedName("docs")
    private List<BookSearch> bookList;
    BookContainer(){
        bookList = new LinkedList<BookSearch>();
    }

    public List<BookSearch> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookSearch> bookList) {
        this.bookList = bookList;
    }
}
