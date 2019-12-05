package com.example.praktikumprogmob.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostResult {
    @SerializedName("post")
    @Expose

    private ArrayList<Post> posts = null;

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
