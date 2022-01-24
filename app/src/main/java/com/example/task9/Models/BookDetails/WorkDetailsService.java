package com.example.task9.Models.BookDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WorkDetailsService {
    @GET("{id}")
    Call<WorkDetailsModel> findBooks(@Path("id") String id);
}
