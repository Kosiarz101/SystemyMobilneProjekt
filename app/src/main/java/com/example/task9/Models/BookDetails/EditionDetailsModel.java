package com.example.task9.Models.BookDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditionDetailsModel {
    @SerializedName("publishers")
    List<String> publishers;
    @SerializedName("number_of_pages")
    String numberOfPages;
    @SerializedName("covers")
    List<String> covers;
    @SerializedName("title")
    String title;
    @SerializedName("publish_date")
    String publishDate;

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
