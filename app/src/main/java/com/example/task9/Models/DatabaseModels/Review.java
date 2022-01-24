package com.example.task9.Models.DatabaseModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "review")
public class Review implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private String rating;
    private String bookId;
    private String ytUserTitle;
    private String ytVideoUrl;
    private String channelName;
    private String ytOriginalTitle;

    public Review(String title, String content, String rating, String bookId) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.bookId = bookId;
    }
    public Review() {
    }

    protected Review(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        rating = in.readString();
        bookId = in.readString();
        ytUserTitle = in.readString();
        ytVideoUrl = in.readString();
        channelName = in.readString();
        ytOriginalTitle = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getYtUserTitle() {
        return ytUserTitle;
    }

    public void setYtUserTitle(String ytUserTitle) {
        this.ytUserTitle = ytUserTitle;
    }

    public String getYtVideoUrl() {
        return ytVideoUrl;
    }

    public void setYtVideoUrl(String ytVideoUrl) {
        this.ytVideoUrl = ytVideoUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getYtOriginalTitle() {
        return ytOriginalTitle;
    }

    public void setYtOriginalTitle(String ytOriginalTitle) {
        this.ytOriginalTitle = ytOriginalTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(rating);
        dest.writeString(bookId);
        dest.writeString(ytUserTitle);
        dest.writeString(ytVideoUrl);
        dest.writeString(channelName);
        dest.writeString(ytOriginalTitle);
    }
}
