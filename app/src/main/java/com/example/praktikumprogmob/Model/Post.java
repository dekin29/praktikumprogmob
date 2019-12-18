package com.example.praktikumprogmob.Model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("judul")
    @Expose
    private String judul;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;

    @SerializedName("kontak")
    @Expose
    private String kontak;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    protected Post(Parcel in) {
        id = in.readString();
        id_user = in.readString();
        user_name = in.readString();
        judul = in.readString();
        foto = in.readString();
        deskripsi = in.readString();
        kontak = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_user);
        dest.writeString(user_name);
        dest.writeString(judul);
        dest.writeString(foto);
        dest.writeString(deskripsi);
        dest.writeString(kontak);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String usern_name) {
        this.user_name = user_name;
    }
}
