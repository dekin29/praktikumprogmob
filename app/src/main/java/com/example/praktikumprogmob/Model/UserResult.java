package com.example.praktikumprogmob.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResult {
    @SerializedName("buku")
    @Expose
    private List<User> bukus = null;

    public List<User> getBukus() {
        return bukus;
    }
}
