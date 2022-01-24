package com.example.task9.Models.BookDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthorService {
    @GET("authors/{id}")
    Call<AuthorModel> findAuthor(@Path("id") String id);
}
