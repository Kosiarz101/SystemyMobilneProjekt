package com.example.task9.Models.BookDetails;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class WorkDetailsModel {
    @SerializedName("description")
    private Object description;
    @SerializedName("covers")
    private List<String> covers;
    @SerializedName("title")
    private String title;
    @SerializedName("first_publish_date")
    private String firstPublishDate;

    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstPublishDate() {
        return firstPublishDate;
    }

    public void setFirstPublishDate(String firstPublishDate) {
        this.firstPublishDate = firstPublishDate;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }
}
