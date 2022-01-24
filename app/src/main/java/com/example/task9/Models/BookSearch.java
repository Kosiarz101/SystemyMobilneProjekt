package com.example.task9.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class BookSearch implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("author_name")
    private List<String> authors;
    @SerializedName("cover_i")
    private String cover;
    private String number;
    @SerializedName("key")
    private String idWork;
    @SerializedName("edition_key")
    private List<String> editions;
    @SerializedName("author_key")
    private List<String> authorKeys;

    public BookSearch(){
        authors = new LinkedList<String>();
    }

    protected BookSearch(Parcel in) {
        title = in.readString();
        authors = in.createStringArrayList();
        cover = in.readString();
        number = in.readString();
        idWork = in.readString();
        editions = in.createStringArrayList();
        authorKeys = in.createStringArrayList();
    }

    public static final Creator<BookSearch> CREATOR = new Creator<BookSearch>() {
        @Override
        public BookSearch createFromParcel(Parcel in) {
            return new BookSearch(in);
        }

        @Override
        public BookSearch[] newArray(int size) {
            return new BookSearch[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIdWork() {
        return idWork;
    }

    public void setIdWork(String idWork) {
        this.idWork = idWork;
    }

    public List<String> getEditions() {
        return editions;
    }

    public void setEditions(List<String> editions) {
        this.editions = editions;
    }

    public List<String> getAuthorKeys() {
        return authorKeys;
    }

    public void setAuthorKeys(List<String> authorKeys) {
        this.authorKeys = authorKeys;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeString(cover);
        dest.writeString(number);
        dest.writeString(idWork);
        dest.writeStringList(editions);
        dest.writeStringList(authorKeys);
    }
}
