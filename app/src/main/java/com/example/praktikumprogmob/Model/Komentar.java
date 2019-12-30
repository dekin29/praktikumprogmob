package com.example.praktikumprogmob.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Komentar {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("id_post")
    @Expose
    private String id_post;

    @SerializedName("id_user")
    @Expose
    private int id_user;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("komentar")
    @Expose
    private String komentar;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
