package com.example.task9.Models.BookDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class AuthorModel {
    @SerializedName("name")
    private String name;
    @SerializedName("bio")
    private Object bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        try
        {
            return (String)bio;
        } catch (Exception e){
            return "there is no bio available";
        }
    }

    public void setBio(Object bio) {
        this.bio = bio;
    }
}
