package com.example.praktikumprogmob.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("profile_image")
    @Expose
    private String profile_image;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("notelp")
    @Expose
    private String notelp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }


    public String getProfileimage() {
        return profile_image;
    }

    public void setProfileimage(String profile_image) {
        this.profile_image = profile_image;
    }

    @SerializedName("id")
    @Expose
    private int id;


    @SerializedName("token")
    @Expose
    private String token;

    public String getToken(Context mContext) {
        SharedPreferences  sharedPreferences = mContext.getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        return sharedPreferences.getString("access_token",null);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId(Context mContext) {
        SharedPreferences  sharedPreferences = mContext.getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id",0);
    }

    public void setId(int id) {
        this.id = id;
    }

}

