package com.example.task9.Models;

import com.example.task9.Models.DatabaseModels.BookDatabaseModel;

import java.util.LinkedList;

public class OperationManager {
    public static BookSearch CastBookDatabaseModelToBookSearch(BookDatabaseModel bookDatabaseModel)
    {
        BookSearch bookSearch = new BookSearch();

        bookSearch.setIdWork(bookDatabaseModel.getWorkId());

        LinkedList<String> authorKeyList = new LinkedList<String>();
        authorKeyList.add(bookDatabaseModel.getAuthorId());
        bookSearch.setAuthorKeys(authorKeyList);

        LinkedList<String> editionKeyList = new LinkedList<String>();
        editionKeyList.add(bookDatabaseModel.getEditionId());
        bookSearch.setEditions(editionKeyList);
        return bookSearch;
    }
}
