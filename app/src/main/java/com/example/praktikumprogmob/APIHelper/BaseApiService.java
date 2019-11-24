package com.example.praktikumprogmob.APIHelper;

import com.example.praktikumprogmob.Model.User;
import com.example.praktikumprogmob.Model.UserResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BaseApiService {
    // Fungsi ini untuk memanggil API login
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    // Fungsi ini untuk memanggil API register
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerRequest(@Field("name") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("c_password") String cpassword);

    @POST("details")
    Call<User> getUser(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("gantipassword")
    Call<ResponseBody> gantiPassword(@Field("id") Integer id,
                                     @Field("oldpassword") String old_password,
                                     @Field("password") String password,
                                     @Field("c_password") String cpassword);

    @FormUrlEncoded
    @POST("editprofile")
    Call<ResponseBody> editProfile(@Field("id") Integer id,
                                     @Field("name") String name,
                                     @Field("email") String email,
                                     @Field("notelp") String notelp);

}


