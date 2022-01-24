package com.example.task9.Models.BookDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EditionDetailsService {
    @GET("books/{id}")
    Call<EditionDetailsModel> findBooks(@Path("id") String id);
}
