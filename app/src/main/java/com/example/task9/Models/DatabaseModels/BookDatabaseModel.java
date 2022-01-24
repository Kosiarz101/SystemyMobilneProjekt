package com.example.task9.Models.DatabaseModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book")
public class BookDatabaseModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String workId;
    private String authorId;
    private String authorName;

    public BookDatabaseModel(String title, String workId, String authorId, String authorName) {
        this.title = title;
        this.workId = workId;
        this.authorId = authorId;
        this.authorName = authorName;
    }
    public BookDatabaseModel() {

    }

    protected BookDatabaseModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        workId = in.readString();
        authorId = in.readString();
        authorName = in.readString();
    }

    public static final Creator<BookDatabaseModel> CREATOR = new Creator<BookDatabaseModel>() {
        @Override
        public BookDatabaseModel createFromParcel(Parcel in) {
            return new BookDatabaseModel(in);
        }

        @Override
        public BookDatabaseModel[] newArray(int size) {
            return new BookDatabaseModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(workId);
        dest.writeString(authorId);
        dest.writeString(authorName);
    }
}
