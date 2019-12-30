package com.example.praktikumprogmob.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailPostResult {
    @SerializedName("post")
    @Expose
    private ArrayList<Post> posts = null;

    @SerializedName("komentar")
    @Expose
    private ArrayList<Komentar> komentars = null;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Komentar> getKomentars() {
        return komentars;
    }

}
