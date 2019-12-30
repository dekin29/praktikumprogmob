package com.example.praktikumprogmob.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.praktikumprogmob.Model.Post;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id")
    List<Post> loadAllPost();

    @Insert
    void insertPost(Post post);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

    @Query("SELECT * FROM posts WHERE id = :id_post")
    Post loadPostById(int id_post);

}
