package com.example.praktikumprogmob.Model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "posts")
public class Post implements Parcelable {
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name="id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name="id_user")
    @SerializedName("id_user")
    @Expose
    private int id_user;

    @ColumnInfo(name="id_kategori")
    @SerializedName("id_kategori")
    @Expose
    private int id_kategori;

    @SerializedName("nama_kategori")
    @Expose
    private String nama_kategori;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @ColumnInfo(name="judul")
    @SerializedName("judul")
    @Expose
    private String judul;

    @ColumnInfo(name="foto")
    @SerializedName("foto")
    @Expose
    private String foto;

    @ColumnInfo(name="deskripsi")
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;

    @ColumnInfo(name="kontak")
    @SerializedName("kontak")
    @Expose
    private String kontak;

    @ColumnInfo(name="created_at")
    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @Ignore
    public Post(int id_user, int id_kategori, String judul, String foto, String deskripsi, String kontak, String created_at) {
        this.id_user = id_user;
        this.id_kategori = id_kategori;
        this.judul = judul;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.kontak = kontak;
        this.created_at = created_at;
    }

    @Ignore
    public Post(int id_kategori, String judul, String foto, String deskripsi, String kontak) {
        this.id_kategori = id_kategori;
        this.judul = judul;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.kontak = kontak;
    }

    public Post(int id, int id_user, int id_kategori, String judul, String foto, String deskripsi, String kontak, String created_at) {
        this.id = id;
        this.id_user = id_user;
        this.id_kategori = id_kategori;
        this.judul = judul;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.kontak = kontak;
        this.created_at = created_at;
    }

    protected Post(Parcel in) {
        id = in.readInt();
        id_user = in.readInt();
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
        dest.writeInt(id);
        dest.writeInt(id_user);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
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
